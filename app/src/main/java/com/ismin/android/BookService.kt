package com.ismin.android

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BookService {
    @GET("/books")
    fun getAllBooks(): Call<List<Book>>

    //@POST("/books")
    //fun createBook(@Body book: Book): Call<Book>

    @PUT("/books")
    fun setFavoris(
        @Path("isbn") isbn: String,
        @Body favoris: Boolean
    ): Call<Book>
}