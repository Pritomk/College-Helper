package com.example.collegehelper.ui.attendance

import android.app.Application
import androidx.lifecycle.*
import com.example.collegehelper.repositories.ClassItemRepository
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.room.classItem.ClassItemDatabase
import com.example.collegehelper.volleyDao.ClassDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : ClassItemRepository
    val allClassItems : LiveData<List<ClassItem>>
    private val classDao: ClassDao

    init {
        val dao = ClassItemDatabase.getDatabase(application).getClassItemDao()
        classDao = ClassDao(application)
        repository = ClassItemRepository(dao)
        allClassItems = repository.allClassItems
    }


    //Local database methods
    fun deleteClassItem(classItem : ClassItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteClassItem(classItem)
    }

    fun insertClassItem(classItem: ClassItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertClassItem(classItem)
    }

    fun updateClassItem(classItem: ClassItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateClassItem(classItem)
    }


    //Online database methods
    @OptIn(DelicateCoroutinesApi::class)
    fun insertClassOnline(className: String, subName: String){
        GlobalScope.launch {
            classDao.insertClass(className, subName)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun updateClassOnline(className: String, subName: String, mongoId: String) {
        GlobalScope.launch {
            classDao.updateClass(className, subName,mongoId)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun deleteClassOnline(mongoId: String) {
        GlobalScope.launch {
            classDao.deleteClassItem(mongoId)
        }
    }
}