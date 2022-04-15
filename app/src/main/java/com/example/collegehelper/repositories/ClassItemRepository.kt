package com.example.collegehelper.repositories

import androidx.lifecycle.LiveData
import com.example.collegehelper.daos.ClassItemDao
import com.example.collegehelper.room.classItem.ClassItem

class ClassItemRepository(private val classItemDao: ClassItemDao) {

    val allClassItems = classItemDao.getAllClassItems()

    suspend fun insertClassItem(classItem: ClassItem) {
        classItemDao.insert(classItem)
    }

    suspend fun updateClassItem(classItem: ClassItem) {
        classItemDao.update(classItem)
    }

    suspend fun deleteClassItem(classItem: ClassItem) {
        classItemDao.delete(classItem)
    }

    fun getClassMongoId(cid: Long): LiveData<String> {
        return classItemDao.getClassMongoId(cid)
    }
}