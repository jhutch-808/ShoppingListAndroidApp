package hu.bme.ait.wanderer.ui.screens.locationInfoScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.wanderer.R
import hu.bme.ait.wanderer.network.InfoscreenResult
import hu.bme.ait.wanderer.network.PlaceResult
import hu.bme.ait.wanderer.ui.screens.commons.CommonBottomAppBar
import hu.bme.ait.wanderer.ui.theme.DarkGreen
import hu.bme.ait.wanderer.ui.theme.ForestGreen
import hu.bme.ait.wanderer.ui.theme.MintGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInfoScreen(
    placeId: String,
    onBackToMainClicked: () -> Unit,
    onAddRestarauntClicked: (name: String, pricelevel: String, address: String)-> Unit,
    onListRestarauntsClicked: ()-> Unit,
    viewModel: LocationInfoViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = placeId) {
        viewModel.getLocationInfo(placeId)

    }
    val uiState = viewModel.locationUIState

    Log.d("NAV_DEBUG", "LocationInfoScreen composed successfully with place: ${placeId}")
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = onBackToMainClicked) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White
                )
            )

        },
        bottomBar = {
            CommonBottomAppBar(
                onAddRestarauntClicked = {
                    if(uiState is LocationUIState.Success){
                        Log.d("NAV_DEBUG_LOCATIONINFO", "Navigating to AddRestarauntScreen with place: ${(uiState as LocationUIState.Success).location.displayName?.text}")
                        val placeResult = (uiState as LocationUIState.Success).location
                        val name = placeResult.displayName?.text ?: "Unknown"
                        val priceLevel = placeResult.priceLevel ?: "Unknown"
                        onAddRestarauntClicked(name,priceLevel,placeResult.formattedAddress ?: "Unknown")
                    }
                },
                onListRestarauntsClicked = onListRestarauntsClicked,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        when (viewModel.locationUIState) {
                            LocationUIState.Error -> {
                                Text("Error fetching location info")
                            }

                            LocationUIState.Init -> {
                                Text("Initializing...")
                            }

                            LocationUIState.Loading -> {
                                CircularProgressIndicator()
                            }

                            is LocationUIState.Success -> InfoResultWidget(
                                (viewModel.locationUIState as LocationUIState.Success).location
                            )
                        }
                    }

                }
            }

        }

    }

}

@Composable
fun InfoResultWidget(placeResult: InfoscreenResult) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkGreen) // Use the same color as your top bar
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the place's name
            placeResult.displayName?.text?.let {
                Text(
                    text = it,
                    // Make the name text white to stand out on the dark background
                    color = Color.White,
                    // Use a slightly smaller, but still prominent style
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            // Display the open/closed status
            placeResult.currOpeningHours?.openNow?.let { isOpen ->
                val status = if (isOpen) "Open" else "Closed"
                // Use a light green for "Open" and a light red for "Closed" for better contrast
                val statusColor = if (isOpen) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodyLarge,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(10.dp)) {

            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Address",
                text = placeResult.formattedAddress ?: "Not Available"
            )
            InfoRow(
                icon = Icons.Default.Star,
                label = "Rating",
                text = placeResult.rating?.let { "${it} / 5.0 (${placeResult.userRatingsTotal}) reviews" }
                    ?: "No Rating"
            )
            InfoRow(
                icon = Icons.Default.AttachMoney,
                label = "Price Level",
                text = placeResult.priceLevel?.let { level ->
                    when (level) {
                        "PRICE_LEVEL_INEXPENSIVE" -> "$ Inexpensive"
                        "PRICE_LEVEL_MODERATE" -> "$$ Moderate"
                        "PRICE_LEVEL_EXPENSIVE" -> "$$$ Expensive"
                        "PRICE_LEVEL_VERY_EXPENSIVE" -> "$$$$ Very Expensive"
                        else -> "Not Available"
                    }
                } ?: "Not Available"
            )
            InfoRow(
                icon = Icons.Default.Restaurant,
                label = "Type",
                text = placeResult.primaryType ?: "Not Available"
            )
            InfoRow(
                icon = Icons.Default.Schedule,
                label = "Opening Hours",
                text = placeResult.currOpeningHours?.weekdayDescriptions?.joinToString("\n")
                    ?: "Not Available"
            )
        }
    }

}


@Composable
fun InfoRow(icon: ImageVector, text: String, label: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)

    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            val textStyle = if (label == "Opening Hours") {
                MaterialTheme.typography.labelMedium // Use a smaller style for the schedule
            } else {
                MaterialTheme.typography.bodyLarge // Keep the original style for others
            }

            Text(
                text = text,
                style = textStyle,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

