package com.example.collegehelper.room.student

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.collegehelper.daos.StudentDao
import com.example.collegehelper.room.classItem.ClassItemDatabase

@Database(entities = [Student::class], version = 1, exportSchema = true)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun getStudentDao() : StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        fun getDatabase(context: Context): StudentDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "student_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}