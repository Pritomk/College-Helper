package com.example.collegehelper.pdfViewer

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.collegehelper.databinding.ActivityPdfViewBinding
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import java.io.*
import java.net.URL
import java.net.URLConnection
import kotlin.properties.Delegates


class PdfViewActivity : AppCompatActivity() {

    private val TAG = "com.example.collegehelper.pdfViewer.PdfViewActivity"
    private lateinit var binding: ActivityPdfViewBinding
    private lateinit var pdfView: PDFView
    private lateinit var fileName: String
    private var downloadId by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfView = binding.pdfViewer
        fileName = intent.getStringExtra("fileName").toString()
        fileName = fileName.substring(0, fileName.length - 4)

        openPdf()

        registerReceiver(onDownLoadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun openPdf() {
        val file = File("${Environment.getExternalStorageDirectory()}/$DIRECTORY_DOWNLOADS/${fileName}.pdf")
        if (file.exists()) {
            pdfView.fromFile(file).load()
        } else {
            setPdf()
        }
    }


    private fun setPdf() {
        val storagePath = "${Environment.getExternalStorageDirectory()}/Download/"
        val reference = intent.getStringExtra("reference").toString()
        Firebase.storage.reference.child(reference).downloadUrl
            .addOnSuccessListener {
                Log.d(TAG, "$it")

                downloadFile(this, fileName, storagePath, ".pdf", it)

            }
            .addOnCompleteListener {

            }

//        val file = File(storagePath)


    }
    private fun downloadFile(
        context: Context,
        fileName: String,
        storagePath: String,
        fileExtension: String,
        uri: Uri
    ) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(uri)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir( DIRECTORY_DOWNLOADS, "$fileName$fileExtension")

        downloadId = downloadManager.enqueue(request)

    }

    private val onDownLoadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadId == id) {
                Toast.makeText(this@PdfViewActivity, "Download Completed", Toast.LENGTH_SHORT).show();

                val file = File("${Environment.getExternalStorageDirectory()}/$DIRECTORY_DOWNLOADS/${fileName}.pdf")
                pdfView.fromFile(file).load()
            }
        }
    }

}