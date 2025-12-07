package hu.bme.ait.wanderer.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "locationsTable")
data class LocationInfoItem(
    @PrimaryKey(autoGenerate = true) val locationId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "visitDate") val visitDate: Date,
    @ColumnInfo(name = "locationType") val locationType: LocationTypeCategory,
    @ColumnInfo(name = "priceRange") val priceRange: PriceRangeCategory,
    @ColumnInfo(name = "ranking") val ranking: Int,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "address") val address: String
)

enum class LocationTypeCategory(val displayName: String){
    RESTAURANT("Restaurant"),
    CAFE("Cafe"),
    DESSERT("Dessert"),
    PUB("Pub"),
    STUDYSPOT("Study spot"),
    THRIFTING("Thrifting"),
    SHOPPING("Shopping")

}


enum class PriceRangeCategory(val displayName: String){
    CHEAP("$"),
    MEDIUM("$$"),
    EXPENSIVE("$$$"),
    SUPEREXPENSIVE("$$$$"),
    UNKNOWN("Unknown")
}


