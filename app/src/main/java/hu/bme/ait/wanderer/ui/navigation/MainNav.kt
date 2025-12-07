package hu.bme.ait.wanderer.ui.navigation

import androidx.navigation3.runtime.NavKey
import hu.bme.ait.wanderer.network.PlaceResult
import kotlinx.serialization.Serializable

@Serializable
data object MainScreenRoute: NavKey

@Serializable
data object WelcomeScreenRoute : NavKey

@Serializable
data class AddRestarauntScreenRoute(val name: String,
                                     val priceLevel: String,
                                     val address: String): NavKey

@Serializable
data object ListPlacesScreenRoute: NavKey

@Serializable
data class LocationInfoScreenRoute(val placeId: String): NavKey