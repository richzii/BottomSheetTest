package com.bottomsheettest.app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bottomsheettest.app.models.Message

@Database(entities = [Message::class], version = 1)
abstract class MessagesDatabase: RoomDatabase() {
    abstract val dao: MessageDao
}