package com.example.collegehelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collegehelper.room.classItem.ClassItem

@Dao
interface ClassItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classItem : ClassItem)

    @Delete
    suspend fun delete(classItem : ClassItem)

    @Update
    suspend fun update(classItem: ClassItem)

    @Query("Select * from class_table order by cid ASC")
    fun getAllClassItems() : LiveData<List<ClassItem>>
}