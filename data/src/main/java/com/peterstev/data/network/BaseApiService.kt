package com.peterstev.data.network

import com.peterstev.data.models.JsonMovieResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseApiService {

    @GET("3/search/movie")
    fun searchMovie(
        @Query("api_key") apiKey: String,
        @Query("query") text: String,
        @Query("page") page: Int,
    ): Observable<JsonMovieResult>
}
