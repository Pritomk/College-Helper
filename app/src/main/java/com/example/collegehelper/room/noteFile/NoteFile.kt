package com.example.collegehelper.room.noteFile

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity(tableName = "note_file_table")
data class NoteFile(
//    @PrimaryKey
    val nid: Long = 0L,
    val fileName: String = "",
    val reference: String = ""
)