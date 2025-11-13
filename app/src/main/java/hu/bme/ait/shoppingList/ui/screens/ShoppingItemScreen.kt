package hu.bme.ait.shoppingList.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import hu.bme.ait.shoppingList.R
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
import hu.bme.ait.shoppingList.data.ShoppingItem
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import hu.bme.ait.shoppingList.data.Category
import hu.bme.ait.shoppingList.ui.theme.Pink100
import hu.bme.ait.shoppingList.ui.theme.Pink80
import hu.bme.ait.shoppingList.ui.theme.Purple40
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingItemScreen(
    shoppingListViewModel: ShoppingListViewModel = hiltViewModel()
) {

    var showItemDialog by rememberSaveable { mutableStateOf( false) }
    var shoppingItemToEdit: ShoppingItem? by rememberSaveable { mutableStateOf<ShoppingItem?>(null) }

    var coroutineScope = rememberCoroutineScope()

    var sortState by rememberSaveable { mutableStateOf(SortState.NONE) }
    var shoppingItemList = shoppingListViewModel.getShoppingItems(sortState).collectAsState(emptyList())


    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.cutebackground),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxWidth()) {

        TopAppBar(
            title = {
                Text("Shopping List!")
            },
            colors = TopAppBarDefaults.topAppBarColors(Pink80),
            actions = {
                IconButton(
                    onClick = {
                        showItemDialog = true
                    }, colors = IconButtonDefaults.iconButtonColors(Purple40)

                ) {
                    Icon(Icons.Filled.AddCircle, "Add", tint = Color.White)

                }

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            shoppingListViewModel.removeAllShoppingItems()
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(Purple40)
                ) {
                    Icon(Icons.Filled.Delete, "Delete", tint = Color.White)
                }


                // Sorting button
                IconButton(onClick = {
                    sortState = when (sortState) {
                        SortState.NONE -> SortState.ASCENDING
                        SortState.ASCENDING -> SortState.DESCENDING
                        SortState.DESCENDING -> SortState.NONE
                    }
                }, colors = IconButtonDefaults.iconButtonColors(Purple40)) {
                    Icon(
                        imageVector = when (sortState) {
                            SortState.NONE -> Icons.Filled.Info
                            SortState.ASCENDING -> Icons.Filled.KeyboardArrowUp
                            SortState.DESCENDING -> Icons.Filled.KeyboardArrowDown
                        }, contentDescription = "Sort", tint = Color.White
                    )
                }


            }
        )

        if (showItemDialog) {
            ShoppingItemDialog(
                shoppingListViewModel,
                shoppingItemToEdit,
                onCancel = {
                    showItemDialog = false
                }
            )
        }

        if (shoppingItemList.value.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No items.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(shoppingItemList.value) { shoppingItem ->
                    ShoppingItemCard(
                        shoppingItem,
                        onDelete = { shoppingItem ->
                            shoppingListViewModel.removeShoppingItem(shoppingItem)
                        },
                        onChecked = { shoppingItem, checked ->
                            shoppingListViewModel.changeShoppingItemState(shoppingItem, checked)
                        },
                        onShoppingItemEdit = { selectedItem ->
                            shoppingItemToEdit = selectedItem
                            showItemDialog = true

                        }
                    )
                }

            }
        }
    }
    }
}

enum class SortState{
    NONE,
    ASCENDING,
    DESCENDING
}

@Composable
fun ShoppingItemCard(
    shoppingItem: ShoppingItem,
    onDelete:(ShoppingItem) -> Unit,
    onChecked: (ShoppingItem, Boolean) -> Unit,
    onShoppingItemEdit: (ShoppingItem) -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(Pink100),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        var expanded by remember {mutableStateOf(false)}
        Column(modifier = Modifier
            .padding(5.dp)
            .animateContentSize()) {

        }
        Row(
            modifier = Modifier
                .padding(20.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = shoppingItem.category.getIcon()),
                contentDescription = "Category",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
            )

            Text(shoppingItem.name, modifier = Modifier.fillMaxWidth(0.2f))
            Text("$${"%.2f".format(shoppingItem.price)}", modifier = Modifier.fillMaxWidth(0.3f))
            Spacer(modifier = Modifier.fillMaxSize(0.20f))
            Checkbox(
                checked = shoppingItem.isBought,
                onCheckedChange = {checkboxSate -> onChecked(shoppingItem, checkboxSate) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                modifier = Modifier.clickable {
                    onDelete(shoppingItem)
                },
                tint = Color.Red
            )
            Icon(
                imageVector = Icons.Filled.Build,
                contentDescription = "Edit",
                modifier = Modifier.clickable {
                    onShoppingItemEdit(shoppingItem)
                },
                tint = Color.Blue
            )
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded) {
                        "Less"
                    } else {
                        "More"
                    }
                )
            }

        }
        if (expanded){
            Text(
                text = shoppingItem.description,
                fontSize = 12.sp
            )
            Text(
                text = shoppingItem.createDate,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ShoppingItemDialog(
    viewModel: ShoppingListViewModel,
    shoppingItemToEdit: ShoppingItem? = null,
    onCancel: () -> Unit
) {
    var shoppingItemName by remember {
        mutableStateOf(
            shoppingItemToEdit?.name ?: ""
        )
    }

    val isNameValid = shoppingItemName.isNotBlank()

    var shoppingItemDescription by remember {
        mutableStateOf(
            shoppingItemToEdit?.description ?: ""
        )
    }
    var selectedCategory by remember {
        mutableStateOf( shoppingItemToEdit?.category?: Category.PRODUCE)
    }

    // Float state for the actual values which is going to be stored
    var priceValue by rememberSaveable { mutableStateOf(shoppingItemToEdit?.price ?: 0f) }


    // String state for display
    var priceText by remember {
        mutableStateOf(
            if (shoppingItemToEdit != null){
                "%.2f".format(shoppingItemToEdit.price)
            } else {
                ""
            }
        )
    }

    val isPriceValid = priceText.isNotBlank() && priceText != "."

    var categoryExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {
        onCancel()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    if (shoppingItemToEdit == null) "New Item" else "Edit Item",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Shopping Item Name") },
                    value = "$shoppingItemName",
                    onValueChange = { shoppingItemName = it },
                    isError = !isNameValid,
                    supportingText = {
                        if (!isNameValid) {
                            Text("Name can't be empty", color = MaterialTheme.colorScheme.error)
                        }
                        }

                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Item description") },
                    value = "$shoppingItemDescription",
                    onValueChange = { shoppingItemDescription = it })
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Enter Amount") },
                    value = priceText,
                    onValueChange = { newText ->
                        val cleanedPrice = newText.filter { it.isDigit() || it =='.' }
                        if (cleanedPrice.count(){it == '.'} <= 1){
                            priceText = cleanedPrice
                            priceValue = cleanedPrice.toFloatOrNull()?: 0.0f

                        }
                    },
                    prefix = {Text("$")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = !isPriceValid,
                    supportingText = {
                        if(!isPriceValid) {
                            Text("Price can't be empty", color = MaterialTheme.colorScheme.error)
                        }
                    }

                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        OutlinedTextField(
                            value = selectedCategory.name,
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = {

                                Icon(
                                    imageVector = if (categoryExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "Select Category",
                                    modifier = Modifier.clickable { categoryExpanded = !categoryExpanded }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // The actual dropdown menu
                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Category.values().forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category.name) },
                                    onClick = {
                                        selectedCategory = category
                                        categoryExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(id = category.getIcon()),
                                            contentDescription = null,
                                            tint = Color.Unspecified
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                )
                {
                    //Cancel Button
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))

                    //Save Button
                    TextButton(onClick = {
                        if (shoppingItemToEdit == null) {
                            viewModel.addShoppingItem(
                                ShoppingItem(
                                    // id column is not necessary since it will be autogenerated
                                    name = shoppingItemName,
                                    description = shoppingItemDescription,
                                    createDate = Date(System.currentTimeMillis()).toString(),
                                    category = selectedCategory,
                                    isBought = false,
                                    price = priceValue
                                )
                            )
                        } else {
                            val editedShoppingItem = shoppingItemToEdit.copy(
                                name = shoppingItemName,
                                description = shoppingItemDescription,
                                category = selectedCategory,
                                price = priceValue
                            )
                            viewModel.updateShoppingItem(editedShoppingItem)

                        }

                        onCancel()
                    },
                        enabled = isNameValid && isPriceValid
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}