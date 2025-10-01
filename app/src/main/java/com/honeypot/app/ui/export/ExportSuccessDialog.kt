package com.honeypot.app.ui.export

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.honeypot.app.R
import com.honeypot.app.components.DrawableEndText
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.utils.firstLetterCap
import java.time.LocalDate

@Composable
fun ExportSuccessDialog(
    date: LocalDate?,
    email: String?,
    onDismiss: () -> Unit,
) {

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LottieAnime()
            TitleText()
            DrawableEndText(
                text = email ?: "",
                icon = R.drawable.ic_verified_check,
                fontWeight = FontWeight.Bold,
                textSize = 14.sp,
                iconSize = 16.dp,
                colorFilter = false,
            )
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.for_))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(
                            if (date != null) "${
                                date.month?.name?.firstLetterCap()
                            }, ${date.year}" else null,
                        )
                    }
                },
                style = MaterialTheme.typography.labelMedium,
            )
            VerticalSpace(1.dp)
            HorizontalDivider()
            BulletPoint(
                stringResource(R.string.we_will_send_you_a_notification_once_your_statement_is_emailed_to_you)
            )
            BulletPoint(
                stringResource(R.string.please_make_sure_to_check_spam_folder)
            )
            VerticalSpace(1.dp)
            FilledButton(
                text = stringResource(R.string.got_it),
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(0.4f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                textModifier = Modifier.padding(vertical = 17.dp)
            )
        }
    }
}

@Composable
private fun TitleText() {
    Text(
        text = stringResource(R.string.your_transaction_statement_will_be_sent_to_your_registered_email),
        style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 16.sp
        ),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun LottieAnime() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.send_email_animation))
    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .aspectRatio(1f)
    )
}

@Composable
private fun BulletPoint(
    text: String
) {
    Row(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
