package com.ismin.android


class Bookshelf {
    private val storage = HashMap<String, Book>()

    fun addBook(book: Book) {
        storage[book.isbn] = book
    }

    fun getBook(isbn: String): Book {
        return storage[isbn] ?: throw Exception("Book not found")
    }

    fun getAllBooks(): ArrayList<Book> {
        return ArrayList(storage.values
            .sortedBy { book -> book.title })
    }

    fun getBooksOf(author: String): List<Book> {
        return storage.filterValues { book -> book.author.equals(author) }
            .values
            .sortedBy { book -> book.title }
    }

    fun getTotalNumberOfBooks(): Int {
        return storage.size
    }


    fun clear() {
        storage.clear()
    }
}