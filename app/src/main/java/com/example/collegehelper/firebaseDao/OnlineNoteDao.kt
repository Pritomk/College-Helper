package com.example.collegehelper.firebaseDao

import android.util.Log
import com.example.collegehelper.room.notes.NoteClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class OnlineNoteDao {

    private val db = FirebaseFirestore.getInstance()
    private val noteCollection = db.collection("notes")
    private val auth = Firebase.auth
    private val TAG = "com.example.collegehelper.firebaseDao.OnlineNoteDao"


    fun insertNoteOnline(noteClass: NoteClass) {
        val className = noteClass.className;
        val subName = noteClass.subName;
        val map = HashMap<String, String>()
        map["className"] = className
        map["subName"] = subName
        noteCollection.document(auth.uid.toString()).collection("classes").document().set(map).addOnCompleteListener {
            Log.d(TAG, "$it")
        }
    }
}