package com.example.collegehelper.room.notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.collegehelper.daos.NoteDao

@Database(entities = [NoteClass::class], version = 1)
abstract class NoteClassDatabase : RoomDatabase() {

    abstract fun getNoteDao() : NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteClassDatabase? = null

        fun getDatabase(context: Context): NoteClassDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteClassDatabase::class.java,
                    "note_class_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}