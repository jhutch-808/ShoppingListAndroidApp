package hu.bme.ait.wanderer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateConverter::class, LabelConverter::class)
@Database(entities = [LocationInfoItem::class, LocationLabelCrossRef::class], version = 3, exportSchema = false)
abstract class AppDataBase: RoomDatabase(){
    abstract fun locationDAO(): LocationDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "location_database.db"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }

    }



}