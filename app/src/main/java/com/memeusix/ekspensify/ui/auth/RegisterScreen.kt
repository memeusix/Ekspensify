package com.memeusix.ekspensify.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.memeusix.ekspensify.R
import com.memeusix.ekspensify.components.AppBar
import com.memeusix.ekspensify.components.CustomOutlineTextField
import com.memeusix.ekspensify.components.FilledButton
import com.memeusix.ekspensify.components.ShowLoader
import com.memeusix.ekspensify.components.VerticalSpace
import com.memeusix.ekspensify.data.ApiResponse
import com.memeusix.ekspensify.data.model.TextFieldStateModel
import com.memeusix.ekspensify.data.model.requestModel.AuthRequestModel
import com.memeusix.ekspensify.navigation.LoginScreenRoute
import com.memeusix.ekspensify.navigation.OtpVerificationScreenRoute
import com.memeusix.ekspensify.navigation.RegisterScreenRoute
import com.memeusix.ekspensify.ui.auth.components.DontHaveAccountText
import com.memeusix.ekspensify.ui.auth.components.GoogleAuthBtn
import com.memeusix.ekspensify.ui.auth.viewModel.AuthViewModel
import com.memeusix.ekspensify.ui.theme.Typography
import com.memeusix.ekspensify.utils.dynamicImePadding
import com.memeusix.ekspensify.utils.goToNextScreenAfterLogin
import com.memeusix.ekspensify.utils.handleApiResponse
import com.memeusix.ekspensify.utils.isValidEmail
import com.memeusix.ekspensify.utils.toastUtils.CustomToast
import com.memeusix.ekspensify.utils.toastUtils.CustomToastModel
import com.memeusix.ekspensify.utils.toastUtils.ToastType
import com.onesignal.OneSignal

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    val nameState = remember { mutableStateOf(TextFieldStateModel()) }
    val emailState = remember { mutableStateOf(TextFieldStateModel()) }
    var isCheckBoxCheck by remember { mutableStateOf(false) }

    // api states
    val registerResponse by authViewModel.register.collectAsState()
    val signUpWithGoogleResponse by authViewModel.signUpWithGoogle.collectAsState()


    val context = LocalContext.current
    val coroutines = rememberCoroutineScope()


    // loading state
    val isLoading =
        registerResponse is ApiResponse.Loading || signUpWithGoogleResponse is ApiResponse.Loading

    val isGoogleSignUpLoading = remember { mutableStateOf(false) }


    LaunchedEffect(registerResponse, signUpWithGoogleResponse) {
        handleApiResponse(
            response = registerResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.apply {
                    navController.navigate(
                        OtpVerificationScreenRoute(
                            name = nameState.value.text.trim(),
                            email = emailState.value.text.trim(),
                        )
                    )
                }
            }
        )
        handleApiResponse(
            response = signUpWithGoogleResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                data?.apply {
                    if (user != null && !token.isNullOrEmpty()) {
                        authViewModel.spUtilsManager.logout()
                        authViewModel.spUtilsManager.updateUser(user)
                        authViewModel.spUtilsManager.updateAccessToken(token!!)
                        authViewModel.spUtilsManager.updateLoginStatus(true)

                        OneSignal.login(user!!.id.toString())
                        goToNextScreenAfterLogin(navController)
                    }
                }
            }
        )
    }

    // Main Ui
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppBar(stringResource(R.string.sign_up), navController)
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .dynamicImePadding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            VerticalSpace(56.dp)
            CustomOutlineTextField(
                state = nameState,
                placeholder = stringResource(R.string.name),
                maxLength = 40
            )
            VerticalSpace(24.dp)
            CustomOutlineTextField(
                state = emailState,
                placeholder = "Email",
                isExpendable = false,
                maxLength = 50
            )
            VerticalSpace(16.dp)
            TermsAndConditionCheckBox(isCheckBoxCheck) {
                isCheckBoxCheck = it
            }
            VerticalSpace(16.dp)
            FilledButton(text = stringResource(R.string.send_otp),
                shape = RoundedCornerShape(16.dp),
                textModifier = Modifier.padding(vertical = 17.dp),
                onClick = {
                    when {
                        nameState.value.text.trim().isEmpty() -> {
                            nameState.value = nameState.value.copy(error = "please enter your name")
                        }

                        emailState.value.text.trim().isEmpty() -> {
                            emailState.value =
                                emailState.value.copy(error = context.getString(R.string.please_enter_your_email))
                        }

                        !emailState.value.text.trim().isValidEmail() -> {
                            emailState.value =
                                emailState.value.copy(error = context.getString(R.string.please_enter_a_valid_email))
                        }

                        !isCheckBoxCheck -> {
                            toastState.value = CustomToastModel(
                                message = context.getString(R.string.please_check_terms_and_conditions),
                                isVisible = true,
                                type = ToastType.ERROR
                            )
                        }

                        else -> {
                            authViewModel.register(
                                AuthRequestModel(
                                    name = nameState.value.text.trim(),
                                    email = emailState.value.text.trim()
                                )
                            )
                        }
                    }
                })
            VerticalSpace(15.dp)
            Text(
                text = stringResource(R.string.or),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            VerticalSpace(15.dp)
            GoogleAuthBtn(
                text = stringResource(R.string.sign_up_with_google),
                isLoading = isGoogleSignUpLoading.value,
                onClick = {
                    authViewModel.launchGoogleAuth(
                        context,
                        coroutines,
                        isGoogleSignUpLoading
                    ) { tokenId ->
                        authViewModel.signUpWithGoogle(tokenId)
                    }
                }
            )
            VerticalSpace(33.dp)
            DontHaveAccountText(
                firstText = stringResource(R.string.already_have_an_account),
                secondText = stringResource(R.string.login)
            ) {
                navController.navigate(LoginScreenRoute) {
                    popUpTo(RegisterScreenRoute) { inclusive = true }
                }
            }
            VerticalSpace(20.dp)
        }


        // Shows the loader
        ShowLoader(isLoading)

    }
}

@Composable
private fun TermsAndConditionCheckBox(isCheckBoxCheck: Boolean, onCheckChange: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Checkbox(
            checked = isCheckBoxCheck, onCheckedChange = {
                onCheckChange(it)
            }, modifier = Modifier.size(30.dp), colors = CheckboxDefaults.colors(
                uncheckedColor = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            stringResource(R.string.by_signing_up_you_agree_to_the_terms_of_service_and_privacy_policy),
            style = MaterialTheme.typography.bodySmall
        )
    }
}