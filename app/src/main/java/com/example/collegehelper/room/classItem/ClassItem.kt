package com.example.collegehelper.room.classItem

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "class_table",
    indices = [androidx.room.Index(value = ["className", "subjectName"], unique = true)]
)
data class ClassItem(
    @PrimaryKey(autoGenerate = true)
    var cid: Long = 0L,
    var className: String = "",
    var subjectName: String = ""
)