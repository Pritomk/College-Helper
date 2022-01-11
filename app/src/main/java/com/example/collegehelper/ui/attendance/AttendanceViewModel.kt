package com.example.collegehelper.ui.attendance

import android.app.Application
import androidx.lifecycle.*
import com.example.collegehelper.repositories.ClassItemRepository
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.room.classItem.ClassItemDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : ClassItemRepository
    val allClassItems : LiveData<List<ClassItem>>

    init {
        val dao = ClassItemDatabase.getDatabase(application).getClassItemDao()
        repository = ClassItemRepository(dao)
        allClassItems = repository.allClassItems
    }

    fun deleteClassItem(classItem : ClassItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteClassItem(classItem)
    }

    fun insertClassItem(classItem: ClassItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertClassItem(classItem)
    }
}