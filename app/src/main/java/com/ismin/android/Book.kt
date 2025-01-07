package com.ismin.android

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Book(
    @SerializedName("isbn") val isbn: String = "",  // Mappage de "objectid" à "isbn"
    @SerializedName("title") val title: String = "Titre inconnu",  // Mappage de "nom_de_l_oeuvre"
    @SerializedName("author") val author: String = "Auteur inconnu",  // Mappage de "nom_de_l_artiste"
    @SerializedName("date") val date: String = "Date inconnue",  // Mappage de "date_de_creation"
    @SerializedName("coordonnees") val coordonnees: Coordonnees = Coordonnees(),  // Mappage de "coord" à "coordonnees"
    @SerializedName("emplacement") val emplacement: String = "Emplacement inconnu",  // Mappage de "emplacement"
    @SerializedName("favoris") var favoris: Boolean
) : Serializable

