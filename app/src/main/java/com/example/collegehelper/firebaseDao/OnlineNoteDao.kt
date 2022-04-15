package com.example.collegehelper.firebaseDao

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.collegehelper.room.noteFile.NoteFile
import com.example.collegehelper.room.notes.NoteClass
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class OnlineNoteDao {

    private val db = FirebaseFirestore.getInstance()
    private val noteCollection = db.collection("notes")
    private val auth = Firebase.auth
    private val TAG = "com.example.collegehelper.firebaseDao.OnlineNoteDao"
    private val uid = auth.currentUser!!.uid
    var fetchNoteFiles: MutableLiveData<List<NoteFile>> = MutableLiveData()

    fun insertNoteOnline(noteClass: NoteClass) {
        val className = noteClass.className;
        val subName = noteClass.subName;
        val map = HashMap<String, String>()
        map["className"] = className
        map["subName"] = subName
        noteCollection.document(auth.uid.toString()).collection("classes").document().set(map)

    }

    fun addFile(fileUri: Uri, fileName: String,className: String) {
        val reference = fileUpload(fileUri, className)
        val map = HashMap<String, String>()
        map["fileName"] = fileName
        map["reference"] = reference
        noteCollection.document(uid)
            .collection(className)
            .document()
            .set(map)
            .addOnSuccessListener {
                Log.d(TAG, "$it")
            }
            .addOnFailureListener {
                Log.e(TAG,"$it")
            }
            .addOnCompleteListener {
                Log.d(TAG, "$it")
            }
    }

    fun fetchFiles(className: String) {
        noteCollection.document(uid)
            .collection(className)
            .get()
            .addOnSuccessListener { task ->
                Log.d(TAG,"Task is ${task.documents}")
                if (!task.isEmpty) {
                    val list = ArrayList<NoteFile>()
                    for (document in task.documents) {
                        list.add(NoteFile(0,document["fileName"].toString(), document["reference"].toString()))
                    }
                    fetchNoteFiles.value = list
                }
            }
            .addOnFailureListener {
                Log.d(TAG,"Error is $it")
            }
    }

    private fun fileUpload(fileUri: Uri,className: String): String {
        val storage = Firebase.storage
        val user = Firebase.auth.currentUser!!

        val path: String = user.uid
        val referenceChild = "notes/$path/$className/${getTimeDate()}"
        val reference = storage.reference.child(referenceChild)
        reference.putFile(fileUri)
            .addOnSuccessListener {
                Log.e(TAG, it.metadata?.path.toString())
            }
            .addOnFailureListener { e: Exception ->
                Log.e(TAG, e.message.toString() + " executed ")
            }
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                    .totalByteCount
            }
            .addOnCompleteListener {
                Log.e(TAG, it.result.toString())
            }
        return referenceChild
    }

    private fun getTimeDate(): String {
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        val currentTime: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        return "$currentDate/$currentTime"
    }

}