package com.honeypot.app.ui.categories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.honeypot.app.R
import com.honeypot.app.components.AlertDialog
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.PullToRefreshLayout
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.navigation.CreateCategoryScreenRoute
import com.honeypot.app.ui.categories.components.CategoryList
import com.honeypot.app.ui.categories.components.CategoryTypeSelectionToggle
import com.honeypot.app.ui.categories.viewmodel.CategoryViewModel
import com.honeypot.app.utils.NavigationRequestKeys
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.getViewModelStoreOwner
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel

@Composable
fun CategoriesScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel = hiltViewModel(navController.getViewModelStoreOwner())
) {
    val selectedCategoryType by categoryViewModel.selectedCategoryType.collectAsStateWithLifecycle()

    val getCategories by categoryViewModel.getCategories.collectAsStateWithLifecycle()

    val categories by remember {
        derivedStateOf {
            getCategories.data?.filter { it.type == selectedCategoryType.getValue() }
        }
    }

    val deleteCategories by categoryViewModel.deleteCategory.collectAsStateWithLifecycle()
    val isLoading =
        getCategories is ApiResponse.Loading || deleteCategories is ApiResponse.Loading


    //Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    // delete dialog
    val isDeleteDialogOpen = remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }



    if (isDeleteDialogOpen.value.first) {
        AlertDialog(
            title = stringResource(R.string.are_you_sure),
            message = stringResource(R.string.you_want_delete_this_category),
            btnText = stringResource(R.string.delete),
            onDismiss = singleClick {
                isDeleteDialogOpen.value =
                    isDeleteDialogOpen.value.copy(false, null)
            },
            onConfirm = singleClick {
                isDeleteDialogOpen.value.second?.let { categoryViewModel.deleteCategory(it) }
                isDeleteDialogOpen.value =
                    isDeleteDialogOpen.value.copy(false, null)
            }
        )
    }


    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle?.getLiveData<Boolean>(NavigationRequestKeys.CREATE_CATEGORY)
            ?.observeForever { result ->
                if (result == true) {
                    categoryViewModel.getCategories()
                    savedStateHandle.remove<Boolean>(NavigationRequestKeys.CREATE_CATEGORY)
                }
            }
    }

    LaunchedEffect(deleteCategories) {
        handleApiResponse(response = deleteCategories,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.let {
                    categoryViewModel.resetDeleteCategory()
                    categoryViewModel.getCategories()
                }
            })
    }

    //Main Ui
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(
                heading = stringResource(R.string.categories),
                navController = navController,
                isBackNavigation = true,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
        ) {
            Box(modifier = Modifier.padding(horizontal = 20.dp)) {
                CategoryTypeSelectionToggle(
                    selectedCategoryType = selectedCategoryType,
                    onChange = {
                        categoryViewModel.updateSelectedCategoryType(it)
                    }
                )
            }
            VerticalSpace(10.dp)
            PullToRefreshLayout(
                onRefresh = categoryViewModel::getCategories,
                modifier = Modifier
                    .weight(1f)
            ) {
                CategoryList(
                    modifier = Modifier.fillMaxSize(),
                    categories.orEmpty(),
                    onDeleteClick = {
                        isDeleteDialogOpen.value = isDeleteDialogOpen.value.copy(true, it)
                    }
                )
            }
            FilledButton(
                text = stringResource(R.string.add),
                onClick = {
                    navController.navigate(
                        CreateCategoryScreenRoute(
                            categoryResponseModelArgs = null
                        )
                    )
                },
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 20.dp),
                textModifier = Modifier.padding(vertical = 17.dp)
            )
        }
        // show Loader
        ShowLoader(isLoading)
    }
}
