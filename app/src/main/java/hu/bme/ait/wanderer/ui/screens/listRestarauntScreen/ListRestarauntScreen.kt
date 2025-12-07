package hu.bme.ait.wanderer.ui.screens.listRestarauntScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.wanderer.data.LocationTypeCategory
import hu.bme.ait.wanderer.data.LocationWithLabels
import hu.bme.ait.wanderer.ui.screens.commons.CommonBottomAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLocationsScreen(
    onBackClicked: () -> Unit,
    viewModel: LocationInfoViewModel = hiltViewModel()
) {
    val locations by viewModel.locationsUiState.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Locations") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // filter bar
            FilterChipBar(
                selectedFilter = selectedFilter,
                onFilterChange = viewModel::onFilterChange
            )

            // list of locations
            if (locations.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No saved locations yet.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(locations, key = { it.location.locationId }) { locationWithLabels ->
                        LocationCard(locationWithLabels = locationWithLabels)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipBar(
    selectedFilter: LocationTypeCategory?,
    onFilterChange: (LocationTypeCategory) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(LocationTypeCategory.values()) { category ->
            FilterChip(
                selected = (selectedFilter == category),
                onClick = { onFilterChange(category) },
                label = { Text(category.displayName) }
            )
        }
    }
}
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationCard(locationWithLabels: LocationWithLabels) {
    val location = locationWithLabels.location
    val labels = locationWithLabels.labels

    var isExpanded by remember { mutableStateOf(false) }

    // Use Box to allow layering the button over the content
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Main content column
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    // Add padding at the bottom to make space for the button if there are many labels
                    .padding(bottom = if (labels.size > 2) 24.dp else 0.dp)
            ) {
                // --- This part remains the same ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = location.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        // Make room for the rating stars on small screens
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Row {
                        (1..location.ranking).forEach { _ ->
                            Icon(Icons.Default.Star, contentDescription = "Rank", tint = MaterialTheme.colorScheme.primary)
                        }
                        (location.ranking + 1..5).forEach { _ ->
                            Icon(Icons.Default.StarBorder, contentDescription = "Empty Rank", tint = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
                Text(location.address, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                if (location.notes.isNotBlank()) {
                    Text(location.notes, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                }

                // --- MODIFIED LABEL DISPLAY ---
                if (labels.isNotEmpty()) {
                    // Use FlowRow for a responsive chip layout
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        // Show the first 2 labels
                        labels.take(2).forEach { label ->
                            SuggestionChip(onClick = {}, label = { Text(label.displayText) })
                        }
                    }

                    // The "dropdown" part for the rest of the labels
                    AnimatedVisibility(visible = isExpanded) {
                        FlowRow(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Display the rest of the labels
                            labels.drop(2).forEach { label ->
                                SuggestionChip(onClick = {}, label = { Text(label.displayText) })
                            }
                        }
                    }
                }
            }
        }

        // --- NEW EXPAND/COLLAPSE BUTTON ---
        // This button is only shown if there are more than 2 labels
        if (labels.size > 2) {
            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Aligns to the bottom-right corner of the Box
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse labels" else "Expand labels",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
