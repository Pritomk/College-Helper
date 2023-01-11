package com.example.collegehelper.firebaseDao

import androidx.lifecycle.MutableLiveData
import com.example.collegehelper.room.classItem.ClassItem
import com.example.collegehelper.room.noteFile.NoteFile
import com.example.collegehelper.room.notes.NoteClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class OnlineClassDao {

    private val db = FirebaseFirestore.getInstance()
    private val noteCollection = db.collection("attendance")
    private val auth = Firebase.auth
    private val TAG = "com.example.collegehelper.firebaseDao.OnlineNoteDao"

    fun insertClassOnline(className: String , subName: String) {
        val map = HashMap<String, String>()
        map["className"] = className
        map["subName"] = subName
        noteCollection.document(auth.uid.toString()).collection("classes").document().set(map)
    }

}