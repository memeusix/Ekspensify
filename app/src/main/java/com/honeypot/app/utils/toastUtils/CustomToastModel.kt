package com.honeypot.app.utils.toastUtils

data class CustomToastModel(
    val message: String? = null,
    val duration: Long = 2000,
    var isVisible: Boolean = false,
    val type: ToastType = ToastType.ERROR,
)

enum class ToastType {
    SUCCESS, ERROR, WARNING, INFO
}