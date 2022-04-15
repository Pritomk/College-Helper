package com.example.collegehelper.notes

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.collegehelper.firebaseDao.OnlineNoteDao
//import com.example.collegehelper.repositories.NoteFileRepository
import com.example.collegehelper.room.noteFile.NoteFile
//import com.example.collegehelper.room.noteFile.NoteFileDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao = OnlineNoteDao()
//    private val repository: NoteFileRepository
    val noteFiles: LiveData<List<NoteFile>>

    init {
//        val noteFileDao = NoteFileDatabase.getDatabase(application).getNoteFileDao()
//        repository = NoteFileRepository(noteFileDao)
        noteFiles = noteDao.fetchNoteFiles
    }

    fun addPost(fileUri: Uri, fileName: String, className: String) {
        GlobalScope.launch {
            noteDao.addFile(fileUri, fileName, className)
        }
    }

    fun fetchFiles(className: String) {
        GlobalScope.launch {
            noteDao.fetchFiles(className)
        }
    }

    //    Room function
//    fun insertNoteFile(noteFile: NoteFile) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertNoteFile(noteFile)
//    }
//
//    fun deleteNoteFile(noteFile: NoteFile) = viewModelScope.launch(Dispatchers.IO) {
//        repository.deleteNoteFile(noteFile)
//    }
}