package com.example.fooddeliveryapp.network

import com.google.gson.annotations.SerializedName

data class NominatimSearchResult(
    @SerializedName("lat") val lat: String,
    @SerializedName("lon") val lon: String,
    @SerializedName("display_name") val displayName: String
)