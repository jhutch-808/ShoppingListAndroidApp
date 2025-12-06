package hu.bme.ait.wanderer.network

import retrofit2.http.Query
import retrofit2.http.GET

interface PlacesApiService {
    @GET("api/place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): PlacesSearchResponse // You'll need to define this data class
}

