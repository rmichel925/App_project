package com.ismin.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class InfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate le layout du fragment
        val rootView = inflater.inflate(R.layout.fragment_info, container, false)

        // Récupération des TextView dans le layout
        val urlTextView: TextView = rootView.findViewById(R.id.f_info_url)
        val explanationsTextView: TextView = rootView.findViewById(R.id.f_info_explanations)
        val librariesTextView: TextView = rootView.findViewById(R.id.f_info_libraries)

        // Affichage des informations
        urlTextView.text = "URL des données utilisées : https://www.data.gouv.fr/fr/datasets/oeuvres-dart-1/#/resources"
        explanationsTextView.text = "Explications des données affichées : Ce sont les oeuvres récupérées depuis l'API."
        librariesTextView.text = "Librairies et Licences : Retrofit, Gson, AndroidX (Licence Apache 2.0)"

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(books: ArrayList<Book>) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOKS, books)
                }
            }
    }
}
