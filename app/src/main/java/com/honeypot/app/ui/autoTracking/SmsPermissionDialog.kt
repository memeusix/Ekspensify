package com.honeypot.app.ui.autoTracking

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.honeypot.app.R
import com.honeypot.app.components.DrawableStartText
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.ui.theme.extendedColors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SmsPermissionDialog(
    permissions: List<String>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // Manage permission results
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult: Map<String, Boolean> ->
        val allPermissionsGranted = permissionsResult.values.all { it }
        if (allPermissionsGranted) {
            onDismiss()
        }
    }


    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SmsIcon()
            Text(
                "SMS Permission",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                stringResource(
                    R.string.this_allow_us_to_read_only_transactional_sms_to_automatic_track_your_expense_and_income
                ),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            VerticalSpace(1.dp)
            Highlights()
            VerticalSpace(1.dp)
            FilledButton(
                text = "Grant Permission",
                textModifier = Modifier.padding(vertical = 12.dp),
                shape = CircleShape,
                textStyle = MaterialTheme.typography.bodyLarge,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                onClick = {

                    val shouldShowPermissionRationale = permissions.any {
                        ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, it)
                    }

                    if (!shouldShowPermissionRationale) {
                        launcher.launch(permissions.toTypedArray())
                    } else {
                        openAppSettings(context)
                    }
                }
            )
            TermsAndPrivacyText()
        }
    }
}

@Composable
private fun TermsAndPrivacyText() {
    Text(
        buildAnnotatedString {
            append("By granting permission, you agree to our")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("\nTerms of Use")
            }
            append(" and ")
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append("Privacy Policy.")
            }
        },
        style = MaterialTheme.typography.labelSmall,
        textAlign = TextAlign.Center
    )
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun Highlights() {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        maxItemsInEachRow = 2
    ) {
        DrawableStartText(
            text = "No OTPs",
            icon = R.drawable.ic_vault,
            iconSpace = 5.dp,
            color = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 5.dp)
        )
        DrawableStartText(
            text = "No Personal SMS",
            icon = R.drawable.ic_vault,
            iconSpace = 5.dp,
            color = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 5.dp)
        )
        DrawableStartText(
            text = "Fully Local",
            icon = R.drawable.ic_vault,
            iconSpace = 5.dp,
            color = MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 5.dp)
        )
    }
}

@Composable
private fun SmsIcon() {
    Icon(
        rememberAsyncImagePainter(R.drawable.ic_sms),
        contentDescription = "",
        tint = MaterialTheme.extendedColors.iconColor,
        modifier = Modifier
            .width(60.dp)
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.secondary, CircleShape)
            .padding(15.dp)
    )
}