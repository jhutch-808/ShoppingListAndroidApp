package hu.bme.ait.wanderer.ui.screens.mainScreen

import android.util.Log
import android.webkit.ConsoleMessage
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.wanderer.data.LocationInfoItem
import hu.bme.ait.wanderer.data.LocationDAO
import hu.bme.ait.wanderer.network.PlaceResult
import hu.bme.ait.wanderer.network.PlacesApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val placesApiService: PlacesApiService): ViewModel() {
    // A private mutable state flow to hold the search results
    private val _searchResults = MutableStateFlow<List<PlaceResult>>(emptyList())
    // A public immutable state flow for the UI to observe
    val searchResults: StateFlow<List<PlaceResult>> = _searchResults

    fun searchPlaces(query: String) {
        // Don't search if the query is blank
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                // IMPORTANT: You need to add your API key to your project's local.properties
                // and access it via BuildConfig.
                val response = placesApiService.searchPlaces(
                    query = query,
                    apiKey = "AIzaSyCVYkRKxbOeusGub0IKhCRbyH79VyBS95k"
                )
                Log.d("MainScreenVM", "API Status: ${response.status}")
                Log.d("MainScreenVM", "Results Count: ${response.results.size}")

                if (response.status == "OK") {
                    _searchResults.value = response.results
                } else {
                    // You can add more robust error handling here
                    Log.w("MainScreenVM", "API returned status: ${response.status}")
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                // Handle network errors, etc.
                _searchResults.value = emptyList()
                // For debugging:
                e.printStackTrace()
                Log.e("MainScreenVM", "API call failed: ${e.message}")
                Log.e("MainScreenVM", "Cause: ${e.cause}")

            }
        }
    }


}