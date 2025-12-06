package hu.bme.ait.wanderer.data

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}


public class LabelConverter {
    @TypeConverter
    fun toLabel(value: String) = enumValueOf<Label>(value)

    @TypeConverter
    fun fromLabel(value: Label) = value.name
}