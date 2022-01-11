package com.example.collegehelper.room.status

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.collegehelper.daos.StatusDao

@Database(entities = [Status::class], version = 1, exportSchema = false)
abstract class StatusDatabase : RoomDatabase() {

    abstract fun getStatusDao() : StatusDao

    companion object {
        @Volatile
        private var INSTANCE: StatusDatabase? = null

        fun getDatabase(context: Context): StatusDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StatusDatabase::class.java,
                    "status_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}