package com.honeypot.app.ui.dashboard.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.honeypot.app.R
import com.honeypot.app.components.AlertDialog
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.components.EmptyView
import com.honeypot.app.components.ListIcon
import com.honeypot.app.components.PullToRefreshLayout
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.navigation.AboutScreenRoute
import com.honeypot.app.navigation.AccountScreenRoute
import com.honeypot.app.navigation.AutoTrackingScreenRoute
import com.honeypot.app.navigation.CategoriesScreenRoute
import com.honeypot.app.navigation.ExportScreenRoute
import com.honeypot.app.navigation.IntroScreenRoute
import com.honeypot.app.ui.dashboard.profile.componets.ProfileAvatar
import com.honeypot.app.ui.dashboard.profile.data.ProfileOptionProvider
import com.honeypot.app.ui.dashboard.profile.data.ProfileOptions
import com.honeypot.app.ui.dashboard.profile.viewModel.ProfileViewModel
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.singleClick
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel

@Composable
fun ProfileScreen(
    navController: NavController, viewmodel: ProfileViewModel = hiltViewModel()
) {
    val getMeState by viewmodel.getMe.collectAsStateWithLifecycle()
    val updateMeState by viewmodel.updateMe.collectAsStateWithLifecycle()
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }

    val showEditBottomSheet = remember { mutableStateOf(false) }

    val isLoading = getMeState is ApiResponse.Loading || updateMeState is ApiResponse.Loading


    val isLogoutDialogOpen = remember { mutableStateOf(false) }

    if (isLogoutDialogOpen.value) {
        AlertDialog(
            title = stringResource(R.string.are_you_sure),
            message = stringResource(R.string.you_want_to_logout),
            btnText = stringResource(R.string.logout),
            onDismiss = { isLogoutDialogOpen.value = false },
            onConfirm = {
                isLogoutDialogOpen.value = false
                navController.navigate(IntroScreenRoute) {
                    popUpTo(0) { inclusive = false }
                }
                viewmodel.spUtilsManager.logout()
            }
        )
    }


    val modifier = Modifier
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colorScheme.background)
        .fillMaxWidth()
        .border(
            width = 1.dp,
            color = MaterialTheme.extendedColors.primaryBorder,
            RoundedCornerShape(16.dp)
        )
        .padding(vertical = 15.dp)
        .animateContentSize()

    LaunchedEffect(getMeState, updateMeState) {
        handleApiResponse(
            response = getMeState,
            toastState = toastState,
            navController = navController,
            onSuccess = { data -> }
        )
        handleApiResponse(
            response = updateMeState,
            toastState = toastState,
            navController = navController,
            onSuccess = { data -> }
        )
    }

    // Custom Toast
    CustomToast(toastState)


    // Edit Profile Bottom Sheet
    if (showEditBottomSheet.value) {
        EditProfileBottomSheet(user = getMeState.data ?: UserResponseModel()) { userModel ->
            userModel?.let {
                viewmodel.updateMe(it)
            }
            showEditBottomSheet.value = false
        }
    }


    //Main Screen
    PullToRefreshLayout(
        onRefresh = viewmodel::getMe,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserInfo(isLoading, getMeState, showEditBottomSheet)
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    stringResource(R.string.general),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 10.dp, start = 15.dp, end = 15.dp)
                )
                ProfileOptionProvider.getGeneralOptions().forEach { profileOptions ->
                    ProfileListItem(
                        profileOptions = profileOptions,
                        onClick = singleClick {
                            handleProfileOptionClick(
                                isLogoutDialogOpen,
                                profileOptions,
                                navController,
                                viewmodel
                            )
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.extendedColors.primaryBorder)
                }
                Text(
                    stringResource(R.string.danger),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp)
                )
                ProfileOptionProvider.getDangerOptions().forEach { profileOptions ->
                    ProfileListItem(
                        profileOptions = profileOptions,
                        onClick = singleClick {
                            handleProfileOptionClick(
                                isLogoutDialogOpen,
                                profileOptions,
                                navController,
                                viewmodel
                            )
                        }
                    )
                }
            }
            VerticalSpace(20.dp)
        }
        ShowLoader(isLoading, true)
    }
}

@Composable
private fun ColumnScope.UserInfo(
    isLoading: Boolean,
    getMeState: ApiResponse<UserResponseModel>,
    showEditBottomSheet: MutableState<Boolean>
) {
    getMeState.data?.let { details ->
        ProfileAvatar(details, onEditClick = singleClick {
            showEditBottomSheet.value = true
        })
        Text(
            details.name ?: "",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            details.email ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    } ?: run {
        EmptyView(
            title = stringResource(R.string.profile_fetch_failed),
            description = stringResource(R.string.profile_error_desc),
            image = null,
            modifier = Modifier
        )
    }
}

@Composable
private fun ProfileListItem(profileOptions: ProfileOptions, onClick: () -> Unit) {
    CustomListItem(
        title = stringResource(profileOptions.titleRes),
        leadingContent = {
            ListIcon(profileOptions.icon)
        },
        modifier = Modifier.padding(horizontal = 15.dp, vertical = 9.dp),
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                tint = MaterialTheme.extendedColors.iconColor,
                contentDescription = null,
            )
        },
        onClick = onClick
    )
}


private fun handleProfileOptionClick(
    isLogoutDialogOpen: MutableState<Boolean>,
    profileOptions: ProfileOptions,
    navController: NavController,
    viewmodel: ProfileViewModel
) {
    when (profileOptions) {
        ProfileOptions.ACCOUNT -> {
            navController.navigate(
                AccountScreenRoute(
                    isFromProfile = true
                )
            )
        }

        ProfileOptions.CATEGORY -> {
            navController.navigate(CategoriesScreenRoute)
        }

        ProfileOptions.EXPORT -> {
            navController.navigate(ExportScreenRoute)
        }

        ProfileOptions.AUTO_TRACKING -> {
            navController.navigate(AutoTrackingScreenRoute)
        }

        ProfileOptions.ABOUT -> {
            navController.navigate(AboutScreenRoute)
        }

        ProfileOptions.LOGOUT -> {
            isLogoutDialogOpen.value = true
        }
    }
}

