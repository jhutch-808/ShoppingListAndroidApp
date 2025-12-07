package hu.bme.ait.wanderer.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import kotlin.collections.map

data class LocationWithLabels(
    @Embedded // emebeds the main Locationifo object
    val location: LocationInfoItem,

    @Relation( // How room should fetch the labels
        parentColumn = "locationId",
        entityColumn = "locationId", // references the location ID
    )
    val crossRef: List<LocationLabelCrossRef>
){
    val labels: List<Label>
        get() = crossRef.map { it.label }
}

enum class Label(val displayText: String) {
    GOOD_FOR_STUDYING("Good for Studying"),
    GOOD_ATMOSPHERE("Good Atmosphere"),
    CASH_ONLY("Cash Only"),
    HAS_WIFI("Has WiFi"),
    OUTDOOR_SEATING("Outdoor Seating"),
    GOOD_FOR_GROUPS("Good for Groups"),
    VEGAN_OPTIONS("Vegan Options"),
    KID_FRIENDLY("Kid-Friendly"),
    ALCOHOL_FREE("Alcohol-Free"),
    CHEAP_EATS("Cheap Eats"),
    TAKE_OUT("Take-Out"),
    DELIVERY("Delivery"),
    GOOD_FOR_LUNCH("Good for Lunch"),
    GOOD_FOR_DINNER("Good for Dinner")
}