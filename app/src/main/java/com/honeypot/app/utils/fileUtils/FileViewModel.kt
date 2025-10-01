package com.honeypot.app.utils.fileUtils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FileViewModel @Inject constructor(
    private val fileRepository: FileRepository,
) : ViewModel() {

    // ? handling state for file saving

    private val _fileSaveState = MutableStateFlow<Result<String>?>(null)
    val fileSaveState = _fileSaveState.asStateFlow()

    // ? save image

    fun saveImage(imageUrl: String, fileName: String) {
        viewModelScope.launch {
            val result = fileRepository.saveImage(imageUrl, fileName)
            _fileSaveState.value = result
        }
    }

    // ? save pdf

    fun savePDF(pdfData: ByteArray, fileName: String) {
        viewModelScope.launch {
            val result = fileRepository.savePdf(pdfData, fileName)
            _fileSaveState.value = result
        }
    }


    fun resetToDefault() {
        _fileSaveState.value = null
    }
}