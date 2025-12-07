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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
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
import hu.bme.ait.wanderer.network.PlaceResult
import hu.bme.ait.wanderer.ui.theme.ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInfoScreen(
    placeId: String,
    onBackToMainClicked: () -> Unit,
    onNavigateToAddLocationClicked: () -> Unit,
    viewModel: LocationInfoViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = placeId) {
        viewModel.getLocationInfo(placeId)

    }
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

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Spacer(Modifier.height(16.dp))

            //Bottom Bar

        }


    }


}

@Composable
fun InfoResultWidget(placeResult: PlaceResult) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(color = ForestGreen).padding(10.dp)
    ) {
        placeResult.name?.let {
            Text(
                text = "Name: $it",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(16.dp))
        }
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
                    1 -> "$ Inexpensive"
                    2 -> "$$ Moderate"
                    3 -> "$$$ Expensive"
                    4 -> "$$$$ Very Expensive"
                    else -> "Not Available"
                }
            } ?: "Not Available"
        )
        InfoRow(
            icon = Icons.Default.Restaurant,
            label = "Type",
            text = placeResult.types?.joinToString(", ") ?: "Not Available"
        )
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
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

}

