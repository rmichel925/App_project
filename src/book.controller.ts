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
    return this.bookService.getBook(book.objectid); // Utilisation de objectid
  }

  @Get()
  getBooks(@Query('author') author: string): Book[] {
    if (author) {
      return this.bookService.getBooksOf(author);
    }
    return this.bookService.getAllBooks();
  }

  @Get(':objectid')
  getBook(@Param('objectid') objectid: number): Book {
    return this.bookService.getBook(objectid); // Utilisation de objectid
  }

  @Delete(':objectid')
  deleteBook(@Param('objectid') objectid: number): void {
    this.bookService.remove(objectid); // Utilisation de objectid
  }

  @Post('search')
  @HttpCode(200)
  searchBooks(@Body() { term }: { term: string }): Book[] {
    return this.bookService.search(term);
  }
}
