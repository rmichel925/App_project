package com.ismin.android

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookAdapter(private var books: List<Book>, private val onBookClick: (Book) -> Unit, private val bookService: BookService) : RecyclerView.Adapter<BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val rowView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_book, parent, false)
        return BookViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.txvIsbn.text = "ISBN: ${book.isbn}"
        holder.txvTitle.text = book.title
        holder.txvAuthor.text = book.author
        holder.txvDate.text = book.date

        // Mettre à jour l'image selon l'emplacement
        updateEmplacementImage(holder, book)



        // Gérer le clic sur un élément de la liste
        holder.itemView.setOnClickListener {
            onBookClick(book)  // Appeler le callback avec l'objet Book cliqué
        }

        // Gérer l'image de l'étoile selon l'état du livre
        holder.txvFavorite.setOnClickListener {
            book.favoris = !book.favoris  // Inverser l'état de favoris
            updateFavoriteState(holder, book, position) // Mettre à jour l'image de l'étoile

            // Mettre à jour l'état du livre sur le serveur
            //updateFavoriteOnServer(book, holder)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }

    private fun updateFavoriteState(holder: BookViewHolder, book: Book, position: Int) {
        if (book.favoris) {
            holder.txvFavorite.setImageResource(R.drawable.ic_star_filled) // étoile pleine
        } else {
            holder.txvFavorite.setImageResource(R.drawable.ic_star_empty) // étoile vide
        }
    }

    private fun updateEmplacementImage(holder: BookViewHolder, book: Book) {
        // Mettre à jour l'image selon l'emplacement
        if (book.emplacement == "CC QUATRE TEMPS") {
            holder.emplacementImage.setImageResource(R.drawable.logo_4temps)
        } else if (book.emplacement == "INTERIEUR GRANDE ARCHE" || book.emplacement == "GRANDE ARCHE") {
            holder.emplacementImage.setImageResource(R.drawable.logo_grande_arche)
        } else if (book.emplacement == "GARE") {
            holder.emplacementImage.setImageResource(R.drawable.logo_gare)
        } else if (book.emplacement == "ESPACE PUBLIC" ||
            book.emplacement == "ESPACE PUBLIC GRANDE ARCHE" ||
            book.emplacement == "ESPACE PUBLIC(CC 4 TEMPS)") {
            holder.emplacementImage.setImageResource(R.drawable.logo_espace_public)
        } else if (book.emplacement == "HORS ESPACE PUBLIC") {
            holder.emplacementImage.setImageResource(R.drawable.logo_espace_hors_public)
        } else if (book.emplacement == "PARKING") {
            holder.emplacementImage.setImageResource(R.drawable.logo_parking)
        } else if (book.emplacement == "VOLUMES") {
            holder.emplacementImage.setImageResource(R.drawable.logo_espace_public)
        } else {
            holder.emplacementImage.setImageResource(R.drawable.logo_espace_public) // Image par défaut
        }

        //holder.emplacementImage.setImageResource(R.drawable.logo_espace_public)
    }

    private fun updateFavoriteOnServer(book: Book, holder: BookViewHolder) {
        val call = bookService.setFavoris(book.isbn, book.favoris)  // Utilise l'ID et l'objet book modifié
        call.enqueue(object : Callback<Book> {
            override fun onResponse(call: Call<Book>, response: Response<Book>) {
                if (response.isSuccessful) {
                    Toast.makeText(holder.itemView.context, "Favoris mis à jour", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Erreur de mise à jour", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Book>, t: Throwable) {
                Toast.makeText(holder.itemView.context, "Échec de la mise à jour", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateBooks(allBooks: List<Book>) {
        books = allBooks
        notifyDataSetChanged()
    }
}

