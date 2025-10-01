package com.honeypot.app.utils.fileUtils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

object FileHelper {

    // ? converting image Url string into Bitmap

    suspend fun downloadImage(imageString: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val inputStream = URL(imageString).openStream()
            BitmapFactory.decodeStream(inputStream)
        }
    }


    // ? saving image bitmap into device storage for all api level

    fun saveBitmapToStorage(bitmap: Bitmap, fileName: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val url = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            url?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
            }
        } else {
            val pictureDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(pictureDir, fileName)
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        }
    }


    // ? saving PDF into device storage for all api level

    fun savePdfStorage(pdfData: ByteArray, fileName: String, context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
            }
            val url = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
            url?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    outputStream.write(pdfData)
                }
            }
        } else {
            val docsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val file = File(docsDir, fileName)
            FileOutputStream(file).use { outputStream ->
                outputStream.write(pdfData)
            }
        }
    }

}