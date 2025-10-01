package com.honeypot.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.honeypot.app.data.model.requestModel.AuthRequestModel
import com.honeypot.app.navigation.viewmodel.NavigationViewModel
import com.honeypot.app.navigation.viewmodel.NotificationEventModel
import com.honeypot.app.ui.SplashScreen
import com.honeypot.app.ui.acounts.AccountsScreen
import com.honeypot.app.ui.acounts.CreateAccountScreen
import com.honeypot.app.ui.auth.IntroScreen
import com.honeypot.app.ui.auth.LoginScreen
import com.honeypot.app.ui.auth.OtpVerificationScreen
import com.honeypot.app.ui.auth.RegisterScreen
import com.honeypot.app.ui.autoTracking.AutoTrackingScreen
import com.honeypot.app.ui.autoTracking.PendingTransactionScreen
import com.honeypot.app.ui.categories.CategoriesScreen
import com.honeypot.app.ui.categories.CreateCategoryScreen
import com.honeypot.app.ui.dashboard.bottomNav.BottomNav
import com.honeypot.app.ui.dashboard.budget.BudgetDetailsScreen
import com.honeypot.app.ui.dashboard.budget.BudgetTransactionScreen
import com.honeypot.app.ui.dashboard.budget.CreateBudgetScreen
import com.honeypot.app.ui.dashboard.profile.AboutScreen
import com.honeypot.app.ui.dashboard.transactions.CreateTransactionScreen
import com.honeypot.app.ui.dashboard.transactions.FilterScreen
import com.honeypot.app.ui.export.ExportScreen
import com.honeypot.app.ui.picture.PicturePreviewScreen
import com.honeypot.app.utils.NotificationActivity


@Composable
fun NavGraph(
    navController: NavHostController,
    navigationViewModel: NavigationViewModel,
    modifier: Modifier,
    callBackNav: (NavDestination?) -> Unit
) {
    val notificationEventState by navigationViewModel.notificationEvent.collectAsStateWithLifecycle()
    var isNavHostReady by remember { mutableStateOf(false) }

    LaunchedEffect(notificationEventState, isNavHostReady) {
        if (isNavHostReady) {
            handleNotificationClickNavigation(navController, notificationEventState)
        }
    }
    LaunchedEffect(navController.currentDestination) {
        callBackNav(navController.currentDestination)
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashScreenRoute,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(250)) +
                    fadeIn(tween(250))
        },
        exitTransition = {
            fadeOut(tween(250))
        },
        popEnterTransition = {
            fadeIn(tween(250))
        },
        contentAlignment = Alignment.Center,
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(250))+
                    fadeOut(tween(250))
        },
    ) {
        composable<SplashScreenRoute> {
            SplashScreen(navController)
        }
        composable<IntroScreenRoute> {
            IntroScreen(navController)
        }
        composable<LoginScreenRoute> {
            LoginScreen(navController = navController)
        }
        composable<RegisterScreenRoute> {
            RegisterScreen(navController = navController)
        }
        composable<OtpVerificationScreenRoute> {
            val args = it.toRoute<OtpVerificationScreenRoute>()
            val authRequestModel = AuthRequestModel(
                name = args.name,
                email = args.email
            )
            OtpVerificationScreen(
                navController = navController,
                navArgs = authRequestModel,
            )
        }


        // Bottom Nav Screen
        composable<BottomNavRoute> {
            BottomNav(navController = navController)
            isNavHostReady = true
        }


        // Account Screens
        composable<AccountScreenRoute>(
            deepLinks = listOf(navDeepLink { uriPattern = "budget://account" })
        ) {
            val args = it.toRoute<AccountScreenRoute>()
            AccountsScreen(navController, args)
        }
        composable<CreateAccountScreenRoute> {
            val args = it.toRoute<CreateAccountScreenRoute>()
            CreateAccountScreen(
                navController,
                args
            )
        }

        // Categories Screens
        composable<CategoriesScreenRoute> {
            CategoriesScreen(navController)
        }

        composable<CreateCategoryScreenRoute> {
            CreateCategoryScreen(
                navController = navController
            )
        }

        // Auto Tracking Screen
        composable<AutoTrackingScreenRoute> {
            AutoTrackingScreen(navController)
        }

        composable<PendingTransactionRoute> {
            PendingTransactionScreen(navController)
        }

        // Transaction Screens
        composable<CreateTransactionScreenRoute> {
            val args = it.toRoute<CreateTransactionScreenRoute>()
            CreateTransactionScreen(navController, args)
        }

        composable<FilterScreenRoute> {
            FilterScreen(navController)
        }

        // Budget Screens
        composable<CreateBudgetScreenRoute> {
            CreateBudgetScreen(navController)
        }
        composable<BudgetDetailsScreenRoute> {
            val args = it.toRoute<BudgetDetailsScreenRoute>()
            BudgetDetailsScreen(navController, args)
        }
        composable<BudgetTransactionScreenRoute> {
            val args = it.toRoute<BudgetTransactionScreenRoute>()
            BudgetTransactionScreen(navController, args)
        }

        // Image Preview Screen
        composable<PicturePreviewScreenRoute> {
            val args = it.toRoute<PicturePreviewScreenRoute>()
            PicturePreviewScreen(args)
        }

        // Export Screen
        composable<ExportScreenRoute> {
            ExportScreen(navController)
        }

        // About Screen
        composable<AboutScreenRoute> {
            AboutScreen(navController)
        }

    }
}


fun handleNotificationClickNavigation(
    navController: NavHostController,
    notificationEventState: NotificationEventModel?
) {

    notificationEventState?.additionalData?.nameValuePairs?.let {
        when (it.activity) {
            NotificationActivity.BUDGET -> {
                // Navigate using deep links
                if (it.id?.isDigitsOnly() == true) {
                    navController.navigate(
                        BudgetDetailsScreenRoute(
                            budgetId = it.id.toInt()
                        )
                    )
                }
            }

            NotificationActivity.TRANSACTION -> {

            }

            else -> Unit
        }
    }
}
