package com.ismin.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BOOKS = "books"

class BookListFragment : Fragment() {

    private lateinit var books: ArrayList<Book>
    private lateinit var bookAdapter: BookAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var bookService: BookService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            books = it.getSerializable(BOOKS) as ArrayList<Book>
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            bookService = context.getBookService()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_book_list, container, false)

        // Initialiser le RecyclerView et l'adaptateur
        recyclerView = rootView.findViewById(R.id.f_book_list_rcv_books)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )


        // Initialiser l'adaptateur après avoir récupéré les livres et passé bookService
        bookAdapter = BookAdapter(books, { book -> onBookClick(book) }, bookService)

        recyclerView.adapter = bookAdapter

        return rootView
    }

    // Gérer le clic sur un livre
    private fun onBookClick(book: Book) {
        val intent = Intent(activity, BookDetailActivity::class.java)
        intent.putExtra("book", book)  // Passer l'objet Book
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(books: ArrayList<Book>) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOKS, books)
                }
            }
    }
}
