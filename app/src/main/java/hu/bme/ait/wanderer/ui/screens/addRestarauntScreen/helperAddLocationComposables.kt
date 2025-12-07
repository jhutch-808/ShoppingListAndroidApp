package hu.bme.ait.wanderer.ui.screens.addRestarauntScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import hu.bme.ait.wanderer.data.Label
import hu.bme.ait.wanderer.data.LocationTypeCategory

@Composable
fun SentimentSelector(
    selectedSentiment: Sentiment?,
    onSentimentSelected: (Sentiment) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SentimentCircle(Color.Green, isSelected = selectedSentiment == Sentiment.LIKED) { onSentimentSelected(Sentiment.LIKED) }
        SentimentCircle(Color.Yellow, isSelected = selectedSentiment == Sentiment.MEH) { onSentimentSelected(Sentiment.MEH) }
        SentimentCircle(Color.Red, isSelected = selectedSentiment == Sentiment.DISLIKED) { onSentimentSelected(Sentiment.DISLIKED) }
    }
}

@Composable
fun SentimentCircle(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick)
            .then(
                if (isSelected) Modifier.border(
                    4.dp,
                    MaterialTheme.colorScheme.primary,
                    CircleShape
                ) else Modifier
            )
    )
}

@Composable
fun InfoRow(label: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


@Composable
fun CategorySelector(
    categories: List<LocationTypeCategory>, // Use enum
    selectedCategory: String?,
    onCategorySelected: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text("Category", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        // Consider a LazyRow or FlowRow for a long list of categories
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.take(4).forEach { category ->
                FilterChip(
                    selected = category.name == selectedCategory,
                    onClick = { onCategorySelected(category.name) },
                    label = { Text(category.name.lowercase().replaceFirstChar { it.uppercase() }) } // e.g., "RESTAURANT" -> "Restaurant"
                )
            }
        }
    }
}

@Composable
fun LabelSelectionDialog(
    allLabels: List<Label>, // Use enum
    selectedLabels: Set<Label>,
    onLabelToggled: (Label) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Labels", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) { // Prevent dialog from being too tall
                    items(allLabels) { label ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLabelToggled(label) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = selectedLabels.contains(label),
                                onCheckedChange = { onLabelToggled(label) }
                            )
                            // Use the user-friendly text from your enum
                            Text(label.displayText, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(onClick = onDismissRequest, modifier = Modifier.align(Alignment.End)) {
                    Text("Done")
                }
            }
        }
    }
}


@Composable
fun NotesInputDialog(
    initialNotes: String,
    onSave: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var text by remember { mutableStateOf(initialNotes) }
    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(Modifier.padding(16.dp)) {
                Text("Add Notes", style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(Modifier.height(16.dp))
                Button(onClick = { onSave(text) }, modifier = Modifier.align(Alignment.End)) {
                    Text("Save")
                }
            }
        }
    }
}
// Add this new Composable at the bottom of your AddLocationScreen.kt file

@Composable
fun AddLocationBottomBar(
    onSaveClicked: () -> Unit,
    onListRestaurantsClicked: () -> Unit
) {
    BottomAppBar(
        actions = {
            // This is the button to navigate to the list screen
            IconButton(onClick = onListRestaurantsClicked) {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = "List Saved Restaurants"
                )
            }
        },
        // The FloatingActionButton is the primary action, which is to Save.
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveClicked,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Save Location")
                    Spacer(Modifier.width(8.dp))
                    Text("Save")
                }
            }
        }
    )
}



