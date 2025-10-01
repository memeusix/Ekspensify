package com.honeypot.app.utils


import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.navigation.NavController
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.ErrorResponseModel
import com.honeypot.app.navigation.IntroScreenRoute
import com.honeypot.app.utils.spUtils.SpUtils
import com.honeypot.app.utils.spUtils.SpUtilsManager
import com.honeypot.app.utils.toastUtils.CustomToastModel
import com.honeypot.app.utils.toastUtils.ToastType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.min
import android.graphics.Color as AndroidColor

const val TAG: String = "GENERAL FUNCTIONS"


private var isClicked = false
fun singleClick(delayMillis: Long = 300, onClick: () -> Unit): () -> Unit {
    return {
        if (!isClicked) {
            isClicked = true
            onClick()

            // Reset `isClicked` to false after the delayMillis duration
            CoroutineScope(Dispatchers.Main).launch {
                delay(delayMillis)
                isClicked = false
            }
        }
    }
}


val gson = Gson()
inline fun <reified T> T?.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String?.fromJson(): T? {
    return gson.fromJson(this, object : TypeToken<T>() {}.type)
}


fun <T> handleApiResponse(
    response: ApiResponse<T>,
    toastState: MutableState<CustomToastModel?>,
    navController: NavController? = null,
    onSuccess: (T?) -> Unit,
) {
    when (response) {
        is ApiResponse.Success -> {
            onSuccess(response.data)
        }

        is ApiResponse.Failure -> {
            val errorMessage = response.errorResponse?.message ?: "An error occurred"
            val spUtils = navController?.context?.let { SpUtils(it) }?.let { SpUtilsManager(it) }

            if (response.errorResponse?.code == 401 && spUtils?.isLoggedIn?.value == true) {
                navController.let { nav ->
                    goToLogin(nav, errorMessage)
                    return
                }
            }

            toastState.value = CustomToastModel(
                message = errorMessage,
                isVisible = true,
                type = ToastType.ERROR
            )
        }

        else -> Unit
    }
}


fun <T> handleApiResponseWithError(
    response: ApiResponse<T>,
    toastState: MutableState<CustomToastModel?>? = null,
    navController: NavController? = null,
    onFailure: (ErrorResponseModel?) -> Unit,
    onSuccess: (T?) -> Unit
) {
    when (response) {
        is ApiResponse.Success -> {
            onSuccess(response.data)
        }

        is ApiResponse.Failure -> {
            val errorMessage = response.errorResponse?.message ?: "An error occurred"
            val spUtils = navController?.context?.let { SpUtils(it) }?.let { SpUtilsManager(it) }

            if (response.errorResponse?.code == 401 && spUtils?.isLoggedIn?.value == true) {
                navController.let { nav ->
                    goToLogin(nav, errorMessage)
                    return
                }
            }

            onFailure(response.errorResponse)
            toastState?.let {
                toastState.value = CustomToastModel(
                    message = errorMessage,
                    isVisible = true,
                    type = ToastType.ERROR
                )
            }
        }

        else -> Unit
    }
}

private fun goToLogin(
    navController: NavController,
    errorMessage: String
) {
    val spUtils = SpUtilsManager(SpUtils(navController.context))
    // Navigate to IntroScreen and clear back stack
    navController.navigate(IntroScreenRoute) {
        popUpTo(0) { inclusive = false }
    }
    // Perform logout using a helper function or manager
    spUtils.logout()
    // Display the error message as a toast
    Toast.makeText(navController.context, errorMessage, Toast.LENGTH_SHORT).apply {
        setGravity(Gravity.TOP, 0, 0)
        show()
    }

}


@Composable
fun SetWindowDim(value: Float = 0f) {
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
    window?.setDimAmount(value)
}


fun getFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val fileName = "temp_image_${System.currentTimeMillis()}.jpg"
        val tempFile = File(context.cacheDir, fileName)
        context.contentResolver.openInputStream(uri).use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }
        }
        tempFile
    } catch (e: Exception) {
        null
    }
}

fun spacedByWithFooter(space: Dp) = object : Arrangement.Vertical {

    override val spacing = space

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        outPositions: IntArray,
    ) {
        if (sizes.isEmpty()) return
        val spacePx = space.roundToPx()

        var occupied = 0
        var lastSpace = 0

        sizes.forEachIndexed { index, size ->

            if (index == sizes.lastIndex) {
                outPositions[index] = totalSize - size
            } else {
                outPositions[index] = min(occupied, totalSize - size)
            }
            lastSpace = min(spacePx, totalSize - outPositions[index] - size)
            occupied = outPositions[index] + size + lastSpace
        }
        occupied -= lastSpace
    }
}

fun formatBudgetId(id: Int?): String {
    if (id == null) return ""
    return "BGT #$id"
}

fun getColor(colorCode: String?, defaultColor: Color = Color.Gray): Color {
    return if (colorCode.isNullOrEmpty()) {
        defaultColor
    } else {
        try {
            Color(AndroidColor.parseColor(colorCode))
        } catch (e: IllegalArgumentException) {
            defaultColor
        }
    }
}
