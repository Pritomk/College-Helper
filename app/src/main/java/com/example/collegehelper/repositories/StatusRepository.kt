package com.example.collegehelper.repositories

import androidx.lifecycle.LiveData
import com.example.collegehelper.daos.StatusDao
import com.example.collegehelper.room.status.Status
import com.example.collegehelper.room.student.Student

class StatusRepository(private val statusDao: StatusDao) {

    val allStatusItem = statusDao.getAllStatusItems()

    suspend fun insert(status: Status) {
        statusDao.insert(status)
    }

    suspend fun delete(status: Status) {
        statusDao.delete(status)
    }

    fun getAllStatus(cid: Long) : LiveData<List<Status>> {
        return statusDao.getAllStatus(cid)
    }

    fun getStatus(cid: Long, sid: Long, dateKey: String) : LiveData<String> {
        return statusDao.getStatus(cid, sid, dateKey)
    }

    fun getDateStatus(cid: Long, dateKey: String) : LiveData<List<Status>> {
        return statusDao.getDateStatus(cid,dateKey)
    }
}