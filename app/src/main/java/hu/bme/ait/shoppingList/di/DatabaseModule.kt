package hu.bme.ait.shoppingList.di

import android.content.Context
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent;
import hu.bme.ait.shoppingList.data.AppDatabase;
import hu.bme.ait.shoppingList.data.shoppingItemDAO


@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideShoppingItemDao(appDatabase: AppDatabase) : shoppingItemDAO {
        return appDatabase.todoDao()
    }

    @Provides
    fun provideShoppingItemsDataBase(
        @ApplicationContext appContext: Context
    ) : AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

}