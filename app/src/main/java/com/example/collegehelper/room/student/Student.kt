package com.example.collegehelper.room.student

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_table")
data class Student(
    @PrimaryKey(autoGenerate = true)
    var sid : Long = 0L,
    var cid : Long = 0L,
    var studentName : String = "",
    var roll : Int = 0
)