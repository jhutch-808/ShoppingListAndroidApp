package hu.bme.ait.wanderer.ui.screens.mainScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.wanderer.ui.theme.Pink100
import hu.bme.ait.wanderer.ui.theme.Pink80
import hu.bme.ait.wanderer.ui.theme.Purple40
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAddRestarauntClicked: () -> Unit,
    onListRestarauntsClicked: () -> Unit,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {

//    // --- ViewModel States ---
//    val searchQuery by mainScreenViewModel.searchQuery.collectAsState()
//    val searchResults by mainScreenViewModel.searchResults.collectAsState()
//    val selectedPlace by mainScreenViewModel.selectedPlace.collectAsState()

//    // --- Hardcoded data (replace with DAO data) ---
//    val savedLocations = listOf(
//        LatLng(47.497913, 19.040236),
//        LatLng(40.7128, -74.0060)
//    )
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(savedLocations.first(), 10f)
//    }
//
//    // --- Side Effects ---
//    // This will run when a place is selected from the search results
//    LaunchedEffect(selectedPlace) {
//        selectedPlace?.location?.let { latLng ->
//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
//            )
//        }
//    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("WhereToGo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            MainBottomAppBar(
                onAddRestarauntClicked = onAddRestarauntClicked,
                onListRestarauntsClicked = onListRestarauntsClicked,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

        }
    }
}

@Composable
fun MainBottomAppBar(
    onAddRestarauntClicked: () -> Unit,
    onListRestarauntsClicked: () -> Unit,
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = onListRestarauntsClicked) {
                Icon(
                    Icons.AutoMirrored.Filled.List, // Corrected icon
                    contentDescription = "List Saved Places"
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddRestarauntClicked) {
                Icon(Icons.Default.Add, contentDescription = "Add Place")
            }
        }
    )
}
