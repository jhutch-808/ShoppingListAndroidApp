package hu.bme.ait.shoppingList.ui.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.ait.shoppingList.data.shoppingItemDAO
import hu.bme.ait.shoppingList.data.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(val shoppingItemDAO: shoppingItemDAO): ViewModel() {

    fun getAllShoppingItemAsAList(): Flow<List<ShoppingItem>> {
        return shoppingItemDAO.getAllShoppingItems()
    }
    fun addShoppingItem(shoppingItem: ShoppingItem) {

        viewModelScope.launch() {
            shoppingItemDAO.insert(shoppingItem)
        }
    }

    fun removeShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch {
            shoppingItemDAO.delete(shoppingItem)
        }
    }

    fun changeShoppingItemState(shoppingItem: ShoppingItem, value: Boolean) {
        // need to make a a new object to trigger a state change for the done check box
        val updatedItem = shoppingItem.copy(isBought = value)
        viewModelScope.launch {
            shoppingItemDAO.update(updatedItem)
        }

    }

    fun removeAllShoppingItems(){
        viewModelScope.launch {
            shoppingItemDAO.deleteAllShoppingItems()
        }
    }

    fun updateShoppingItem(editedShoppingItem: ShoppingItem) {

        viewModelScope.launch {
            shoppingItemDAO.update(editedShoppingItem)
        }
    }


    fun getShoppingItems(sortState: SortState): Flow<List<ShoppingItem>> {
        return when (sortState) {
            SortState.NONE -> shoppingItemDAO.getAllShoppingItems()
            SortState.ASCENDING -> shoppingItemDAO.getShoppingItemsByPriceASC()
            SortState.DESCENDING -> shoppingItemDAO.getShoppingItemsByPriceDESC()
        }
    }



}