package com.ismin.android

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_BASE_URL = "https://app-45e08025-ddc4-403c-bce0-2cfa898a6ac1.cleverapps.io"

class MainActivity : AppCompatActivity() {

    private val bookshelf = Bookshelf()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(SERVER_BASE_URL)
        .build()
    private val bookService = retrofit.create(BookService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Récupérer la liste des livres depuis le serveur
        bookService.getAllBooks().enqueue(object : Callback<List<Book>> {
            override fun onResponse(
                call: Call<List<Book>>, response: Response<List<Book>>
            ) {
                val allBooks: List<Book>? = response.body()
                allBooks?.forEach { bookshelf.addBook(it) }
                displayBookListFragment() // Affiche le fragment de liste par défaut
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Toast.makeText(baseContext, "Quelque chose s'est mal passé", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        // Configure la Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Afficher par défaut le fragment de liste
        displayBookListFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                displayBookListFragment()
                true
            }
            R.id.action_map -> {
                displayMapFragment()
                true
            }
            R.id.action_info -> {
                displayInfoFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Cette méthode permet d'accéder à l'instance de bookService
    fun getBookService(): BookService {
        return bookService
    }

    private fun displayBookListFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = BookListFragment.newInstance(bookshelf.getAllBooks())
        fragmentTransaction.replace(R.id.a_main_lyt_container, fragment)
        fragmentTransaction.commit()
    }

    private fun displayMapFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = MapFragment.newInstance(bookshelf.getAllBooks())
        fragmentTransaction.replace(R.id.a_main_lyt_container, fragment)
        fragmentTransaction.commit()
    }

    private fun displayInfoFragment() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = InfoFragment.newInstance(bookshelf.getAllBooks())
        fragmentTransaction.replace(R.id.a_main_lyt_container, fragment)
        fragmentTransaction.commit()
    }
}
