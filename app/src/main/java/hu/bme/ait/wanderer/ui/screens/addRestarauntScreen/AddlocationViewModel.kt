package hu.bme.ait.wanderer.ui.screens.addRestarauntScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.wanderer.data.AppDataBase
import hu.bme.ait.wanderer.data.Label
import hu.bme.ait.wanderer.data.LocationDAO
import hu.bme.ait.wanderer.data.LocationInfoItem
import hu.bme.ait.wanderer.data.LocationLabelCrossRef
import hu.bme.ait.wanderer.data.LocationTypeCategory
import hu.bme.ait.wanderer.data.PriceRangeCategory
import hu.bme.ait.wanderer.network.InfoscreenResult
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

enum class Sentiment {
    LIKED, MEH, DISLIKED
}

data class AddLocationUiState(
    // Data passed from the previous screen
    val name: String = "",
    val address: String = "",
    val priceRange: PriceRangeCategory = PriceRangeCategory.UNKNOWN,
    val selectedLabels: Set<Label> = emptySet(),
    val locationTypeCategory: LocationTypeCategory= LocationTypeCategory.RESTAURANT,
    val selectedSentiment: Sentiment? = null,
    val visitDate: String = "", // Could be a Date object later
    val notes: String = "",
    val ranking: Int = 0
)


@HiltViewModel
class AddlocationViewModel @Inject constructor(
    private val locationDAO: LocationDAO
) : ViewModel() {
    var uiState by mutableStateOf(AddLocationUiState())
        private set

    fun initializeState(name: String, priceLevelString: String, address: String) {
        val priceCategory = when (priceLevelString) {
            "PRICE_LEVEL_INEXPENSIVE" -> PriceRangeCategory.CHEAP
            "PRICE_LEVEL_MODERATE" -> PriceRangeCategory.MEDIUM
            "PRICE_LEVEL_EXPENSIVE" -> PriceRangeCategory.EXPENSIVE
            "PRICE_LEVEL_VERY_EXPENSIVE" -> PriceRangeCategory.SUPEREXPENSIVE
            else -> PriceRangeCategory.UNKNOWN
        }
        uiState = uiState.copy(name = name, priceRange = priceCategory, address = address)
    }

    fun onLabelToggled(label: Label) {
        val currentLabels = uiState.selectedLabels.toMutableSet()
        if (currentLabels.contains(label)) {
            currentLabels.remove(label)
        } else {
            currentLabels.add(label)
        }
        uiState = uiState.copy(selectedLabels = currentLabels)

    }

    fun onLocationTypeSelected(type: String) {
        uiState = uiState.copy(locationTypeCategory = LocationTypeCategory.valueOf(type))
    }

    fun onSentimentSelected(sentiment: Sentiment) {
        uiState = uiState.copy(selectedSentiment = sentiment)
    }

    fun onNotesChanged(notes: String) {
        uiState = uiState.copy(notes = notes)
    }

    fun saveLocationToDataBase() {
        viewModelScope.launch {
            val newLocation = LocationInfoItem(
                name = uiState.name,
                address = uiState.address,
                priceRange = uiState.priceRange,
                visitDate = Date(),
                locationType = uiState.locationTypeCategory.name?: "other",
                ranking = when (uiState.selectedSentiment) {
                    Sentiment.LIKED -> 5
                    Sentiment.MEH -> 3
                    Sentiment.DISLIKED -> 1
                    null -> 0
                },
                notes = uiState.notes
            )
            val newLocationID = locationDAO.insert(newLocation)

            val labelCrossRef = uiState.selectedLabels.map { label ->
                LocationLabelCrossRef(newLocationID, label)

            }
            if (labelCrossRef.isNotEmpty()) {
                locationDAO.insertLabelCrossRefs(labelCrossRef)
            }
        }


    }
}