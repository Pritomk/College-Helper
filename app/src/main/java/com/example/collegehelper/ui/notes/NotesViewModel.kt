package com.example.collegehelper.ui.notes

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegehelper.daos.NoteDao
import com.example.collegehelper.firebaseDao.OnlineNoteDao
import com.example.collegehelper.repositories.NoteClassRepository
import com.example.collegehelper.room.notes.NoteClass
import com.example.collegehelper.room.notes.NoteClassDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesViewModel(private val application: Application) : ViewModel() {

    private val repository: NoteClassRepository
    val allNoteItems: LiveData<List<NoteClass>>
    private val noteDao: NoteDao = NoteClassDatabase.getDatabase(application).getNoteDao()
    private val onlineNoteDao: OnlineNoteDao

    init {
        repository = NoteClassRepository(noteDao)
        allNoteItems = repository.allNoteItems
        onlineNoteDao = OnlineNoteDao()
    }

    fun insertNoteClass(noteClass: NoteClass) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertNote(noteClass)
    }

    fun updateNoteClass(noteClass: NoteClass) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(noteClass)
    }

    fun deleteNoteClass(noteClass: NoteClass) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(noteClass)
    }

    fun insertNoteClassOnline(noteClass: NoteClass){
        GlobalScope.launch {
            onlineNoteDao.insertNoteOnline(noteClass)
        }
    }
}