package hu.bme.ait.wanderer.data

import androidx.room.Entity

@Entity(tableName = "location_label_cross_ref", primaryKeys = ["locationId","label"])
data class LocationLabelCrossRef(
    val locationId: Long,
    val label: Label
)