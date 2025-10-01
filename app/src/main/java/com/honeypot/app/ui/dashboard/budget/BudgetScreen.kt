package com.honeypot.app.ui.dashboard.budget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.honeypot.app.R
import com.honeypot.app.components.EmptyView
import com.honeypot.app.components.PagingListLoader
import com.honeypot.app.components.PullToRefreshLayout
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.navigation.BudgetDetailsScreenRoute
import com.honeypot.app.navigation.CreateBudgetScreenRoute
import com.honeypot.app.ui.dashboard.budget.components.BudgetFilterRow
import com.honeypot.app.ui.dashboard.budget.components.BudgetItem
import com.honeypot.app.ui.dashboard.budget.helper.isClosed
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetViewModel
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.getViewModelStoreOwner
import com.honeypot.app.utils.singleClick

@Composable
fun BudgetScreen(
    navController: NavHostController,
    budgetViewModel: BudgetViewModel = hiltViewModel(navController.getViewModelStoreOwner())
) {
    val lazyState = rememberLazyListState()
    val budgets = budgetViewModel.getBudgets().collectAsLazyPagingItems()
    val budgetMeta by budgetViewModel.budgetMeta.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BudgetFilterRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 20.dp, vertical = 5.dp)
                .padding(top = 15.dp),
            filterData = budgetMeta,
            onFilterClick = { filterOptions ->
                // TODO : this btn is disable so first remove enable = false
            },
            onCreateClick = singleClick {
                navController.navigate(CreateBudgetScreenRoute)
            })

        PullToRefreshLayout(
            onRefresh = budgets::refresh,
            modifier = Modifier.weight(1f)
        ) {
            if (budgets.itemCount == 0 && budgets.loadState.isIdle) {
                EmptyView(
                    title = stringResource(R.string.no_budgets_found),
                    description = stringResource(R.string.there_are_no_budgets_available_right_now_create_one_to_start_planning),
                )
            }

            LazyColumn(
                state = lazyState,
                flingBehavior = ScrollableDefaults.flingBehavior(),
                userScrollEnabled = true,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(budgets.itemCount, key = budgets.itemKey { it.id!! }) { index ->
                    val budget = budgets[index]
                    BudgetItem(
                        budget = budget,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = singleClick {
                                navController.navigate(BudgetDetailsScreenRoute(budget?.id!!))
                            })
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .alpha(
                                if (budget.isClosed()) 0.6f else 1f
                            ),
                    )
                    HorizontalDivider(color = MaterialTheme.extendedColors.primaryBorder)
                }

                budgets.apply {
                    item {
                        PagingListLoader(loadState)
                    }
                }
                item { VerticalSpace() }
            }
        }
    }
}

