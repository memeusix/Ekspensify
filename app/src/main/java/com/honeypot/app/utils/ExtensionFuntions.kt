package com.honeypot.app.utils

import android.content.Context
import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.honeypot.app.data.model.responseModel.AuthResponseModel
import com.honeypot.app.navigation.AccountScreenRoute
import com.honeypot.app.navigation.BottomNavRoute
import com.honeypot.app.ui.theme.extendedColors
import java.math.BigDecimal
import java.util.Locale

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun AuthResponseModel.goToNextScreenAfterLogin(
    navController: NavController
) {
    if (user?.accounts == 0) {
        navController.navigate(AccountScreenRoute()) {
            popUpTo(0) { inclusive = true }
        }
    } else {
        navController.navigate(
            BottomNavRoute
        ) {
            popUpTo(0) { inclusive = true }
        }
    }
}


fun Int.formatRupees(): String {
    val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
    val formattedVal = format.format(kotlin.math.abs(this))
    return if (this < 0) "- ₹ $formattedVal" else "₹ $formattedVal"
}

fun Double.formatRupees(): String {
    val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
    val formattedVal = format.format(kotlin.math.abs(this))
    return if (this < 0) "- ₹ $formattedVal" else "₹ $formattedVal"
}
fun BigDecimal.formatRupees(): String {
    val format = NumberFormat.getNumberInstance(Locale("en", "IN"))
    val formattedVal = format.format(this.abs())
    return if (this < BigDecimal.ZERO) "- ₹ $formattedVal" else "₹ $formattedVal"
}

@Composable
fun Modifier.drawImageCutoutCenter(
    bitmap: ImageBitmap
): Modifier {
    return this.drawWithContent {
        with(drawContext.canvas.nativeCanvas) {
            val checkpoint = saveLayer(null, null)

            // Draw the content underneath
            drawContent()

            val centerX = (size.width / 2 - bitmap.width / 2).toInt() + 3
            val centerY = 0
            drawImage(
                image = bitmap,
                dstSize = IntSize(width = bitmap.width, height = bitmap.height),
                dstOffset = IntOffset(centerX, centerY),
                blendMode = androidx.compose.ui.graphics.BlendMode.DstOut
            )
            restoreToCount(checkpoint)
        }
    }
}

fun String.getInitials(count: Int = 2): String {
    if (this.isBlank()) return ""
    return this.split(" ")
        .filter { it.isNotEmpty() }
        .take(count)
        .joinToString("") { it[0].toString() }.uppercase()
}


fun String.generateIconSlug(): String {
    return "ic_" + this.trim().lowercase().replace(' ', '_')
}

fun Context.openWebLink(imageUri: String?) {
    if (imageUri.orEmpty().isNotEmpty()) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(imageUri)
        }
        startActivity(intent)
    }
}

fun Context.sendEmail(to: String, subject: String, body: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }
    this.startActivity(Intent.createChooser(intent, "Choose an email app"))
}


fun Modifier.dynamicImePadding(paddingValues: PaddingValues): Modifier {
    return this.then(
        Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .navigationBarsPadding()
            .imePadding()
    )
}

fun Modifier.dynamicPadding(paddingValues: PaddingValues): Modifier {
    return this.then(
        Modifier
            .padding(top = paddingValues.calculateTopPadding())
            .navigationBarsPadding()
    )
}


fun String.getTransactionType(): String {
    return when (this) {
        "DEBIT" -> "Expense"
        "CREDIT" -> "Income"
        else -> ""
    }
}


fun <T> MutableList<T>.toggle(item: T) {
    if (contains(item)) {
        remove(item)
    } else {
        add(item)
    }
}


@Composable
fun Modifier.roundedBorder(radius: Dp = 16.dp, color: Color? = null): Modifier {
    return this.border(
        1.dp,
        color ?: MaterialTheme.extendedColors.primaryBorder,
        RoundedCornerShape(radius)
    )
}

@Composable
fun Modifier.roundedBorder(shape: Shape, color: Color? = null): Modifier {
    return this.border(
        1.dp,
        color ?: MaterialTheme.extendedColors.primaryBorder,
        shape
    )
}

// get first word
fun String.getFirstWord(): String {
    return this.split(" ").first()
}

fun NavHostController.getViewModelStoreOwner() = this.getViewModelStoreOwner(this.graph.id)
fun NavController.getViewModelStoreOwner() = this.getViewModelStoreOwner(this.graph.id)


fun String?.getCategoryType(): String {
    return when (this) {
        CategoryType.DEBIT.name -> CategoryType.DEBIT.displayName
        CategoryType.CREDIT.name -> CategoryType.CREDIT.displayName
        else -> CategoryType.BOTH.displayName
    }
}


fun String?.isValidUrl(): Boolean {
    if (this.isNullOrEmpty()) return false
    return Patterns.WEB_URL.matcher(this).matches()
}

fun String.firstLetterCap(): String {
    return this.lowercase(Locale.getDefault())
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }
}