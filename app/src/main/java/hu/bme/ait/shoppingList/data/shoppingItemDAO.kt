package hu.bme.ait.shoppingList.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface shoppingItemDAO{

    @Query("SELECT * FROM shoppingItemtable")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingItemtable WHERE id = :id")
    fun getShoppingItem(id: Int): Flow<ShoppingItem>

    // counts how many row we have in the table
    @Query("SELECT COUNT(*) from shoppingItemtable")
    suspend fun getTodosNum(): Int

    // counts how many HIGH prio todoItem we have in the table
    @Query("SELECT COUNT(*) from shoppingItemtable WHERE category=:category")
    suspend fun getSpecifiedCategory(category: Category): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shoppingItem: ShoppingItem)

    @Update
    suspend fun update(shoppingItem: ShoppingItem)

    // Deletes a singulare shopping item
    @Delete
    suspend fun delete(shoppingItem: ShoppingItem)

    @Query("DELETE from shoppingItemtable")
    suspend fun deleteAllShoppingItems()

    @Query("SELECT * from shoppingItemtable ORDER BY price ASC")
    fun getShoppingItemsByPriceASC(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingItemtable ORDER BY price DESC")
    fun getShoppingItemsByPriceDESC(): Flow<List<ShoppingItem>>

}