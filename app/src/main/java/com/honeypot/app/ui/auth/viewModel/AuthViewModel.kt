package com.honeypot.app.ui.auth.viewModel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.honeypot.app.BuildConfig
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.requestModel.AuthRequestModel
import com.honeypot.app.data.model.responseModel.AuthResponseModel
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.data.repository.AuthRepository
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    val spUtilsManager: SpUtilsManager
) : ViewModel() {

    companion object {
        val TAG = AuthViewModel::class.java.name
    }

    //Login With Google
    private val _signInWithGoogle =
        MutableStateFlow<ApiResponse<AuthResponseModel>>(ApiResponse.idle())
    val signInWithGoogle = _signInWithGoogle.asStateFlow()
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signInWithGoogle.value = ApiResponse.loading()
            _signInWithGoogle.value = authRepository.signInWithGoogle(idToken)
            delay(500)
            _signInWithGoogle.value = ApiResponse.idle()
        }
    }

    //Register  With Google
    private val _signUpWithGoogle =
        MutableStateFlow<ApiResponse<AuthResponseModel>>(ApiResponse.idle())
    val signUpWithGoogle = _signUpWithGoogle.asStateFlow()
    fun signUpWithGoogle(idToken: String) {
        viewModelScope.launch {
            _signUpWithGoogle.value = ApiResponse.loading()
            _signUpWithGoogle.value = authRepository.signUpWithGoogle(idToken)
            delay(500)
            _signUpWithGoogle.value = ApiResponse.idle()
        }
    }

    // Login Api Calling
    private val _login = MutableStateFlow<ApiResponse<AuthResponseModel>>(ApiResponse.idle())
    val login = _login.asStateFlow()
    fun login(authRequestModel: AuthRequestModel) {
        viewModelScope.launch {
            _login.value = ApiResponse.loading()
            _login.value = authRepository.login(authRequestModel)
            delay(500)
            _login.value = ApiResponse.idle()
        }
    }


    // Register Api calling
    private val _register = MutableStateFlow<ApiResponse<UserResponseModel>>(ApiResponse.idle())
    val register = _register.asStateFlow()
    fun register(authRequestModel: AuthRequestModel) {
        viewModelScope.launch {
            _register.value = ApiResponse.loading()
            _register.value = authRepository.register(authRequestModel)
            delay(500)
            _register.value = ApiResponse.idle()
        }
    }

    // Send Otp Api calling
    private val _sendOtp = MutableStateFlow<ApiResponse<Any>>(ApiResponse.idle())
    val sendOtp = _sendOtp.asStateFlow()
    fun sendOtp(authRequestModel: AuthRequestModel) {
        viewModelScope.launch {
            _sendOtp.value = ApiResponse.loading()
            _sendOtp.value = authRepository.sendOtp(authRequestModel)
            delay(500)
            _sendOtp.value = ApiResponse.idle()
        }
    }


    // Google sign in
    fun launchGoogleAuth(
        context: Context,
        coroutines: CoroutineScope,
        loadingState: MutableState<Boolean>,
        onResult: (String) -> Unit
    ) {
        loadingState.value = true

        val credentialManager = CredentialManager.create(context)

        val googleIdOptions: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.CLIENT_ID)
            .setNonce(generateNonce())
            .build()

//        val googleIdOptions: GetSignInWithGoogleOption =
//            GetSignInWithGoogleOption.Builder(BuildConfig.CLIENT_ID)
//                .setNonce(generateNonce())
//                .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        coroutines.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                // call back for result
                onResult(googleIdToken)
                loadingState.value = false
            } catch (e: GetCredentialException) {
                loadingState.value = false
            } catch (e: GoogleIdTokenParsingException) {
                loadingState.value = false
            }
        }
    }

    private fun generateNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return hashNonce
    }

}