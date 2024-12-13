import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { Book } from './Book';  // Interface Book pour le service
import { APIBook } from './APIBook'; // Interface APIBook pour les données de l'API

@Injectable()
export class BookService implements OnModuleInit {
  private readonly logger = new Logger(BookService.name);
  private readonly storage: Map<string, Book> = new Map(); // Utilisation de ISBN comme clé

  constructor(private readonly httpService: HttpService) {}

  async onModuleInit() {
    this.logger.log('Loading books from file and API');
    await Promise.all([this.loadBooksFromFile(), this.loadBooksFromApi()]);
    this.logger.log(`${this.storage.size} books loaded`);
  }

  private async loadBooksFromFile() {
    // Charger les livres depuis le fichier local (dataset.json) ou une autre source
    // Assurez-vous que le fichier ou la source existe
    // Exemple : const data = await readFile('src/dataset.json', 'utf8');
    const data = []; // Remplacer par le chargement réel
    const books: Book[] = JSON.parse(data.toString());  // Exemple fictif de données
    books.forEach((book) => this.addBook(book));
  }

  private async loadBooksFromApi() {
    // Remplacez l'URL par celle de votre base de données open data
    const apiUrl = 'https://opendata.hauts-de-seine.fr/api/explore/v2.1/catalog/datasets/fr-833718794-oeuvres-d-art/records?limit=20'; // Exemple d'URL
    const response = await firstValueFrom(
      this.httpService.get(apiUrl).pipe(
        map((response) => response.data), // Récupération des données
        map((apiBooks: APIBook[]) =>
          apiBooks.map((apiBook) => ({
            isbn: apiBook.objectid.toString(), // Conversion objectid en string (ISBN)
            title: apiBook.nom_de_l_oeuvre,
            author: apiBook.nom_de_l_artiste,
            date: apiBook.date_de_creation,
            coordonnees: apiBook.coord,
            emplacement: apiBook.emplacement,
            favoris: false,  // Par défaut, 'favoris' est faux
          }))
        ),
        tap((books: Book[]) => books.forEach((book) => this.addBook(book)))
      )
    );
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
}
