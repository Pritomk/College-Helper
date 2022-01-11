package com.example.collegehelper.room.classItem

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.collegehelper.daos.ClassItemDao

@Database(entities = [ClassItem::class], version = 1, exportSchema = false)
abstract class ClassItemDatabase : RoomDatabase() {

    abstract fun getClassItemDao() : ClassItemDao

    companion object {
        @Volatile
        private var INSTANCE: ClassItemDatabase? = null

        fun getDatabase(context: Context): ClassItemDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClassItemDatabase::class.java,
                    "class_item_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}