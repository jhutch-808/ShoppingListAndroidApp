package hu.bme.ait.wanderer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDAO{

    @Query("SELECT * FROM locationsTable")
    fun getAllLocations(): Flow<List<LocationInfoItem>>


}