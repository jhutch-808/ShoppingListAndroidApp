package hu.bme.ait.wanderer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDAO{

    @Query("SELECT * FROM locationsTable")
    fun getAllLocations(): Flow<List<LocationInfoItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(locationInfoItem: LocationInfoItem): Long

    @Update
    suspend fun update(locationInfoItem: LocationInfoItem)

    @Delete
    suspend fun delete(locationInfoItem: LocationInfoItem)

    @Query("DELETE FROM locationsTable")
    suspend fun deleteAll()

    @Query("SELECT * FROM locationsTable WHERE locationId = :locationId")
    fun getLocationById(locationId: Int): Flow<LocationInfoItem>

    @Query("SELECT * FROM locationsTable WHERE name = :name")
    fun getLocationByName(name: String): Flow<LocationInfoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabelCrossRefs(crossRefs: List<LocationLabelCrossRef>)

    @Transaction
    @Query(
        """
            SELECT * FROM locationsTable
            ORDER BY ranking DESC
        """
    )
    fun getLocationsWithLabelsSortedByRank(): Flow<List<LocationWithLabels>>






}