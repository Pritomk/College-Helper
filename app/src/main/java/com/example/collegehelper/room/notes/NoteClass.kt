package com.example.collegehelper.room.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_class_table")
data class NoteClass(
    @PrimaryKey(autoGenerate = true)
    var nid: Long = 0L,
    var className: String = "",
    var subName: String = ""
)