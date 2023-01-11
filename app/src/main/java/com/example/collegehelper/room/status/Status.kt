package com.example.collegehelper.room.status

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "status_table", indices = [Index(value = ["sid", "dateKey"], unique = true)])
data class Status(
    @PrimaryKey(autoGenerate = true)
    var statusKey: Long = 0L,
    var sid: Long = 0L,
    var cid: Long = 0L,
    var status: String = "",
    var dateKey: String = "",
)
