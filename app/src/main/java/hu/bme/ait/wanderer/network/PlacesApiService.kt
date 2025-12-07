package hu.bme.ait.wanderer.network

import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface PlacesApiService {
    @GET("api/place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): PlacesSearchResponse // define data class in

    @GET("https://places.googleapis.com/v1/places/{placeId}?")
    suspend fun getLocationInfo(
        @Path("placeId") placeId: String,
        @Query("key") apiKey: String,
        @Query("fields") fields: String = "displayName,priceLevel,rating,formattedAddress,userRatingCount,primaryType,currentOpeningHours"
    ): InfoscreenResult // You'll need to define this data class
}

