package com.honeypot.app.ui.autoTracking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.components.EmptyView
import com.honeypot.app.components.PagingListLoader
import com.honeypot.app.components.PullToRefreshLayout
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.navigation.CreateTransactionScreenRoute
import com.honeypot.app.room.model.PendingTransactionModel
import com.honeypot.app.ui.autoTracking.viewModel.PendingTransactionViewModel
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Red100
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.TransactionType
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.formatLocalDateTime
import com.honeypot.app.utils.formatRupees
import com.honeypot.app.utils.roundedBorder
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toJson

@Composable
fun PendingTransactionScreen(
    navController: NavHostController,
    pendingTransactionViewModel: PendingTransactionViewModel = hiltViewModel()
) {
    val pendingTransactions = pendingTransactionViewModel.getPendingTransactions()
        .collectAsLazyPagingItems()

    var previousSize by remember { mutableIntStateOf(pendingTransactions.itemCount) }

    val lazyState = rememberLazyListState()

    LaunchedEffect(pendingTransactions.itemCount) {
        if (pendingTransactions.itemCount > previousSize) {
            lazyState.animateScrollToItem(0)
        }
        previousSize = pendingTransactions.itemCount
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                heading = stringResource(R.string.pending_transactions),
                navController
            )
        }
    ) { paddingValues ->
        PullToRefreshLayout(
            onRefresh = pendingTransactionViewModel::getPendingTransactions
        ) {
            if (pendingTransactions.itemCount == 0 && pendingTransactions.loadState.isIdle) {
                EmptyView(
                    title = stringResource(R.string.no_transaction_found),
                    description = stringResource(R.string.there_is_no_pending_transaction_found)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .dynamicImePadding(paddingValues),
                state = lazyState,
                flingBehavior = ScrollableDefaults.flingBehavior(),
                userScrollEnabled = true
            ) {
                items(
                    pendingTransactions.itemCount,
                    key = pendingTransactions.itemKey { it.id }
                ) { index ->
                    val transaction = pendingTransactions[index]
                    PendingTransactionItem(
                        transaction = transaction!!,
                        onDeleteClick = {
                            pendingTransactionViewModel.deletePendingTransaction(
                                transaction.id
                            )
                        },
                        onAddClick = singleClick {
                            val account =
                                pendingTransactionViewModel.getAccountModel(transaction.accountName)
                            navController.navigate(
                                CreateTransactionScreenRoute(
                                    isPending = true,
                                    transactionType = transaction.type!!,
                                    transactionResponseModelArgs = TransactionResponseModel(
                                        amount = transaction.amount?.toBigDecimal(),
                                        type = transaction.type.name,
                                        account = account,
                                        pendingId = transaction.id
                                    ).toJson()
                                )
                            )
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.extendedColors.primaryBorder)
                }
                pendingTransactions.apply {
                    item { PagingListLoader(loadState) }
                }
                item {
                    VerticalSpace(20.dp)
                }
            }
        }
    }
}

@Composable
private fun LazyItemScope.PendingTransactionItem(
    transaction: PendingTransactionModel,
    onDeleteClick: () -> Unit,
    onAddClick: () -> Unit
) {
    val isDebit = transaction.type == TransactionType.DEBIT
    val icon = if (isDebit) R.drawable.ic_expense_arrow else R.drawable.ic_income_arrow
    val color = if (isDebit) Red100 else Green100
    val amount = "${if (isDebit) "-" else "+"}${(transaction.amount ?: 0).formatRupees()}"

    CustomListItem(
        title = amount,
        subtitle = transaction.createdAt.formatLocalDateTime(),
        desc = transaction.description ?: "",
        modifier = Modifier
            .padding(20.dp)
            .animateItem(fadeInSpec = null, fadeOutSpec = null),
        titleStyle = MaterialTheme.typography.bodyLarge.copy(
            color = color
        ),
        leadingContent = {
            PendingTransactionIcon(icon, color)
        },
        enable = false,
        trailingContent = {
            PendingTransactionActionButton(
                onAddClick = onAddClick,
                onDeleteClick = onDeleteClick
            )
        }
    )

}

@Composable
private fun PendingTransactionActionButton(
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            stringResource(R.string.add),
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onAddClick)
                .padding(horizontal = 20.dp)
                .wrapContentHeight(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Icon(
            rememberAsyncImagePainter(R.drawable.ic_close),
            contentDescription = null,
            modifier = Modifier
                .roundedBorder(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onDeleteClick)
                .padding(10.dp)
                .size(18.dp)
        )
    }
}

@Composable
private fun PendingTransactionIcon(icon: Int, color: Color) {
    Icon(
        painter = rememberAsyncImagePainter(icon),
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.extendedColors.imageBg)
            .size(38.dp)
            .padding(5.dp)
    )
}