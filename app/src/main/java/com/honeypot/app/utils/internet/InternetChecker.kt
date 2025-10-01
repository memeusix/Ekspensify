package com.honeypot.app.utils.internet

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InternetChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {

    val isConnected = MutableStateFlow<Boolean>(true)

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val netWorkCallBack = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            isConnected.value = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isConnected.value = false
        }
    }

    private fun updateNetworkStatus() {
        val activityNetwork = connectivityManager.activeNetwork
        val capability = connectivityManager.getNetworkCapabilities(activityNetwork)
        isConnected.value =
            capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
                    && capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }


    fun startTracking() {
        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, netWorkCallBack)
        updateNetworkStatus()
    }

    fun stopTracking() {
        connectivityManager.unregisterNetworkCallback(netWorkCallBack)
    }
}