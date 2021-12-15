package com.example.collegehelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collegehelper.room.ClassItem

@Dao
interface ClassItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classItem : ClassItem)

    @Delete
    suspend fun delete(classItem : ClassItem)

//    @Query("Select * from class_table order by cid ASC")
//    fun getAllClassItems() : LiveData<List<ClassItem>>
}