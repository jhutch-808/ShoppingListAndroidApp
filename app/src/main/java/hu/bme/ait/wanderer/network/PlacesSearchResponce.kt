package hu.bme.ait.wanderer.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// This is the top-level response object from the API.
@Serializable
data class PlacesSearchResponse(
    // The list of places found by the search.
    val results: List<PlaceResult>,
    // The status of the request (e.g., "OK", "ZERO_RESULTS").
    val status: String
)

// This represents a single place found in the search results.
@Serializable
data class PlaceResult(
    // The full, human-readable address of this place.
    @SerialName("formatted_address")
    val formattedAddress: String? = null,

    @SerialName("place_id")
    val placeId: String? = null,

    // The geometry of the place, which includes the location (latitude and longitude).
    val geometry: Geometry? = null,

    // The place's name.
    val name: String? = null,

    // The price level of the place, on a scale of 0 to 4.
    @SerialName("price_level")
    val priceLevel: Int? = null,

    // The place's rating, from 1.0 to 5.0.
    val rating: Double? = null,

    // A list of strings indicating the type of this place (e.g., "restaurant", "cafe").
    val types: List<String>? = null,

    // The total number of user ratings that contributed to the place's rating.
    @SerialName("user_ratings_total")
    val userRatingsTotal: Int? = null
)

// This holds the location data for a place.
@Serializable
data class Geometry(
    val location: Location
)

// This represents the geographic coordinates.
@Serializable
data class Location(
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lng")
    val longitude: Double
)

@Serializable
data class InfoscreenResult(
    @SerialName("displayName")
    val displayName: DisplayName? = null,
    @SerialName("priceLevel")
    val priceLevel: String? = null,
    @SerialName("rating")
    val rating: Double? = null,
    @SerialName("userRatingCount")
    val userRatingsTotal: Int? = null,
    @SerialName("formattedAddress")
    val formattedAddress: String? = null,
    @SerialName("primaryType")
    val primaryType: String? = null,
    @SerialName("currentOpeningHours")
    val currOpeningHours: CurrentOpeningHours? = null
   )
@Serializable
data class CurrentOpeningHours(
    @SerialName("openNow")
    val openNow: Boolean? = null,
    @SerialName("weekdayDescriptions")
    val weekdayDescriptions: List<String>? = null
)



@Serializable
data class DisplayName(
    @SerialName("text")
    val text: String? = null,
    @SerialName("languageCode")
    val languageCode: String? = null
)
