package com.honeypot.app.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.honeypot.app.utils.dynamicImePadding
import com.honeypot.app.utils.goToNextScreenAfterLogin
import com.honeypot.app.utils.handleApiResponse
import com.honeypot.app.utils.isValidEmail
import com.honeypot.app.utils.toastUtils.CustomToast
import com.honeypot.app.utils.toastUtils.CustomToastModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel()
) {

    // Custom Toast
    val toastState = remember { mutableStateOf<CustomToastModel?>(null) }
    CustomToast(toastState)

    // email and password states
    val email: MutableState<TextFieldStateModel> =
        remember { mutableStateOf(TextFieldStateModel()) }

    val coroutines = rememberCoroutineScope()

    // getting context
    val context = LocalContext.current

    // login state for api
    val sendOtpResponse by authViewModel.sendOtp.collectAsState()
    val loginWithGoogleResponse by authViewModel.signInWithGoogle.collectAsState()


    val isGoogleSignInLoading = remember { mutableStateOf(false) }

    val isLoading =
        sendOtpResponse is ApiResponse.Loading || loginWithGoogleResponse is ApiResponse.Loading

    LaunchedEffect(sendOtpResponse, loginWithGoogleResponse) {
        handleApiResponse(
            response = sendOtpResponse,
            toastState = toastState,
            navController = navController,
            onSuccess = { data ->
                navController.navigate(
                    OtpVerificationScreenRoute(
                        email = email.value.text, name = null
                    )
                )
            }
        )
        handleApiResponse(
            response = loginWithGoogleResponse,
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
            AppBar(stringResource(R.string.login), navController)
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
            VerticalSpace(20.dp)
            LoginImage()
            CustomOutlineTextField(
                state = email,
                placeholder = stringResource(R.string.email),
                modifier = Modifier,
                maxLength = 40
            )
            Spacer(Modifier.height(24.dp))
            FilledButton(text = stringResource(R.string.send_otp),
                shape = RoundedCornerShape(16.dp),
                textModifier = Modifier.padding(vertical = 17.dp),
                onClick = {
                    when {
                        email.value.text.trim().isEmpty() -> {
                            email.value =
                                email.value.copy(error = context.getString(R.string.please_enter_your_email))
                        }

                        !email.value.text.trim().isValidEmail() -> {
                            email.value =
                                email.value.copy(error = context.getString(R.string.please_enter_a_valid_email))
                        }

                        else -> {
                            authViewModel.sendOtp(
                                AuthRequestModel(
                                    email = email.value.text.trim()
                                )
                            )
                        }
                    }
                })
            Spacer(Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.or),
                style = Typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(15.dp))
            GoogleAuthBtn(
                text = stringResource(R.string.login_with_google),
                isLoading = isGoogleSignInLoading.value,
                onClick = {
                    authViewModel.launchGoogleAuth(
                        context,
                        coroutines,
                        isGoogleSignInLoading
                    ) { tokenId ->
                        authViewModel.signInWithGoogle(tokenId)
                    }
                }
            )
            Spacer(Modifier.height(33.dp))
            DontHaveAccountText(
                firstText = stringResource(R.string.don_t_have_an_account),
                secondText = stringResource(R.string.sign_up)
            ) {
                navController.navigate(RegisterScreenRoute) {
                    popUpTo(LoginScreenRoute) { inclusive = true }
                }
            }
            VerticalSpace(20.dp)
        }

        // show loader
        ShowLoader(isLoading)
    }

}


@Composable
fun LoginImage() {
    Image(
        painter = painterResource(R.drawable.ic_onboarding_3),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(20.dp)
    )
}

