import { Injectable, Logger, OnModuleInit  } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { APIBook } from './APIBook';
import { Book } from './Book';
import { readFile } from 'node:fs/promises';


@Injectable()
export class BookService implements OnModuleInit {
  private readonly logger = new Logger(BookService.name);
  private readonly storage: Map<number, Book> = new Map(); // Utilisation de objectid comme clé

  constructor(private readonly httpService: HttpService) {}

  async onModuleInit() {
    this.logger.log('Loading books from file and API');
    await Promise.all([this.loadBooksFromFile(), this.loadBooksFromApi()]);
    this.logger.log(`${this.storage.size} books loaded`);
  }

  private async loadBooksFromFile() {
    // Charger les livres depuis le fichier local (dataset.json)
    const data = await readFile('src/dataset.json', 'utf8');
    const books = JSON.parse(data.toString()) as Book[];
    books.forEach((book) => this.addBook(book));
  }

  private async loadBooksFromApi() {
    // Remplacez l'URL par celle de votre base de données open data
    const apiUrl = 'https://opendata.hauts-de-seine.fr/api/explore/v2.1/catalog/datasets/fr-833718794-oeuvres-d-art/records?limit=20'; // Exemple d'URL
    const response = await firstValueFrom(
      this.httpService.get(apiUrl).pipe(
        map((response) => response.data), // Récupération des données de la réponse
        map((apiBooks) => 
          apiBooks.map((apiBook) => ({
            objectid: apiBook.objectid, // objectid comme clé primaire
            nom_de_l_oeuvre: apiBook.nom_de_l_oeuvre, // Title
            nom_de_l_artiste: apiBook.nom_de_l_artiste, // Author
            date_de_creation: apiBook.date_de_creation, // Date
            coord: apiBook.coord, // Coordonnées
            emplacement: apiBook.emplacement, // Emplacement
            favoris: apiBook.favoris, // Favoris
          }))
        ),
        tap((books) => books.forEach((book) => this.addBook(book)))
      )
    );
  }

  addBook(book: Book) {
    this.storage.set(book.objectid, book); // Utilisation de objectid pour ajouter
  }

  getBook(objectid: number): Book {
    const book = this.storage.get(objectid);

    if (!book) {
      throw new Error(`Book with objectid ${objectid} not found`);
    }

    return book;
  }

  getAllBooks(): Book[] {
    return Array.from(this.storage.values()).sort((a, b) =>
      a.nom_de_l_oeuvre.localeCompare(b.nom_de_l_oeuvre),
    );
  }

  getBooksOf(author: string): Book[] {
    return this.getAllBooks()
      .filter((book) => book.nom_de_l_artiste === author)
      .sort((a, b) => a.nom_de_l_oeuvre.localeCompare(b.nom_de_l_oeuvre));
  }

  remove(objectid: number) {
    this.storage.delete(objectid);
  }

  search(term: string) {
    return Array.from(this.storage.values())
      .filter(
        (book) =>
          book.nom_de_l_oeuvre.includes(term) || book.nom_de_l_artiste.includes(term),
      )
      .sort((a, b) => a.nom_de_l_oeuvre.localeCompare(b.nom_de_l_oeuvre));
  }
}

