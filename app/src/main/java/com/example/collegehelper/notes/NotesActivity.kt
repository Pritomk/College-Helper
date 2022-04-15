package com.example.collegehelper.notes

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.collegehelper.databinding.ActivityNotesBinding
import com.example.collegehelper.pdfViewer.PdfViewActivity
import com.example.collegehelper.room.noteFile.NoteFile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jaiselrahman.filepicker.activity.FilePickerActivity
import com.jaiselrahman.filepicker.config.Configurations
import com.jaiselrahman.filepicker.model.MediaFile

class NotesActivity : AppCompatActivity(), OnNoteFileClickListener {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var notesRecycler: RecyclerView
    private lateinit var addFab: FloatingActionButton
    private val STORAGE_PERMISSION_REQUEST_CODE = 41
    private val WRITE_STORAGE_PERMISSION_REQUEST_CODE = 51
    private val mediaFiles = ArrayList<MediaFile>()
    private lateinit var viewModel: NotesActivityViewModel
    private lateinit var className: String
    private lateinit var fileName: String

    private val TAG = "com.example.collegehelper.notes.NotesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        className = intent.getStringExtra("className").toString()

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, NotesViewModelFactory(application))[NotesActivityViewModel::class.java]

        addFab = binding.addNotesFab
        notesRecycler = binding.notesRecycler

        viewModel.noteFiles.observe(this) {
            Log.d(TAG, "$it")
        }
//        //check permission
        if (!checkPermissionForReadExternalStorage()) {
            requestPermissionForReadExternalStorage()
//            requestPermissionForWriteExternalStorage()
        }

        viewModel.fetchFiles(className)

        addFab.setOnClickListener {
            docPicker()
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        val adapter = NoteFileAdapter(this)
        notesRecycler.adapter = adapter
        notesRecycler.layoutManager = LinearLayoutManager(this)
        viewModel.noteFiles.observe(this) {
            Log.d(TAG,"$it")
            adapter.updateList(it as ArrayList<NoteFile>)
        }
    }

    private fun checkPermissionForReadExternalStorage(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val result1: Int = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            val result2: Int = this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    @Throws(Exception::class)
    fun requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(
                (this as Activity?)!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQUEST_CODE
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

//    @Throws(Exception::class)
//    fun requestPermissionForWriteExternalStorage() {
//        try {
//            ActivityCompat.requestPermissions(
//                (this as Activity?)!!, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                WRITE_STORAGE_PERMISSION_REQUEST_CODE
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            throw e
//        }
//    }

    private fun docPicker() {
        val intent = Intent(this, FilePickerActivity::class.java)
        intent.putExtra(
            FilePickerActivity.CONFIGS, Configurations.Builder()
                .setCheckPermission(true)
                .setSelectedMediaFiles(mediaFiles)
                .setShowFiles(true)
                .setShowImages(false)
                .setShowAudios(false)
                .setShowVideos(false)
                .setIgnoreNoMedia(false)
                .enableVideoCapture(false)
                .enableImageCapture(false)
                .setIgnoreHiddenFile(false)
                .setMaxSelection(1)
                .build())
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 101 && resultCode === RESULT_OK) {
            val mediaFiles: java.util.ArrayList<MediaFile>? = data?.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES)
            if (mediaFiles != null) {
                Log.d(TAG, "${mediaFiles[0].uri}")
                val mediaFile = mediaFiles[0]

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/pdf"
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, mediaFile.uri)
                }

                fileName = mediaFile.name

                startActivityForResult(intent, 102)
            } else {
                Toast.makeText(this, "Image not selected", Toast.LENGTH_SHORT).show()
            }
        }
        else if (resultCode == RESULT_OK && requestCode == 102) {
            data?.data?.also {
                viewModel.addPost(it,fileName, className)
            }
        }
    }

    override fun onNoteFileItemClicked(noteFile: NoteFile) {
        val intent = Intent(this, PdfViewActivity::class.java)
        intent.putExtra("fileName",noteFile.fileName)
        intent.putExtra("reference",noteFile.reference)
        startActivity(intent)
    }
}