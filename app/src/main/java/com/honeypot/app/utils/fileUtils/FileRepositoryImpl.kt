package com.honeypot.app.utils.fileUtils

import android.content.Context
import javax.inject.Inject

class FileRepositoryImpl @Inject constructor(
    private val context: Context
) : FileRepository {
    override suspend fun saveImage(image: String, fileName: String): Result<String> {
        return try {
            val bitmap = FileHelper.downloadImage(image)
            FileHelper.saveBitmapToStorage(bitmap, fileName, context)
            Result.success("Image Saved Successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun savePdf(pdfData: ByteArray, fileName: String): Result<String> {
        return try {
            FileHelper.savePdfStorage(pdfData, fileName, context)
            Result.success("PDF Saved Successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}