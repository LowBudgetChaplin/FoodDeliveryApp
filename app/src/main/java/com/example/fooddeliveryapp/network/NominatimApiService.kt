package com.example.fooddeliveryapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApiService {
    @GET("search")
    suspend fun searchPlace(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 1
    ): List<NominatimSearchResult>
}