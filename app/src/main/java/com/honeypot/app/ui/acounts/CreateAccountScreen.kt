package com.honeypot.app.ui.acounts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.honeypot.app.R
import com.honeypot.app.components.AlertDialog
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.CustomOutlineTextField
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.components.TextFieldRupeePrefix
import com.honeypot.app.components.TextFieldType
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.data.model.requestModel.AccountRequestModel
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.navigation.BottomNavRoute
import com.honeypot.app.navigation.CreateAccountScreenRoute
import com.honeypot.app.ui.acounts.components.AccountTypeGridItem
import com.honeypot.app.ui.acounts.components.AccountsCardView
import com.honeypot.app.ui.acounts.components.AmountText
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.acounts.viewModel.AccountViewModel
import com.honeypot.app.ui.acounts.viewModel.CreateAccountState
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.AccountType
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.fromJson
import com.honeypot.app.utils.getViewModelStoreOwner
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel

@Composable
fun CreateAccountScreen(
    navController: NavHostController,
    args: CreateAccountScreenRoute?,
    viewModel: AccountViewModel = hiltViewModel(navController.getViewModelStoreOwner()),
    createAccountState: CreateAccountState = hiltViewModel(),
) {

    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    val context = LocalContext.current


    val balanceState = remember { mutableStateOf(TextFieldStateModel()) }

    var deleteDialogState by remember { mutableStateOf(false) }
    var updateDialogState by remember { mutableStateOf(false) }

    val selectedAccountType = remember { mutableStateOf(AccountType.BANK) }

    val bankList = createAccountState.bankList.collectAsState()
    val walletList = createAccountState.walletList.collectAsState()
    val displayList = remember {
        derivedStateOf {
            if (selectedAccountType.value == AccountType.BANK) {
                bankList.value
            } else {
                walletList.value
            }
        }
    }

//    val selectedBank = remember { mutableStateOf<BankModel?>(null) }
//    val selectedWallet = remember { mutableStateOf<BankModel?>(null) }

    val selectedBank = createAccountState.selectedBank.collectAsState()
    val selectedWallet = createAccountState.selectedWallet.collectAsState()

    // navArgs
    val accountLists = remember { args?.accountList.fromJson<List<AccountResponseModel>>() }
    val argsAccountDetails = remember { args?.accountResponseArgs.fromJson<AccountResponseModel>() }

    // Api States
    val createAccountResponse by viewModel.createAccount.collectAsState()
    val updateAccountResponse by viewModel.updateAccount.collectAsState()
    val deleteAccountResponse by viewModel.deleteAccount.collectAsState()
    val isLoading =
        createAccountResponse is ApiResponse.Loading
                || updateAccountResponse is ApiResponse.Loading
                || deleteAccountResponse is ApiResponse.Loading


    // Initial Data Set
    LaunchedEffect(Unit) {
        createAccountState.initialize(
            accountLists, argsAccountDetails
        )
        argsAccountDetails?.let { details ->
            println(details)
            balanceState.value = balanceState.value.copy(
                text = (details.balance ?: 0.0).toString()
            )
            selectedAccountType.value = AccountType.valueOf(details.type ?: "BANK")
        }
    }


    // Api Response Handling
    LaunchedEffect(createAccountResponse, updateAccountResponse, deleteAccountResponse) {
        // Handle createAccount Result
        handleApiResponse(response = createAccountResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    if (args?.isFromProfile == true) {
                        goBackToList(viewModel, navController)
                    } else {
                        navController.navigate(
                            BottomNavRoute
                        ) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            })

        // Handle Update Account Response
        handleApiResponse(response = updateAccountResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    goBackToList(viewModel, navController)
                }
            })

        // Handle Delete Account Response
        handleApiResponse(response = deleteAccountResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                toastState.value = CustomToastModel(
                    message = context.getString(R.string.account_deleted_successfully),
                    isVisible = true
                )
                goBackToList(viewModel, navController)
            })
    }

    // DeleteDialog
    if (deleteDialogState && argsAccountDetails?.id != null) {
        AlertDialog(
            title = stringResource(R.string.are_you_sure),
            message = stringResource(R.string.you_want_delete_this_account),
            btnText = context.getString(R.string.delete),
            onDismiss = {
                deleteDialogState = false
            },
            onConfirm = {
                viewModel.deleteAccount(argsAccountDetails.id!!)
                deleteDialogState = false
            }
        )
    }
    // Update Dialog
    if (updateDialogState && argsAccountDetails?.id != null) {
        AlertDialog(
            title = stringResource(R.string.are_you_sure),
            message = "you want update this Account",
            btnText = context.getString(R.string.update),
            onDismiss = {
                updateDialogState = false
            },
            onConfirm = {
                val accountRequestModel = AccountRequestModel(
                    name = when (selectedAccountType.value) {
                        AccountType.WALLET -> selectedWallet.value?.name
                        AccountType.BANK -> selectedBank.value?.name
                    },
                    type = selectedAccountType.value.toString(),
                    balance = balanceState.value.text.ifEmpty { "0" }.toBigDecimal(),
                )
                viewModel.updateAccount(argsAccountDetails.id!!, accountRequestModel)
                updateDialogState = false
            }
        )
    }

    // Main Ui
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                heading = stringResource(R.string.add_account),
                navController = navController,
                isBackNavigation = true,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                VerticalSpace(20.dp)
                BalanceView(
                    balanceState,
                    selectedAccount = when (selectedAccountType.value) {
                        AccountType.BANK -> {
                            selectedBank.value
                        }

                        AccountType.WALLET -> {
                            selectedWallet.value
                        }
                    },
                    showDelete = argsAccountDetails != null && (accountLists?.size ?: 0) > 1,
                    onDeleteClick = {
                        deleteDialogState = !deleteDialogState
                    }
                )
                VerticalSpace(20.dp)
                CustomOutlineTextField(
                    state = balanceState,
                    placeholder = stringResource(R.string.balance),
                    isExpendable = false,
                    maxLength = 10,
                    isNegativeAllow = true,
                    prefix = { TextFieldRupeePrefix() },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    type = TextFieldType.NUMBER
                )
                VerticalSpace(16.dp)
                AccountTypeCard(
                    selectedAccountType,
                    displayList.value,
                    selectedWallet.value,
                    selectedBank.value,
                    onItemSelected = { item ->
                        if (selectedAccountType.value == AccountType.WALLET) {
                            createAccountState.updateSelectedWallet(item)
                        } else {
                            createAccountState.updateSelectedBank(item)
                        }
                    }
                )
                Text(
                    stringResource(R.string.no_account_message),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 20.dp),
                )
                VerticalSpace(60.dp)
                Spacer(Modifier.weight(4f))
            }
            SaveBtn(
                text = if (argsAccountDetails == null) {
                    if (args?.isFromProfile == true) {
                        stringResource(R.string.add)
                    } else {
                        stringResource(R.string.continue_)
                    }
                } else {
                    stringResource(R.string.update)
                },
                modifier = Modifier
                    .padding(bottom = 20.dp),
                isEnable = when (selectedAccountType.value) {
                    AccountType.WALLET -> selectedWallet.value != null
                    else -> selectedBank.value != null
                },
                onClick = singleClick {
                    val accountRequestModel = AccountRequestModel(
                        name = when (selectedAccountType.value) {
                            AccountType.WALLET -> selectedWallet.value?.name
                            AccountType.BANK -> selectedBank.value?.name
                        },
                        type = selectedAccountType.value.toString(),
                        balance = balanceState.value.text.ifEmpty { "0" }.toBigDecimal(),
                    )
                    argsAccountDetails?.let {
                        updateDialogState = true
//                        viewModel.updateAccount(it.id!!, accountRequestModel)
                    } ?: run {
                        viewModel.createAccount(accountRequestModel)
                    }
                },
            )
        }
        // showLoader
        ShowLoader(isLoading)
    }
}

@Composable
fun BalanceView(
    balanceState: MutableState<TextFieldStateModel>,
    selectedAccount: BankModel?,
    showDelete: Boolean,
    onDeleteClick: () -> Unit
) {
    AmountText(balanceState.value.text, Modifier)
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        selectedAccount?.icon?.let {
            Image(
                painterResource(it), contentDescription = null, modifier = Modifier.fillMaxHeight()
            )
        }
        Text(
            text = selectedAccount?.name ?: stringResource(R.string.no_account_selected),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (showDelete) {
            Icon(
                painterResource(R.drawable.ic_delete),
                tint = MaterialTheme.extendedColors.iconColor,
                contentDescription = null,
                modifier = Modifier.clickable { onDeleteClick() })
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AccountTypeCard(
    selectedAccountType: MutableState<AccountType>,
    displayList: List<BankModel>,
    selectedWallet: BankModel?,
    selectedBank: BankModel?,
    onItemSelected: (BankModel) -> Unit
) {

    AccountsCardView(
        selectedAccountType = selectedAccountType.value,
        onTypeChange = { selectedAccountType.value = it }
    ) {
        val size = remember { mutableStateOf(IntSize.Zero) }
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size.value = it }
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {

            val itemsPerRow = if (size.value.width == 0) 1 else (size.value.width / (70 * 3))
            val totalItems = displayList.size
            val remainder = totalItems % itemsPerRow


            displayList.forEach { item ->
                val isSelected =
                    item.iconSlug == selectedWallet?.iconSlug || item.iconSlug == selectedBank?.iconSlug
                item.isSelected = isSelected
                AccountTypeGridItem(
                    item = item,
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .requiredWidthIn(min = 65.dp, max = 70.dp),
                    onClick = { onItemSelected(item) },
                )
            }
            // Add placeholders to align the last row
            if (remainder != 0) {
                val placeholders = itemsPerRow.minus(remainder)
                repeat(placeholders) {
                    Spacer(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .requiredWidthIn(min = 65.dp, max = 70.dp),
                    )
                }
            }
        }
    }
}


@Composable
fun SaveBtn(text: String, modifier: Modifier, isEnable: Boolean, onClick: () -> Unit) {
    FilledButton(
        text = text,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        textModifier = Modifier.padding(vertical = 17.dp),
        modifier = modifier,
        enabled = isEnable
    )
}

private fun goBackToList(
    viewModel: AccountViewModel,
    navController: NavHostController
) {
    viewModel.getAllAccounts()
    navController.popBackStack()
}

