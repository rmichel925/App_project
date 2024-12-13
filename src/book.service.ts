import { Injectable, Logger } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { firstValueFrom } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { APIBook } from './APIBook';
import { Book } from './Book';
import { readFile } from 'node:fs/promises';

@Injectable()
export class BookService {
  private readonly logger = new Logger(BookService.name);
  private readonly storage: Map<string, Book> = new Map();
  
  constructor(private readonly httpService: HttpService) {}

  // Méthode pour charger les livres depuis un fichier local et l'API
  async onModuleInit() {
    this.logger.log('Loading books from file and API');
    await Promise.all([this.loadBooksFromFile(), this.loadBooksFromApi()]);
    this.logger.log(`${this.storage.size} books loaded`);
  }

  // Chargement des livres depuis un fichier JSON
  private async loadBooksFromFile() {
    try {
      const data = await readFile('src/dataset.json', 'utf8');
      const books = JSON.parse(data.toString()) as Book[];
      books.forEach((book) => this.addBook(book));
    } catch (error) {
      this.logger.error('Error loading books from file', error);
    }
  }

  // Chargement des livres depuis une API externe (Open Data)
  private async loadBooksFromApi() {
    try {
      const response = await firstValueFrom(
        this.httpService
          .get<APIBook[]>('https://opendata.hauts-de-seine.fr/api/explore/v2.1/catalog/datasets/fr-833718794-oeuvres-d-art/records?limit=20')  // Remplacez par votre URL Open Data
          .pipe(
            map((response) => response.data),  // Extraction des données de la réponse
            map((apiBooks) =>
              apiBooks.map((apiBook) => ({
                isbn: apiBook.objectid,  // Assurez-vous que les noms des propriétés correspondent aux données API
                title: apiBook.nom_de_l_oeuvre,
                author: apiBook.nom_de_l_artiste,
                date: apiBook.date_de_creation,
                coordonnees: apiBook.coord,  // Assurez-vous que ce champ existe dans votre réponse
                emplacement: apiBook.emplacement,
                favoris: false,  // Valeur par défaut pour "favoris"
              })),
            ),
            tap((books) => books.forEach((book) => this.addBook(book))),  // Ajouter les livres à l'application
          ),
      );
    } catch (error) {
      this.logger.error('Error loading books from API', error);
    }
  }

  // Ajouter un livre à la base de données locale
  addBook(book: Book) {
    this.storage.set(book.isbn.toString(), book);
  }

  // Récupérer un livre par son ISBN
  getBook(isbn: string): Book {
    const book = this.storage.get(isbn);
    if (!book) {
      throw new Error(`Book with ISBN ${isbn} not found`);
    }
    return book;
  }

  // Récupérer tous les livres
  getAllBooks(): Book[] {
    return Array.from(this.storage.values()).sort((a, b) => a.title.localeCompare(b.title));
  }

  // Récupérer les livres d'un auteur spécifique
  getBooksOf(author: string): Book[] {
    return this.getAllBooks()
      .filter((book) => book.author === author)
      .sort((a, b) => a.title.localeCompare(b.title));
  }

  // Supprimer un livre par son ISBN
  remove(isbn: string) {
    this.storage.delete(isbn);
  }

  // Rechercher des livres par titre ou auteur
  search(term: string) {
    return Array.from(this.storage.values())
      .filter((book) => book.title.includes(term) || book.author.includes(term))
      .sort((a, b) => a.title.localeCompare(b.title));
  }
}
