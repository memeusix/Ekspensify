package com.honeypot.app.ui.dashboard.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.honeypot.app.R
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.CustomOutlineTextField
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.components.TextFieldRupeePrefix
import com.honeypot.app.components.TextFieldType
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.data.model.requestModel.BudgetRequestModel
import com.honeypot.app.ui.dashboard.budget.bottomsheets.HandleBottomSheets
import com.honeypot.app.ui.dashboard.budget.components.CreateBudgetSectionCard
import com.honeypot.app.ui.dashboard.budget.components.DateFieldWithIcon
import com.honeypot.app.ui.dashboard.budget.components.DropDownField
import com.honeypot.app.ui.dashboard.budget.components.RenewalSelectionItem
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetFormState
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetViewModel
import com.honeypot.app.utils.BudgetPeriod
import com.honeypot.app.utils.BudgetType
import com.honeypot.app.utils.DatePickerType
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.formatLocalDate
import com.honeypot.app.utils.getViewModelStoreOwner
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel
import java.math.BigDecimal

@Composable
fun CreateBudgetScreen(
    navController: NavHostController,
    createBudgetState: BudgetFormState = hiltViewModel(),
    budgetViewModel: BudgetViewModel = hiltViewModel(
        navController.getViewModelStoreOwner()
    )
) {

    // Form States
    val amountState = remember { mutableStateOf(TextFieldStateModel()) }
    val formState by createBudgetState.formState.collectAsState()
    val isValid = remember(formState) {
        formState.limit > BigDecimal.ZERO
                && formState.period.isNotEmpty()
                && formState.type.isNotEmpty()
                && formState.startDate.isNotEmpty()
                && (formState.type != BudgetType.EXPIRING.name || formState.endDate?.isNotEmpty() == true)
    }
    LaunchedEffect(amountState.value.text) {
        createBudgetState.updateBudgetAmount(amountState.value.text.ifEmpty { null })
    }


    // Bottom sheets
    val showCategoryBottomSheet = remember { mutableStateOf(false) }
    val showAccountBottomSheet = remember { mutableStateOf(false) }
    val showTimePeriodBottomSheet = remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    var datePickerType by remember { mutableStateOf(DatePickerType.START_DATE) }
    HandleBottomSheets(
        createBudgetState = createBudgetState,
        formState = formState,
        showCategoryBottomSheet = showCategoryBottomSheet,
        showAccountBottomSheet = showAccountBottomSheet,
        showDatePicker = showDatePicker,
        showTimePeriodBottomSheet = showTimePeriodBottomSheet,
        datePickerType = datePickerType
    )

    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    // handle Api Responses
    val createBudgetResponse by budgetViewModel.createBudget.collectAsStateWithLifecycle()
    LaunchedEffect(createBudgetResponse) {
        handleApiResponse(
            response = createBudgetResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.apply {
                    budgetViewModel.refreshBudgets()
                    navController.popBackStack()
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                heading = stringResource(R.string.create_budget),
                navController = navController,
                isBackNavigation = true
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                VerticalSpace()
                AmountSection(amountState, formState, onCategoryClick = {
                    showCategoryBottomSheet.value = true
                }, onAccountClick = {
                    showAccountBottomSheet.value = true
                })
                RenewalTypeSection(formState = formState, onBudgetTypeClick = { type ->
                    createBudgetState.updateType(type.name)
                }, onStartDateClick = {
                    datePickerType = DatePickerType.START_DATE
                    showDatePicker.value = true
                }, onEndDateClick = {
                    datePickerType = DatePickerType.END_DATE
                    showDatePicker.value = true
                })
                PeriodSection(
                    formState = formState,
                    onClick = { showTimePeriodBottomSheet.value = true })
                VerticalSpace()
            }
            FilledButton(
                text = stringResource(R.string.create_budget),
                textModifier = Modifier.padding(vertical = 17.dp),
                enabled = isValid,
                onClick = {
                    budgetViewModel.createBudget(formState)
                }
            )
        }

        ShowLoader(
            isLoading = createBudgetResponse is ApiResponse.Loading
        )
    }
}

@Composable
private fun AmountSection(
    amountState: MutableState<TextFieldStateModel>,
    formState: BudgetRequestModel,
    onCategoryClick: () -> Unit,
    onAccountClick: () -> Unit
) {
    val selectedCategoryText = remember(formState.categoryIds) {
        when (formState.categoryIds?.size ?: 0) {
            0 -> "All Categories"
            1 -> "1 Category Selected"
            else -> "${formState.categoryIds?.size} Categories Selected"
        }
    }

    val selectedAccountText = remember(formState.accountIds) {
        when (formState.accountIds?.size ?: 0) {
            0 -> "All Accounts"
            1 -> "1 Account Selected"
            else -> "${formState.accountIds?.size} Accounts Selected"
        }
    }

    CreateBudgetSectionCard(title = "Budget Amount") {
        CustomOutlineTextField(state = amountState,
            isExpendable = false,
            maxLength = 10,
            modifier = Modifier.background(
                MaterialTheme.colorScheme.background, RoundedCornerShape(16.dp)
            ),
            placeholder = "Amount",
            type = TextFieldType.NUMBER,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            prefix = { TextFieldRupeePrefix() })
        DropDownField(title = selectedCategoryText, onClick = onCategoryClick)
        DropDownField(title = selectedAccountText, onClick = onAccountClick)
    }
}


@Composable
fun RenewalTypeSection(
    formState: BudgetRequestModel,
    onBudgetTypeClick: (BudgetType) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    CreateBudgetSectionCard(
        title = "Budget Renewal Type"
    ) {
        RenewalSelectionItem(title = BudgetType.RECURRING.displayName,
            desc = BudgetType.RECURRING.description,
            isSelected = formState.type == BudgetType.RECURRING.name,
            onClick = {
                onBudgetTypeClick(BudgetType.RECURRING)
            })
        RenewalSelectionItem(title = BudgetType.EXPIRING.displayName,
            desc = BudgetType.EXPIRING.description,
            isSelected = formState.type == BudgetType.EXPIRING.name,
            onClick = {
                onBudgetTypeClick(BudgetType.EXPIRING)
            })
        Text(
            "Start Date",
            style = MaterialTheme.typography.bodyMedium
        )
        DateFieldWithIcon(
            text = formState.startDate.formatLocalDate() ?: stringResource(R.string.select_start_date),
            isSelected = formState.startDate.isNotEmpty(),
            onClick = onStartDateClick
        )
        if (formState.type == BudgetType.EXPIRING.name) {
            Text(
                "End Date",
                style = MaterialTheme.typography.bodyMedium
            )
            DateFieldWithIcon(
                text = formState.endDate.formatLocalDate() ?: stringResource(R.string.select_end_date),
                isSelected = !formState.endDate.isNullOrEmpty(),
                onClick = onEndDateClick
            )
        }
    }
}


@Composable
private fun PeriodSection(
    formState: BudgetRequestModel, onClick: () -> Unit
) {
    CreateBudgetSectionCard(
        title = "Budget Period"
    ) {
        DropDownField(
            title = BudgetPeriod.entries.find {
                it.name.equals(
                    formState.period, ignoreCase = true
                )
            }?.displayName ?: "",
            isSelected = true,
            onClick = onClick
        )
    }
}



