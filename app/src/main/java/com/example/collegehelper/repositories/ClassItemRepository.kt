package com.example.collegehelper.repositories

import com.example.collegehelper.daos.ClassItemDao
import com.example.collegehelper.room.ClassItem

class ClassItemRepository(private val classItemDao: ClassItemDao) {

//    val allClassItems = classItemDao.getAllClassItems()

    suspend fun insertClassItem(classItem: ClassItem) {
        classItemDao.insert(classItem)
    }

    suspend fun deleteClassItem(classItem: ClassItem) {
        classItemDao.delete(classItem)
    }
}