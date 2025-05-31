package com.example.fooddeliveryapp.network

import com.google.gson.annotations.SerializedName

data class OsrmRouteResponse(
    @SerializedName("routes") val routes: List<Route>
)

data class Route(
    @SerializedName("legs") val legs: List<Leg>
)

data class Leg(
    @SerializedName("distance") val distance: Double,
    @SerializedName("duration") val duration: Double
)