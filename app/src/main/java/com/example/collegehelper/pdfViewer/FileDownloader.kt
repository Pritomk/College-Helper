package com.example.collegehelper.pdfViewer

import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class FileDownloader {

    private val MEGABYTE = 1024 * 1024

    fun downloadFile(fileUrl: String?, directory: File?) {
        try {
            val url = URL(fileUrl)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect()
            val inputStream: InputStream = urlConnection.getInputStream()
            val fileOutputStream = FileOutputStream(directory)
            urlConnection.getContentLength()
            val buffer = ByteArray(MEGABYTE)
            var bufferLength = 0
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutputStream.write(buffer, 0, bufferLength)
            }
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}