package com.example.fooddeliveryapp.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OsrmApiService {
    @GET("route/v1/driving/{coords}")
    suspend fun getRoute(
        @Path("coords") coords: String,
        @Query("overview") overview: String = "false"
    ): OsrmRouteResponse
}