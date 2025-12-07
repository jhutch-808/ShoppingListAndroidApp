package hu.bme.ait.wanderer.ui.screens.mainScreen

import android.util.Log
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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.R
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.wanderer.network.PlaceResult
import hu.bme.ait.wanderer.ui.screens.commons.CommonBottomAppBar
import hu.bme.ait.wanderer.ui.theme.ForestGreen
import hu.bme.ait.wanderer.ui.theme.Pink100
import hu.bme.ait.wanderer.ui.theme.Pink80
import hu.bme.ait.wanderer.ui.theme.Purple40
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onAddRestarauntClicked: (name: String, pricelevel: String, address: String) -> Unit,
    onListRestarauntsClicked: () -> Unit,
    onLocationInfoClicked:(String) -> Unit,
    mainScreenViewModel: MainScreenViewModel = hiltViewModel()
) {
    //State for search bar
    var query by rememberSaveable { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchResults by mainScreenViewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wanderer") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White
                ),
                actions = {

                }
            )
        },
        bottomBar = {
            CommonBottomAppBar(
                onAddRestarauntClicked = { onAddRestarauntClicked("","","") },
                onListRestarauntsClicked = onListRestarauntsClicked,
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            //Search bar
            DockedSearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = if (!active) 16.dp else 0.dp),
                query = query,
                onQueryChange = {
                    query = it
                    mainScreenViewModel.searchPlaces(it)
                                },
                onSearch = {
                    // This is where you would trigger the search
                    mainScreenViewModel.searchPlaces(it)
                    active = false
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Search for a place") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            ) {
                LazyColumn {
                    items(searchResults) { placeResult ->
                        ListItem(
                            headlineContent = { Text(placeResult.name ?: "No name") },
                            supportingContent = { Text(placeResult.formattedAddress ?: "") },
                            leadingContent = {
                                // Display price level if available
                                placeResult.priceLevel?.let {
                                    Text(
                                        text = "$".repeat(it),
                                        color = Color.Gray,
                                        fontSize = 18.sp
                                    )
                                }
                            },
                            trailingContent = {
                                // Display rating if available
                                placeResult.rating?.let {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            painter = painterResource(id = android.R.drawable.star_on),
                                            contentDescription = "Rating",
                                            tint = Color(0xFFFFC107) // Amber color for star
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(it.toString())
                                    }
                                }
                            },
                            modifier = Modifier.clickable {
                                // When a user clicks a result:
                                // 1. Update the search bar text
                                query = placeResult.name ?: ""
                                // 2. Close the active search view
                                active = false
                                Log.d("NAV_DEBUG", "Navigating to LocationInfo with place: ${placeResult.name}")
                                // 3. (Future step) You could navigate to a detail screen
                                onLocationInfoClicked(placeResult.placeId ?: "")
                            }
                        )
                    }
                }

            }

            // Your main content goes here.
            // When the search bar is not active, this content is visible.
            if (!active) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(top = 80.dp), // Add padding to not overlap with search bar
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Your saved places or comparison view will be here.")
                }
            }
            //

        }
    }
}

//@Composable
//fun MainBottomAppBar(
//    onAddRestarauntClicked: () -> Unit,
//    onListRestarauntsClicked: () -> Unit,
//) {
//    BottomAppBar(
//        actions = {
//            IconButton(onClick = onListRestarauntsClicked) {
//                Icon(
//                    Icons.AutoMirrored.Filled.List, // Corrected icon
//                    contentDescription = "List Saved Places"
//                )
//            }
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = onAddRestarauntClicked) {
//                Icon(Icons.Default.Add, contentDescription = "Add Place")
//            }
//        }
//    )
//}
