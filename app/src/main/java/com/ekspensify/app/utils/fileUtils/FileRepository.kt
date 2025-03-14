package com.ekspensify.app.utils.fileUtils

interface FileRepository {

    suspend fun saveImage(image: String, fileName: String): Result<String>

    suspend fun savePdf(pdfData: ByteArray, fileName: String): Result<String>
}