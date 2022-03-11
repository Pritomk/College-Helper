package com.example.collegehelper.repositories

import com.example.collegehelper.daos.NoteDao
import com.example.collegehelper.room.notes.NoteClass

class NoteClassRepository(private val noteDao: NoteDao) {

    val allNoteItems = noteDao.getAllNoteCLass()

    suspend fun insertNote(noteClass: NoteClass) {
        noteDao.insert(noteClass)
    }

    suspend fun deleteNote(noteClass: NoteClass) {
        noteDao.delete(noteClass)
    }

    suspend fun updateNote(noteClass: NoteClass) {
        noteDao.update(noteClass)
    }
}