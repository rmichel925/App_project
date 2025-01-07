package com.ismin.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

private const val BOOKS_KEY = "books"

class MapFragment : Fragment(), OnMapReadyCallback {

    private var books: ArrayList<Book>? = null
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            books = it.getSerializable(BOOKS_KEY) as ArrayList<Book>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.f_map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        books?.forEach { book ->
            val position = LatLng(book.coordonnees.x, book.coordonnees.y)
            googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(book.title)
                    .snippet("Auteur : ${book.author}")
            )
        }

        // Centrer la caméra sur le premier livre, si disponible
        books?.firstOrNull()?.let {
            val firstBookPosition = LatLng(it.coordonnees.x, it.coordonnees.y)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstBookPosition, 10f))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(books: ArrayList<Book>) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BOOKS_KEY, books)
                }
            }
    }
}
