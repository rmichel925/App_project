import {
  Body,
  Controller,
  Delete,
  Get,
  HttpCode,
  Param,
  Post,
  Query,
} from '@nestjs/common';
import { BookService } from './book.service';
import { Book } from './Book'; // Importation du modèle Book

@Controller('/books')
export class BookController {
  constructor(private readonly bookService: BookService) {}

  @Post()
  createBook(@Body() book: Book): Book {
    this.bookService.addBook(book);
    return this.bookService.getBook(book.isbn); // Utilisation de ISBN comme clé
  }

  @Get()
  getBooks(@Query('author') author: string): Book[] {
    if (author) {
      return this.bookService.getBooksOf(author);
    }
    return this.bookService.getAllBooks();
  }

  @Get(':isbn')
  getBook(@Param('isbn') isbn: string): Book { // Paramètre ISBN sous forme de string
    return this.bookService.getBook(isbn); // Passer ISBN comme string
  }

  @Delete(':isbn')
  deleteBook(@Param('isbn') isbn: string): void { // Paramètre ISBN sous forme de string
    this.bookService.remove(isbn);
  }

  @Post('search')
  @HttpCode(200)
  searchBooks(@Body() { term }: { term: string }): Book[] {
    return this.bookService.search(term);
  }
}
