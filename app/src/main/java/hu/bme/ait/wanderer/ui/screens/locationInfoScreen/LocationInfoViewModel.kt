package hu.bme.ait.wanderer.ui.screens.locationInfoScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.wanderer.data.LocationInfoItem
import hu.bme.ait.wanderer.network.PlaceResult
import hu.bme.ait.wanderer.network.PlacesApiService
import hu.bme.ait.wanderer.network.PlacesSearchResponse
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LocationUIState {
    object Init : LocationUIState
    data class Success(val location: PlaceResult) : LocationUIState
    object Loading : LocationUIState
    object Error : LocationUIState
}

@HiltViewModel
class LocationInfoViewModel @Inject constructor(val placesApiService: PlacesApiService): ViewModel() {
    var locationUIState: LocationUIState by mutableStateOf(LocationUIState.Init)

    fun getLocationInfo(placeId: String) {
        locationUIState = LocationUIState.Loading
        Log.d("LocationInfoViewModel", "getLocationInfo called with placeId: $placeId")

        viewModelScope.launch{
            try{
                val response = placesApiService.getLocationInfo(
                    placeId,
                    "AIzaSyCVYkRKxbOeusGub0IKhCRbyH79VyBS95k"
                )
                Log.d("LocationInfoViewModel", "API Status: ${response}")
                Log.d("LocationInfoViewModel", "Results")
                locationUIState = LocationUIState.Success(response)
            } catch (e: Exception){
                locationUIState = LocationUIState.Error
                Log.d("LocationInfoViewModel", "API call failed: ${e.message}")

            }

        }
    }


}