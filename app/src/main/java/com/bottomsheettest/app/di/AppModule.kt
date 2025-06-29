package com.bottomsheettest.app.di

import android.app.Application
import androidx.room.Room
import com.bottomsheettest.app.data.MessageDao
import com.bottomsheettest.app.data.MessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMessagesDatabase(appContext: Application): MessagesDatabase {
        return Room.databaseBuilder(
            context = appContext,
            klass = MessagesDatabase::class.java,
            name = "messages.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMessagesDao(database: MessagesDatabase): MessageDao {
        return database.dao
    }
}