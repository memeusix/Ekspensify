package com.honeypot.app.ui.auth

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
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.honeypot.app.R
import com.honeypot.app.components.AppBar
import com.honeypot.app.components.CustomOutlineTextField
import com.honeypot.app.components.FilledButton
import com.honeypot.app.components.ShowLoader
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.TextFieldStateModel
import com.honeypot.app.data.model.requestModel.AuthRequestModel
import com.honeypot.app.navigation.LoginScreenRoute
import com.honeypot.app.navigation.OtpVerificationScreenRoute
import com.honeypot.app.navigation.RegisterScreenRoute
import com.honeypot.app.ui.auth.components.DontHaveAccountText
import com.honeypot.app.ui.auth.components.GoogleAuthBtn
import com.honeypot.app.ui.auth.viewModel.AuthViewModel
import com.honeypot.app.ui.theme.Typography
import com.honeypot.app.utils.CommonData
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.goToNextScreenAfterLogin
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.isValidEmail
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel
import com.honeypot.app.utils.toastUtils.ToastType

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
        val annotatedString = buildAnnotatedString {
            append(stringResource(R.string.by_signing_up_you_agree_to_the))
            withLink(
                LinkAnnotation.Url(
                    url = CommonData.TERMS_AND_CONDITION,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                    )
                )
            ) {
                append(stringResource(R.string.terms_of_service))
            }
            append(
                stringResource(R.string.and)
            )
            withLink(
                LinkAnnotation.Url(
                    url = CommonData.PRIVACY_POLICY,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append(stringResource(R.string.privacy_policy))
            }
        }

        Text(
            annotatedString,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
