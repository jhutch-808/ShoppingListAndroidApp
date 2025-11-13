package hu.bme.ait.shoppingList.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.ait.shoppingList.R




@Entity(tableName = "shoppingItemtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "category") var category:Category,
    @ColumnInfo(name = "isBought") var isBought: Boolean,
    @ColumnInfo(name = "price") var price: Float
)

enum class Category {
    PRODUCE, MEATS, CARBS, DRINKS;
    fun getIcon(): Int{
        when (this) {
            PRODUCE -> return R.drawable.produceicon
            MEATS -> return R.drawable.meatsicon
            CARBS -> return R.drawable.carbsicon
            DRINKS -> return R.drawable.drinksicon
        }
    }

}

