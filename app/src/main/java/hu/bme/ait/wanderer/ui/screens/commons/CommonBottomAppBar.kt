package hu.bme.ait.wanderer.ui.screens.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun CommonBottomAppBar(
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