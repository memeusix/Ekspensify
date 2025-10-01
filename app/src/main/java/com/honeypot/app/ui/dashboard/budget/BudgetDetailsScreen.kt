package com.honeypot.app.ui.dashboard.budget

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.honeypot.app.R
import com.honeypot.app.components.AlertDialog
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.EmptyView
import com.honeypot.app.components.PagingListLoader
import com.honeypot.app.components.PullToRefreshLayout
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.responseModel.BudgetReportResponseModel
import com.honeypot.app.data.model.responseModel.BudgetResponseModel
import com.honeypot.app.navigation.BudgetDetailsScreenRoute
import com.honeypot.app.navigation.BudgetTransactionScreenRoute
import com.honeypot.app.ui.dashboard.budget.bottomsheets.SelectAccountBottomSheet
import com.honeypot.app.ui.dashboard.budget.bottomsheets.SelectCategoryBottomSheet
import com.honeypot.app.ui.dashboard.budget.components.BudgetItem
import com.honeypot.app.ui.dashboard.budget.components.BudgetOptionsMenu
import com.honeypot.app.ui.dashboard.budget.components.BudgetReportItem
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetViewModel
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.BottomSheetSelectionType
import com.honeypot.app.utils.BudgetStatus
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.getViewModelStoreOwner
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.handleApiResponseWithError
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel

@Composable
fun BudgetDetailsScreen(
    navController: NavHostController,
    args: BudgetDetailsScreenRoute,
    budgetViewModel: BudgetViewModel = hiltViewModel(navController.getViewModelStoreOwner()),
    budgetViewModelNewInstance: BudgetViewModel = hiltViewModel()
) {
    // API STATES
    val budgetDeleteState by budgetViewModelNewInstance.deleteBudget.collectAsStateWithLifecycle()
    val closeBudgetState by budgetViewModelNewInstance.updateBudget.collectAsStateWithLifecycle()

    // loading from new instance to get fresh report each time
    val budgetReports = budgetViewModelNewInstance.getBudgetReport().collectAsLazyPagingItems()
    val budgetDetailsState by budgetViewModelNewInstance.budgetDetails.collectAsStateWithLifecycle()

    val isLoading =
        budgetDeleteState is ApiResponse.Loading || budgetDetailsState is ApiResponse.Loading || closeBudgetState is ApiResponse.Loading

    // this is to stop calling from pop-back
    var isApiCalled by rememberSaveable { mutableStateOf(false) }

    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    // get budget details
    LaunchedEffect(args.budgetId) {
        if (!isApiCalled) {
            budgetViewModelNewInstance.getBudgetDetails(args.budgetId)
            isApiCalled = true
        }
    }
    // handling api response
    LaunchedEffect(budgetDeleteState, budgetDetailsState) {
        budgetViewModelNewInstance.setReportId(null)
        handleApiResponse(
            response = budgetDeleteState,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    budgetViewModel.refreshBudgets()
                    navController.popBackStack()
                }
            },
        )
        handleApiResponseWithError(response = budgetDetailsState,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    budgetViewModelNewInstance.setBudgetId(data.id)
                }
            },
            onFailure = { error ->
                budgetViewModelNewInstance.setBudgetId(null)
            })
        handleApiResponse(response = closeBudgetState,
            toastState = toastState,
            navController = navController,
            onSuccess = { data -> budgetViewModel.refreshBudgets() })
    }

    // Category Bottom Sheet
    val showCategoryBottomSheet = remember { mutableStateOf(false) }
    if (showCategoryBottomSheet.value) {
        SelectCategoryBottomSheet(categoryList = budgetDetailsState.data?.categories.orEmpty(),
            selectionType = BottomSheetSelectionType.MULTIPLE,
            onDismiss = { showCategoryBottomSheet.value = false },
            onClick = {})
    }

    // Account Bottom Sheet
    val showAccountBottomSheet = remember { mutableStateOf(false) }
    if (showAccountBottomSheet.value) {
        SelectAccountBottomSheet(accountList = budgetDetailsState.data?.accounts.orEmpty(),
            selectionType = BottomSheetSelectionType.MULTIPLE,
            onDismiss = { showAccountBottomSheet.value = false },
            onClick = {})
    }

    // delete dialog
    var isDialogOpen by remember { mutableStateOf(false) }
    var isDelete by remember { mutableStateOf(false) }

    if (isDialogOpen) {
        ConfirmDialogue(isDelete = isDelete, onClick = singleClick {
            isDialogOpen = false
            if (isDelete) budgetViewModelNewInstance.deleteBudget(args.budgetId)
            else budgetViewModelNewInstance.closeBudget(args.budgetId)
        }, onDismiss = {
            isDialogOpen = false
        })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
        AppBar(
            heading = stringResource(R.string.budget_details, args.budgetId),
            navController = navController,
            actions = {
                BudgetOptionsMenu(
                    isClosed = budgetDetailsState.data?.status == BudgetStatus.CLOSED.name,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    onCloseClick = {
                        isDelete = false
                        isDialogOpen = true
                    },
                    onDeleteClick = {
                        isDelete = true
                        isDialogOpen = true
                    })
            })
    }) { paddingValues ->
        PullToRefreshLayout(
            onRefresh = {
                budgetViewModelNewInstance.getBudgetDetails(args.budgetId)
                budgetReports.refresh()
            }, modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                budgetDetailsState.data?.let {
                    BudgetItem(
                        budget = it.copy(isDetailsPage = true),
                        onCategoryClick = singleClick {
                            showCategoryBottomSheet.value = true
                        },
                        onAccountClick = singleClick {
                            showAccountBottomSheet.value = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                    )
                } ?: run {
                    if (!isLoading) {
                        EmptyView(
                            title = stringResource(R.string.no_budget_available),
                            description = stringResource(R.string.there_are_no_records_for_this_budget)
                        )
                    }
                }
                BudgetReportListView(
                    budgetDetailsState, budgetReports, navController, isLoading
                )
            }
        }

        // show loader
        ShowLoader(isLoading)
    }
}


@Composable
private fun ColumnScope.BudgetReportListView(
    budgetDetailsState: ApiResponse<BudgetResponseModel>,
    budgetReports: LazyPagingItems<BudgetReportResponseModel>,
    navController: NavHostController,
    isLoading: Boolean
) {
    LazyColumn(
        userScrollEnabled = true,
        flingBehavior = ScrollableDefaults.flingBehavior(),
        modifier = Modifier.Companion.weight(1f)
    ) {
        if (budgetDetailsState.data != null) {
            items(budgetReports.itemCount, key = budgetReports.itemKey { it.id!! }) { index ->
                val report = budgetReports[index]
                BudgetReportItem(report = report,
                    budgetDetails = budgetDetailsState.data,
                    onClick = singleClick {
                        if (budgetDetailsState.data?.id != null && report?.id != null) {
                            navController.navigate(
                                BudgetTransactionScreenRoute(
                                    budgetId = budgetDetailsState.data?.id!!,
                                    budgetReportId = report.id!!
                                )
                            )
                        }
                    })

                HorizontalDivider(color = MaterialTheme.extendedColors.primaryBorder)
            }
        }
        budgetReports.apply {
            item {
                if (!isLoading && budgetDetailsState.data != null) {
                    PagingListLoader(loadState)
                }
            }
        }
    }
}


@Composable
private fun ConfirmDialogue(
    isDelete: Boolean, onClick: () -> Unit, onDismiss: () -> Unit
) {
    val dialogTitle = stringResource(
        if (isDelete) R.string.confirm_delete else R.string.confirm_close
    )
    val dialogMessage = stringResource(
        R.string.are_you_sure_you_want_to_delete_this_budget, if (isDelete) "delete" else "close"
    )
    val buttonText =
        if (isDelete) stringResource(R.string.delete) else stringResource(R.string.close)

    AlertDialog(
        title = dialogTitle,
        message = dialogMessage,
        btnText = buttonText,
        onDismiss = onDismiss,
        onConfirm = onClick
    )
}