package com.honeypot.app.navigation

import com.honeypot.app.utils.TransactionType
import kotlinx.serialization.Serializable


@Serializable
object SplashScreenRoute

/**
 * Auth Screens Routes
 */

@Serializable
object IntroScreenRoute

@Serializable
object LoginScreenRoute


@Serializable
object RegisterScreenRoute

@Serializable
data class OtpVerificationScreenRoute(
    val name: String?,
    val email: String?
)


/**
 * DashBoard Routes
 */

@Serializable
object BottomNavRoute

@Serializable
object HomeScreenRoute

@Serializable
object TransactionsScreenRoute

@Serializable
object BudgetsScreenRoute

@Serializable
object ProfileScreenRoute

@Serializable
object AboutScreenRoute


/**
 * Account Screens
 */

@Serializable
data class AccountScreenRoute(
    val isFromProfile: Boolean = false
)

@Serializable
data class CreateAccountScreenRoute(
    val accountResponseArgs: String? = null,
    val accountList: String? = null,
    val isFromProfile: Boolean = false,
)


// Categories Screen
@Serializable
object CategoriesScreenRoute

@Serializable
data class CreateCategoryScreenRoute(
    val categoryResponseModelArgs: String? = null
)

// Transaction Screens
@Serializable
data class CreateTransactionScreenRoute(
    val isPending: Boolean = false,
    val transactionType: TransactionType,
    val transactionResponseModelArgs: String? = null
)

@Serializable
object FilterScreenRoute


@Serializable
data class ImageWebViewRoute(
    val imageUrl: String
)


// Budget Screens
@Serializable
object CreateBudgetScreenRoute

@Serializable
data class BudgetDetailsScreenRoute(
    val budgetId: Int,
)

@Serializable
data class BudgetTransactionScreenRoute(
    val budgetId: Int,
    val budgetReportId: Int
)

// Image preview page
@Serializable
data class PicturePreviewScreenRoute(
    val image: String
)

// Auto Tracking Screen
@Serializable
object AutoTrackingScreenRoute

@Serializable
object PendingTransactionRoute

// Export Screen
@Serializable
object ExportScreenRoute
