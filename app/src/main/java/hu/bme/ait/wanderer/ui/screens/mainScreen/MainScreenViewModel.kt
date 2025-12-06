package hu.bme.ait.wanderer.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.wanderer.data.LocationInfoItem
import hu.bme.ait.wanderer.data.LocationDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(val LocationDAO: LocationDAO): ViewModel() {




}