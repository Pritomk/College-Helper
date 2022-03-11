package com.example.collegehelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collegehelper.room.notes.NoteClass

@Dao
interface NoteDao {

    @Insert
    suspend fun insert(noteClass: NoteClass)

    @Delete
    suspend fun delete(noteClass: NoteClass)

    @Update
    suspend fun update(noteClass: NoteClass)

    @Query("Select * from note_class_table order by nid ASC")
    fun getAllNoteCLass() : LiveData<List<NoteClass>>
}