package com.honeypot.app.ui.picture

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.honeypot.app.R
import com.honeypot.app.navigation.PicturePreviewScreenRoute
import com.honeypot.app.ui.theme.Dark80
import com.honeypot.app.ui.theme.Light100
import com.honeypot.app.utils.fileUtils.FileViewModel
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel
import com.honeypot.app.utils.toastUtils.ToastType

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PicturePreviewScreen(
    args: PicturePreviewScreenRoute,
    fileViewModel: FileViewModel = hiltViewModel()
) {
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    val image = rememberAsyncImagePainter(args.image)

    var isLoading by remember { mutableStateOf(false) }

    val permission =
        Manifest.permission.WRITE_EXTERNAL_STORAGE

    val permissionState = rememberPermissionState(permission)

    val saveImageState by fileViewModel.fileSaveState.collectAsStateWithLifecycle()

    LaunchedEffect(saveImageState) {
        saveImageState?.onSuccess {
            isLoading = false
            toastState.value = CustomToastModel(
                message = it,
                type = ToastType.SUCCESS,
                isVisible = true
            )
            fileViewModel.resetToDefault()
        }?.onFailure {
            isLoading = false
            toastState.value = CustomToastModel(
                message = it.message,
                type = ToastType.ERROR,
                isVisible = true
            )
            fileViewModel.resetToDefault()
        }
    }

    PicturePreviewUI(
        image = image,
        isLoading = isLoading,
        onDownload = singleClick {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q && !permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            } else {
                isLoading = true
                fileViewModel.saveImage(args.image, "Transaction Attachment")
            }
        }
    )

}

@Composable
private fun PicturePreviewUI(
    image: AsyncImagePainter,
    isLoading: Boolean,
    onDownload: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Image(
            image,
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onDownload,
            enabled = !isLoading,
            modifier = Modifier
                .offset((-20).dp, (-20).dp)
                .shadow(20.dp, CircleShape, false, Light100)
                .size(50.dp)
                .background(Dark80, CircleShape)
                .align(Alignment.BottomEnd),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(30.dp),
                    color = MaterialTheme.colorScheme.background,
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 1.dp,
                )
            } else {
                Icon(
                    painterResource(R.drawable.ic_arrow_down_short),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}