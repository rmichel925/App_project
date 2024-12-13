import { Injectable, OnModuleInit } from '@nestjs/common';
import { readFile } from 'node:fs/promises';
import { HttpService } from '@nestjs/axios';
import {AxiosResponse} from "axios";
import { Book } from './Book';  // Interface Book pour le service
import { APIBook } from './APIBook'; // Interface APIBook pour les données de l'API
import { GlobalBookAPI } from './GlobalBookAPI';
import { EmptyError } from 'rxjs';

@Injectable()
export class BookService implements OnModuleInit {
  private readonly storage: Map<string, Book> = new Map();

  async onModuleInit() {
    // Charger les livres depuis le fichier local et en ligne
    Promise.all([/*this.loadBooksFromFile(), */this.readBooksOnline()]).then(() => {
      console.log('Initialisation terminée.');
      console.log(this.getAllBooks());
    });
  }

  // Charger les livres depuis un fichier local
 /* async loadBooksFromFile(): Promise<APIBook[]> {
    const data = await readFile('../books.json', 'utf-8');
    const apiBooks: GlobalBookAPI[] = JSON.parse(data);

    apiBooks["results"].forEach((book) => {
      this.addBook({
        isbn: book.objectid.toString(),
        title: book.nom_de_l_oeuvre,
        author: book.nom_de_l_artiste,
        date: book.date_de_creation,
        coordonnees: book.coord,
        emplacement: book.emplacement,
        favoris: false,
      });
    });
    console.log(this.getAllBooks());
    return apiBooks["results"];
  }*/

  // Charger les livres depuis une API externe
  async readBooksOnline(): Promise<Book[]> {
    const httpService: HttpService = new HttpService();
    const response: AxiosResponse<GlobalBookAPI[]> = await httpService
      .get<GlobalBookAPI[]>(
        'https://opendata.hauts-de-seine.fr/api/explore/v2.1/catalog/datasets/fr-833718794-oeuvres-d-art/records?limit=20', // Remplacez avec l'URL de l'API
      )
      .toPromise();
    const apiBooks = response.data;

    if (!apiBooks || !apiBooks["results"]) {
      console.error('Données invalides reçues de l\'API');
      return [];
    }

    apiBooks["results"].forEach((book) => {
      this.addBook({
        isbn: book.objectid.toString(),
        title: book.nom_de_l_oeuvre,
        author: book.nom_de_l_artiste,
        date: book.date_de_creation,
        coordonnees: book.coord,
        emplacement: book.emplacement,
        favoris: false,
      });
    });

    return apiBooks["results"];
  }

  addBook(book: Book) {
    this.storage.set(book.isbn, book); // Ajout du livre dans le stockage interne
  }

  getBook(isbn: string): Book {
    const book = this.storage.get(isbn);
    if (!book) {
      throw new Error(`Book with ISBN ${isbn} not found`);
    }
    return book;
  }

  getAllBooks(): Book[] {
    return Array.from(this.storage.values()).sort((a, b) =>
      a.title.localeCompare(b.title),
    );
  }

  getBooksOf(author: string): Book[] {
    return this.getAllBooks()
      .filter((book) => book.author === author)
      .sort((a, b) => a.title.localeCompare(b.title));
  }

  remove(isbn: string) {
    this.storage.delete(isbn);
  }

  search(term: string): Book[] {
    return Array.from(this.storage.values())
      .filter(
        (book) =>
          book.title.includes(term) || book.author.includes(term),
      )
      .sort((a, b) => a.title.localeCompare(b.title));
  }

  setFavoris(isbn: string, favoris: boolean): Book {
    const elem = this.getBook(isbn);
    elem.favoris = favoris;
    this.storage.set(isbn, elem);
    return elem
  }

  updateBook(Book: Book) {
    return this.storage.set(Book.isbn, Book);
  }
}
