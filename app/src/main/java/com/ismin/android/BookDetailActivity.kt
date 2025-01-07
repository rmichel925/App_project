package com.ismin.android

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)

        // Récupérer l'objet Book passé en intent
        val book = intent.getSerializableExtra("book") as? Book
        if (book == null) {
            // Gérer le cas où le livre est null pour éviter le crash
            finish() // Quitter l'activité si aucune donnée n'est reçue
            return
        }

        // Récupérer le bouton et gérer le clic
        val backButton: Button = findViewById(R.id.bd_back_button)
        backButton.setOnClickListener {
            if (!isFinishing) {
                finish()
            }
        }


        // Récupérer les TextView du layout
        val titleTextView: TextView = findViewById(R.id.bd_title)
        val authorTextView: TextView = findViewById(R.id.bd_author)
        val isbnTextView: TextView = findViewById(R.id.bd_isbn)
        val dateTextView: TextView = findViewById(R.id.bd_date)
        val emplacementTextView: TextView = findViewById(R.id.bd_emplacement)
        val favorisTextView: TextView = findViewById(R.id.bd_favoris)

        // Afficher les informations avec sécurité (valeurs par défaut si manquantes)
        titleTextView.text = book.title.takeIf { it.isNotEmpty() } ?: "Titre non disponible"
        authorTextView.text = book.author.takeIf { it.isNotEmpty() } ?: "Auteur non disponible"
        isbnTextView.text = book.isbn.takeIf { it.isNotEmpty() } ?: "ISBN non disponible"
        dateTextView.text = book.date.takeIf { it.isNotEmpty() } ?: "Date non disponible"
        emplacementTextView.text = book.emplacement.takeIf { it.isNotEmpty() } ?: "Emplacement non disponible"
        favorisTextView.text = if (book.favoris) "Favoris: Oui" else "Favoris: Non"
    }

}

