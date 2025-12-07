package hu.bme.ait.wanderer.ui.screens.listRestarauntScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.wanderer.data.LocationDAO
import hu.bme.ait.wanderer.data.LocationTypeCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LocationInfoViewModel @Inject constructor(
    locationDAO: LocationDAO
) : ViewModel() {
    private val _selectedFilter = MutableStateFlow<LocationTypeCategory?>(null)
    val selectedFilter = _selectedFilter

    private val allLocations = locationDAO.getLocationsWithLabelsSortedByRank()

    val locationsUiState = combine(
        selectedFilter,
        allLocations
    ) { selectedFilter, allLocations ->
        if (selectedFilter == null) {
            allLocations
        } else {
            allLocations.filter { locationWithLabels ->
                locationWithLabels.location.locationType == selectedFilter
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun onFilterChange(category: LocationTypeCategory){
        if(_selectedFilter.value == category){
            _selectedFilter.value = null
        }else{
            _selectedFilter.value = category
        }

    }


}



