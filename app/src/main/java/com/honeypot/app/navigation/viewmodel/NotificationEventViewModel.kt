package com.honeypot.app.navigation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.honeypot.app.utils.fromJson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.Serializable
import javax.inject.Inject


@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {
    private val _notificationEvent = MutableStateFlow<NotificationEventModel?>(null)
    val notificationEvent = _notificationEvent.asStateFlow()

    fun setNotificationEvent(notificationEvent: String?) {
        val notificationModel = notificationEvent.fromJson<NotificationEventModel>()
        _notificationEvent.value = notificationModel
    }

    fun resetNotificationEvent() {
        _notificationEvent.value = null
    }
}


data class NotificationEventModel(
    @SerializedName("notificationId")
    @Expose
    val notificationId: String? = null,

    @SerializedName("title")
    @Expose
    val title: String? = null,

    @SerializedName("body")
    @Expose
    val body: String? = null,

    @SerializedName("additionalData")
    @Expose
    val additionalData: NameValuePairs? = null,

    @SerializedName("launchURL")
    @Expose
    val launchURL: Any? = null,
) : Serializable

data class NameValuePairs(
    @SerializedName("nameValuePairs")
    @Expose
    val nameValuePairs: NotificationAdditionalData? = null,
)

data class NotificationAdditionalData(
    @SerializedName("activity")
    @Expose
    val activity: String? = null,

    @SerializedName("id")
    @Expose
    val id: String? = null
) : Serializable