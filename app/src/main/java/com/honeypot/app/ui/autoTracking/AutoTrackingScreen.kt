package com.honeypot.app.ui.autoTracking

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.ui.autoTracking.viewModel.AutoTrackViewModel
import com.honeypot.app.ui.dashboard.budget.components.CreateBudgetSectionCard
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Light100
import com.honeypot.app.ui.theme.Orange
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.roundedBorder
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlin.math.roundToInt

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AutoTrackingScreen(
    navController: NavHostController,
    autoTrackingViewModel: AutoTrackViewModel = hiltViewModel()
) {

    val smsFeatureState by autoTrackingViewModel.isSmsFeatureEnable.collectAsStateWithLifecycle()

    val permissions = listOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
    val permissionState = rememberMultiplePermissionsState(permissions)

    // Permission Dialog
    var showSmsPermissionDialog by remember { mutableStateOf(false) }
    if (showSmsPermissionDialog && !permissionState.allPermissionsGranted) {
        SmsPermissionDialog(
            permissions = permissions,
            onDismiss = {
                showSmsPermissionDialog = false
            }
        )
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (!permissionState.allPermissionsGranted) {
            autoTrackingViewModel.toggleSmsFeature(false)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                heading = stringResource(R.string.auto_tracking),
                navController
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AutoTrackingSection(
                smsFeatureState = smsFeatureState,
                onEnable = {
                    autoTrackingViewModel.toggleSmsFeature(it)

                    /**
                     * We Removed SMS PERMISSIONS AND SMS SERVICES TO TRACK Transactions
                     * instead we are using notification service to track sms transactions
                     * so permission request dialog not required
                     */
//                    if (it) {
//                        if (permissionState.allPermissionsGranted) {
//                            autoTrackingViewModel.toggleSmsFeature(
//                                true
//                            )
//                        } else {
//                            showSmsPermissionDialog = true
//                        }
//                    } else {
//                        autoTrackingViewModel.toggleSmsFeature(false)
//                    }
                }
            )
            SafeAndSecureSection()
        }

    }
}

@Composable
private fun AutoTrackingSection(smsFeatureState: Boolean, onEnable: (Boolean) -> Unit) {
    CreateBudgetSectionCard(title = "", isAnimateSize = false) {
        VerticalSpace(34.dp)
        Image(
            painter = rememberAsyncImagePainter(R.drawable.img_autotracking),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        VerticalSpace(14.dp)
        Text(
            text = stringResource(R.string.track_your_expenses_automatically_with_sms_notifications_secure_private_and_effortless),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center,
        )
        VerticalSpace(14.dp)
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val density = LocalDensity.current

            val parentWidthDp = with(density) { constraints.maxWidth.toDp() }

            ConfirmationButton(
                width = parentWidthDp,
                isEnable = smsFeatureState,
                onEnable = onEnable
            )
        }
    }
}

@Composable
private fun SafeAndSecureSection() {
    CreateBudgetSectionCard(title = "", isAnimateSize = false) {
        SecureRow(
            text = stringResource(R.string.safe_secure),
            iconRes = R.drawable.ic_shield_check,
            iconSize = 16.dp
        )
        SecureRow(
            stringResource(R.string.no_otps_or_personal_messages_accessed),
            R.drawable.ic_shield_user,
            16.dp
        )
        SecureRow(
            stringResource(R.string.data_stays_on_your_device_no_servers_involved),
            R.drawable.ic_safe_square,
            16.dp
        )
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun ConfirmationButton(
    width: Dp,
    onEnable: (Boolean) -> Unit,
    isEnable: Boolean
) {
    val dragSize = 42.dp
    val swipeableState = rememberSwipeableState(initialValue = isEnable)
    val isEnableTemp = remember { isEnable }

    val sizePx = with(LocalDensity.current) {
        (width - 10.dp - dragSize).toPx()
    }
    val anchors = mapOf(0f to false, sizePx to true)
    val progress =
        if (swipeableState.offset.value == 0f) 0f else swipeableState.offset.value / sizePx

    val activeSlideTextOffset by animateDpAsState(
        targetValue = androidx.compose.ui.unit.lerp((-200).dp, 0.dp, progress),
        label = ""
    )
    val offSlideTextOffset by animateDpAsState(
        targetValue = androidx.compose.ui.unit.lerp(0.dp, 200.dp, progress),
        label = ""
    )

    LaunchedEffect(swipeableState.currentValue, isEnable) {
        if (isEnable != swipeableState.currentValue) {
            onEnable(swipeableState.currentValue)
            if (isEnable == isEnableTemp) {
                swipeableState.animateTo(isEnable)
            }
        }

    }


    val colorAnimation by animateColorAsState(
        targetValue = lerp(
            start = Orange,
            stop = Green100,
            fraction = progress
        ),
        label = "",
    )

    Box(
        modifier = Modifier
            .width(width)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
            .roundedBorder(radius = dragSize, color = colorAnimation)
            .background(Light100, RoundedCornerShape(dragSize))
            .padding(5.dp)
            .clip(RoundedCornerShape(dragSize))
            .clipToBounds()
    ) {
        Text(
            stringResource(R.string.swipe_to_enable),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (dragSize / 2) - 5.dp)
                .offset(x = offSlideTextOffset)

        )
        Text(
            stringResource(R.string.currently_active),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(Green100),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-dragSize / 2) + 5.dp)
                .offset(x = activeSlideTextOffset)
        )
        DraggableControl(
            modifier = Modifier
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .size(dragSize),
            progress = progress
        )
    }
}


@Composable
private fun DraggableControl(
    modifier: Modifier,
    progress: Float
) {
    val animateRotate by animateFloatAsState(
        targetValue = androidx.compose.ui.util.lerp(0f, 180f, progress),
        label = ""
    )
    val animateColor by animateColorAsState(
        targetValue = lerp(Orange, Green100, progress),
        label = ""
    )
    Icon(
        painterResource(R.drawable.ic_double_arrow_right),
        contentDescription = "Confirm Icon",
        tint = Light100,
        modifier =
        modifier
            .shadow(
                elevation = 2.dp,
                CircleShape,
                clip = false
            )
            .background(animateColor, CircleShape)
            .padding(9.dp)
            .rotate(animateRotate),
    )
}


@Composable
private fun SecureRow(text: String, iconRes: Int, iconSize: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(iconRes),
            modifier = Modifier.size(iconSize),
            contentDescription = null,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
            )
        )
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}