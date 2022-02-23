package com.example.collegehelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.collegehelper.room.status.Status

@Dao
interface StatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(status: Status)

    @Delete
    suspend fun delete(status: Status)

    @Query("Select * from status_table order by statusKey ASC")
    fun getAllStatusItems() : LiveData<List<Status>>

    @Query("Select * from status_table where cid = :cid")
    fun getAllStatus(cid: Long) : LiveData<List<Status>>

    @Query("Select status from status_table where cid = :cid and sid = :sid and dateKey = :dateKey")
    fun getStatus(cid: Long, sid: Long, dateKey: String) : LiveData<String>

    @Query("Select * from status_table where cid = :cid and dateKey = :dateKey")
    fun getDateStatus(cid: Long, dateKey: String) : LiveData<List<Status>>
}