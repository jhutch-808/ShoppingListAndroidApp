package hu.bme.ait.wanderer.di

import android.content.Context
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent;
import hu.bme.ait.wanderer.data.AppDataBase
import hu.bme.ait.wanderer.data.LocationDAO



@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideLocationItemDao(appDatabase: AppDataBase) : LocationDAO {
        return appDatabase.locationDAO()
    }

    @Provides
    fun provideLocationItemsDataBase(
        @ApplicationContext appContext: Context
    ) : AppDataBase {
        return AppDataBase.getDatabase(appContext)
    }

}