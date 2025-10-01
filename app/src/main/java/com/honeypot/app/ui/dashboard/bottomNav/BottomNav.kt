package com.honeypot.app.ui.dashboard.bottomNav

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.honeypot.app.navigation.CreateTransactionScreenRoute
import com.honeypot.app.ui.acounts.viewModel.AccountViewModel
import com.honeypot.app.ui.categories.viewmodel.CategoryViewModel
import com.honeypot.app.ui.dashboard.bottomNav.components.ActionButton
import com.honeypot.app.ui.dashboard.bottomNav.components.BottomBarContent
import com.honeypot.app.ui.dashboard.bottomNav.components.ExpandableFab
import com.honeypot.app.ui.dashboard.budget.BudgetScreen
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetViewModel
import com.honeypot.app.ui.dashboard.home.HomeScreen
import com.honeypot.app.ui.dashboard.home.viewModel.HomeViewModel
import com.honeypot.app.ui.dashboard.profile.ProfileScreen
import com.honeypot.app.ui.dashboard.transactions.TransactionScreen
import com.honeypot.app.ui.dashboard.transactions.viewmodel.TransactionViewModel
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.NavigationRequestKeys
import com.honeypot.app.utils.getViewModelStoreOwner


@Composable
fun BottomNav(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    accountViewModel: AccountViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel(navController.getViewModelStoreOwner()),
    budgetViewModel: BudgetViewModel = hiltViewModel(navController.getViewModelStoreOwner()),
    homeViewModel: HomeViewModel = hiltViewModel(navController.getViewModelStoreOwner())
) {
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var isFabExpanded by rememberSaveable { mutableStateOf(false) }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    DisposableEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<Boolean>(NavigationRequestKeys.REFRESH_TRANSACTION)
            ?.observeForever { result ->
                if (result == true) {
                    transactionViewModel.closeDialog()
                    transactionViewModel.refreshTransaction()
                    accountViewModel.getAllAccounts()
                    budgetViewModel.refreshBudgets()
                    transactionViewModel.closeDialog()
                    homeViewModel.getAcSummary()
                    homeViewModel.getCategoryInsights()
//                    savedStateHandle.remove<Boolean>(NavigationRequestKeys.REFRESH_TRANSACTION)
                    savedStateHandle[NavigationRequestKeys.REFRESH_TRANSACTION] = false
                }
            }
        onDispose {
            isFabExpanded = false
        }
    }

    val items = remember {
        listOf(
            BottomNavItem.Home,
            BottomNavItem.Transaction,
            BottomNavItem.Action,
            BottomNavItem.Budget,
            BottomNavItem.Profile
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        topBar = {
//            key(currentIndex) {
//                if (currentIndex != 0) {
//                    AppBar(
//                        heading = items[currentIndex].label,
//                        navController = navController,
//                        elevation = false,
//                        actions = {
////                            if (items.lastIndex == currentIndex) {
////                                ThemeToggle()
////                            }
//                        },
//                        isBackNavigation = false
//                    )
//                }
//            }
        },
        bottomBar = {
            BottomBar(
                items = items,
                isFabExpended = isFabExpanded,
                currentIndex = currentIndex,
                onItemClick = { index ->
                    currentIndex = index
                    if (isFabExpanded) {
                        isFabExpanded = false
                    }
                },
                onFabClick = { isFabExpanded = !isFabExpanded }
            )
        },
        floatingActionButton = {
            ExpandableFab(
                isFabExpanded = isFabExpanded,
                onClick = { type ->
                    navController.navigate(
                        CreateTransactionScreenRoute(
                            transactionType = type
                        )
                    )
                    isFabExpanded = false
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        ContentView(
            paddingValue = paddingValues,
            isFabExpended = isFabExpanded,
            onFabChange = { isFabExpanded = it },
            currentItem = items[currentIndex],
            navController = navController,
            transactionViewModel = transactionViewModel
        )
    }
}

@Composable
fun BottomBar(
    items: List<BottomNavItem>,
    currentIndex: Int,
    isFabExpended: Boolean,
    onItemClick: (Int) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.extendedColors.bottomNavBg)
            .wrapContentHeight(),
    ) {
        BottomBarContent(
            items = items,
            currentIndex = currentIndex,
            onItemClick = onItemClick,
        )

        ActionButton(
            isFabExpended = isFabExpended,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 0.dp, y = (-8).dp),
            onFabClicked = onFabClick
        )
    }
}


@Composable
private fun ContentView(
    paddingValue: PaddingValues,
    isFabExpended: Boolean,
    onFabChange: (Boolean) -> Unit,
    currentItem: BottomNavItem,
    navController: NavHostController,
    transactionViewModel: TransactionViewModel
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .padding(paddingValue)
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.extendedColors.bottomNavBg
                    ),
                )
            )
            .pointerInput(isFabExpended) {
                detectTapGestures {
                    if (isFabExpended) {
                        onFabChange(false)
                    }
                }
            }
    ) {
        AnimatedContent(
            targetState = currentItem,
            transitionSpec = {
                fadeIn(tween(200)) + slideInVertically(
                    initialOffsetY = { -it / 10 },
                    animationSpec = tween(200)
                ) togetherWith fadeOut(tween(200))
            }, label = ""
        ) { targetScreen ->
            when (targetScreen) {
                is BottomNavItem.Home -> HomeScreen(navController)
                is BottomNavItem.Transaction -> TransactionScreen(navController, transactionViewModel)
                is BottomNavItem.Budget -> BudgetScreen(navController)
                is BottomNavItem.Profile -> ProfileScreen(navController)
                is BottomNavItem.Action -> Unit
            }
        }
    }

}




