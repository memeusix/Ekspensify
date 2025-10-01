package com.honeypot.app.ui.dashboard.transactions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.honeypot.app.R
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.data.model.requestModel.TransactionRequestModel
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.data.model.responseModel.AttachmentResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.navigation.CreateTransactionScreenRoute
import com.honeypot.app.ui.dashboard.transactions.components.CreateTransactionFromOptions
import com.honeypot.app.ui.dashboard.transactions.viewmodel.TransactionViewModel
import com.honeypot.app.ui.theme.Green100
import com.honeypot.app.ui.theme.Red100
import com.honeypot.app.utils.NavigationRequestKeys
import com.honeypot.app.utils.TransactionType
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.formatRupees
import com.honeypot.app.utils.fromJson
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun CreateTransactionScreen(
    navController: NavHostController,
    args: CreateTransactionScreenRoute,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    val transactionArgs =
        remember(args.transactionResponseModelArgs) { args.transactionResponseModelArgs.fromJson<TransactionResponseModel>() }
    val isUpdate = rememberUpdatedState(transactionArgs != null && !args.isPending)

    DisposableEffect(Unit) {
        val defaultDarkIcons = systemUiController.systemBarsDarkContentEnabled
        systemUiController.apply {
            setStatusBarColor(Color.Transparent, darkIcons = false)
        }
        onDispose {
            systemUiController.apply {
                setStatusBarColor(color = Color.Transparent, darkIcons = defaultDarkIcons)
            }
        }
    }

    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    val transactionType = remember { args.transactionType }
    val bgColor = when (transactionType) {
        TransactionType.CREDIT -> Green100
        TransactionType.DEBIT -> Red100
    }

    val amountState = remember { mutableStateOf(TextFieldStateModel()) }
    val noteState = remember { mutableStateOf(TextFieldStateModel()) }
    val selectedCategory = remember { mutableStateOf(CategoryResponseModel()) }
    val selectedAccount = remember { mutableStateOf(AccountResponseModel()) }
    val selectedAttachment = remember {
        mutableStateOf(AttachmentResponseModel())
    }

    val createTransaction by transactionViewModel.createTransaction.collectAsStateWithLifecycle()
    val updateTransaction by transactionViewModel.updateTransaction.collectAsStateWithLifecycle()
    val isLoading = createTransaction is ApiResponse.Loading

    LaunchedEffect(createTransaction, updateTransaction) {
        // handle create transaction response
        handleApiResponse(
            response = createTransaction,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavigationRequestKeys.REFRESH_TRANSACTION, true
                    )
                    navController.popBackStack()

                    if (args.isPending) {
                        transactionViewModel.deletePendingTransaction(transactionArgs?.pendingId!!)
                    }
                }
            }
        )
        handleApiResponse(
            response = updateTransaction,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        NavigationRequestKeys.REFRESH_TRANSACTION, true
                    )
                    navController.popBackStack()
                }
            }
        )
    }

    LaunchedEffect(transactionArgs) {
        transactionArgs?.let { transaction ->
            amountState.value = amountState.value.copy(text = transaction.amount.toString())
            noteState.value = noteState.value.copy(text = transaction.note.orEmpty())
            selectedCategory.value = transaction.category ?: CategoryResponseModel()
            selectedAccount.value = transaction.account ?: AccountResponseModel()
            selectedAttachment.value = selectedAttachment.value.copy(path = transaction.attachment)
        }
    }

    val linearGradient = Brush.linearGradient(
        listOf(
            bgColor,
            bgColor,
            bgColor,
            MaterialTheme.colorScheme.background
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(linearGradient),
//        containerColor = bgColor,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        topBar = {
            AppBar(
                heading = stringResource(R.string.add_transaction),
                navController = navController,
                isLightColor = true,
                bgColor = bgColor
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .background(linearGradient)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.weight(1f))
                AmountDisplayView(amountState)
                CreateTransactionFromOptions(
                    isUpdate = isUpdate.value,
                    amountState = amountState,
                    noteState = noteState,
                    selectedCategory = selectedCategory,
                    selectedAccount = selectedAccount,
                    selectedAttachment = selectedAttachment,
                    transactionViewModel = transactionViewModel,
                    toastState = toastState,
                    type = transactionType,
                    navController = navController,
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 30.dp, start = 20.dp, end = 20.dp, bottom = 50.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
            ) {
                FilledButton(
                    text = if (isUpdate.value) {
                        stringResource(R.string.update)
                    } else {
                        stringResource(R.string.add)
                    },
                    textModifier = Modifier.padding(vertical = 17.dp),
                    enabled = (amountState.value.text.trim()
                        .isNotEmpty() && amountState.value.text.trim()
                        .toDouble() != 0.0) && selectedAccount.value.id != null && selectedCategory.value.id != null,
                    onClick = {
                        val transactionRequestModel = TransactionRequestModel(
                            amount = amountState.value.text.trim().toBigDecimal(),
                            note = noteState.value.text.trim(),
                            accountId = selectedAccount.value.id,
                            categoryId = selectedCategory.value.id,
                            attachmentId = selectedAttachment.value.attachmentId,
                            type = args.transactionType.toString()
                        )
                        if (isUpdate.value && transactionArgs?.id != null) {
                            transactionViewModel.updateTransaction(
                                transactionArgs.id!!,
                                transactionRequestModel
                            )
                        } else {
                            transactionViewModel.createTransaction(transactionRequestModel)
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .padding(bottom = 15.dp)
                )
            }
        }
        //Show Loader
        ShowLoader(isLoading)
    }
}

@Composable
private fun AmountDisplayView(amountState: MutableState<TextFieldStateModel>) {
    var textSize by remember { mutableStateOf(50.sp) }
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            stringResource(R.string.how_much), style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
            )
        )
        Text(
            amountState.value.text.ifEmpty { "0" }.toBigDecimal().formatRupees(),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = textSize),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { textLayoutResult ->
                val currentMaxLinesIndex = textLayoutResult.lineCount - 1
                if (textLayoutResult.isLineEllipsized(currentMaxLinesIndex)) {
                    textSize *= 0.9
                }
            },
        )
    }
}