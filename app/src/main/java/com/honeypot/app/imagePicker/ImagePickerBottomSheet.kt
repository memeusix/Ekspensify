package com.honeypot.app.imagePicker

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.honeypot.app.R
import com.honeypot.app.ui.theme.extendedColors
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ImagePickerBottomSheet(
    onDismiss: (Uri?) -> Unit
) {
    val context = LocalContext.current

    val permission = Manifest.permission.CAMERA
    val permissionState = rememberPermissionState(permission)
    val checkForPermission = permissionState.status.isGranted

    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri.value = uri
            onDismiss(selectedImageUri.value)
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                onDismiss(selectedImageUri.value)
            }
        }


    ModalBottomSheet(
        onDismissRequest = { onDismiss(null) },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(R.drawable.ic_camera),
                contentDescription = "Camera",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.extendedColors.imageBg)
                    .clickable {
                        if (checkForPermission) {
                            val photoUri = createImageUri(context)
                            selectedImageUri.value = photoUri
                            if (photoUri != null) {
                                cameraLauncher.launch(photoUri)
                            }
                        } else {
                            permissionState.launchPermissionRequest()
                        }
                    }
                    .padding(20.dp)
            )
            Icon(
                painterResource(R.drawable.ic_gallery),
                contentDescription = "Gallery",
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.extendedColors.imageBg)
                    .clickable {
//                        if (checkForPermission) {
                        imagePickerLauncher.launch("image/*")
//                        } else {
//                            permissionState.launchMultiplePermissionRequest()
//                        }
                    }
                    .padding(20.dp)
            )
        }
    }

}

fun createImageUri(context: Context): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Android 10+ (API 29+): Use MediaStore
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "captured_image_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } else {
        // Android 9 and below: Use FileProvider
        val photoFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "captured_image_${System.currentTimeMillis()}.jpg"
        )
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            photoFile
        )
    }
}


//fun createImageUri(context: Context): Uri? {
//    val contentResolver = context.contentResolver
//    val contentValues = ContentValues().apply {
//        put(
//            MediaStore.Images.Media.DISPLAY_NAME,
//            "captured_image_${System.currentTimeMillis()}.jpg"
//        )
//        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//    }
//    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//}