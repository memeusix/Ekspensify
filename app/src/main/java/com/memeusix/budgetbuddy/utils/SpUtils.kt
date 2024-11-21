package com.memeusix.budgetbuddy.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.memeusix.budgetbuddy.R
import com.memeusix.budgetbuddy.data.model.responseModel.UserResponseModel
import javax.inject.Inject

class SpUtils @Inject constructor(context: Context) {
    val pref: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var accessToken: String
        get() = getDataByKey(ACCESS_TOKEN)
        set(accessToken) = storeDataByKey(ACCESS_TOKEN, accessToken)

    var isLoggedIn: Boolean
        get() = pref.contains(IS_LOGGED_IN) && pref.getBoolean(IS_LOGGED_IN, true)
        set(isLoggedIn) = pref.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()

    var user: UserResponseModel?
        get() {
            val gson = Gson()
            val json = getDataByKey(USER_MODEL)
            return gson.fromJson(json, UserResponseModel::class.java)
        }
        set(user) {
            val gson = Gson()
            val json = gson.toJson(user)
            pref.edit().putString(USER_MODEL, json).apply()
        }


    private fun getDataByKey(key: String): String {
        return if (pref.contains(key)) {
            pref.getString(key, "").toString()
        } else {
            ""
        }
    }

    private fun storeDataByKey(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    companion object {
        private const val TAG = "SpUtils"
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val USER_MODEL = "USER_MODEL"

    }
}