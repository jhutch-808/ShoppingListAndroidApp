package hu.bme.ait.wanderer.ui.screens.addRestarauntScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Import this
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.wanderer.data.Label // 1. Import your enums
import hu.bme.ait.wanderer.data.LocationTypeCategory
import hu.bme.ait.wanderer.ui.screens.commons.CommonBottomAppBar // 2. Import the common bottom bar
import java.net.URLDecoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    name: String,
    priceLevel: String,
    address: String,
    onBackClicked: () -> Unit,
    onListRestaurantsClicked: () -> Unit,
    viewModel: AddlocationViewModel = hiltViewModel()
) {
    // 3. Decode URL-encoded parameters and initialize ViewModel state
    LaunchedEffect(Unit) {
        viewModel.initializeState(name, priceLevel, address )
    }

    val uiState = viewModel.uiState
    var showLabelDialog by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }

    // This will open the label selection dialog
    if (showLabelDialog) {
        LabelSelectionDialog(
            // 4. Use the Label enum from your data layer
            allLabels = Label.values().toList(),
            selectedLabels = uiState.selectedLabels,
            onLabelToggled = viewModel::onLabelToggled,
            onDismissRequest = { showLabelDialog = false }
        )
    }

    //will open the notes text box dialog
    if (showNotesDialog) {
        NotesInputDialog(
            initialNotes = uiState.notes,
            onSave = {
                viewModel.onNotesChanged(it)
                showNotesDialog = false
            },
            onDismissRequest = { showNotesDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Your Review") },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            AddLocationBottomBar(
                onSaveClicked = {
                    viewModel.saveLocationToDataBase()
                    onBackClicked()
                },
                onListRestaurantsClicked = onListRestaurantsClicked
            )

        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //Restaurant Name Header
            item {
                Text(
                    // 6. Use the name from the ViewModel state
                    text = uiState.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // select category
            item {
                CategorySelector(
                    // 7. Use the LocationTypeCategory enum
                    categories = LocationTypeCategory.values().toList(),
                    selectedCategory = uiState.locationTypeCategory.name,
                    onCategorySelected = viewModel::onLocationTypeSelected
                )
            }

            // choose the sentiment: not good, meh, bad
            item {
                SentimentSelector(
                    selectedSentiment = uiState.selectedSentiment,
                    onSentimentSelected = viewModel::onSentimentSelected
                )
            }

            // info rows
            item {
                //convert the Set<Label> -> displayable string
                val labelsText = uiState.selectedLabels.joinToString(", ") { it.displayText }.ifEmpty { "None" }
                InfoRow(label = "Labels", value = labelsText) {
                    showLabelDialog = true
                }
            }
            item {
                InfoRow(label = "Notes", value = uiState.notes.ifEmpty { "Add notes..." }) {
                    showNotesDialog = true
                }
            }

        }
    }
}

