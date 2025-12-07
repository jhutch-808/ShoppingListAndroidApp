package hu.bme.ait.wanderer.network

import retrofit2.http.Query
import retrofit2.http.GET

interface PlacesApiService {
    @GET("api/place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): PlacesSearchResponse // You'll need to define this data class

    @GET("api/place/details/json")
    suspend fun getLocationInfo(
        @Query("place_id") placeId: String,
        @Query("key") apiKey: String,
        @Query("fields") fields: String = "name,formatted_address,rating,user_ratings_total,price_level,types,geometry"
    ): PlaceResult // You'll need to define this data class
}

