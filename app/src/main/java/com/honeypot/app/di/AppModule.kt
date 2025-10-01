package com.honeypot.app.di

import android.app.Application
import com.honeypot.app.BuildConfig
import com.honeypot.app.data.services.AccountApi
import com.honeypot.app.data.services.AuthApi
import com.honeypot.app.data.services.BudgetApi
import com.honeypot.app.data.services.CategoryApi
import com.honeypot.app.data.services.ExportApi
import com.honeypot.app.data.services.ProfileApi
import com.honeypot.app.data.services.TransactionApi
import com.honeypot.app.utils.fileUtils.FileRepository
import com.honeypot.app.utils.fileUtils.FileRepositoryImpl
import com.honeypot.app.utils.spUtils.SpUtils
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        fun getRetrofit(app: Application): Retrofit {
            val spUtils = SpUtils(app.applicationContext)
            val httpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(getAuthenticationInterceptor(spUtils))
                .build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor { message ->
                    HttpLoggingInterceptor.Logger.DEFAULT.log(filterLogs(message))
                }
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                httpLoggingInterceptor
            } else {
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE }
            }
        }

        private fun getAuthenticationInterceptor(spUtils: SpUtils): Interceptor {
            return Interceptor { chain ->
                val token = spUtils.accessToken
                var request = chain.request()
                request = if (token.isNotEmpty()) {
                    request.newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    request
                }
                chain.proceed(request)
            }
        }

        private fun filterLogs(message: String): String {
            return when {
                message.contains("Content-Type:") && (message.contains("image/") || message.contains(
                    "application/octet-stream"
                )) -> {
                    "Body: [Binary data omitted]"
                }

                else -> message
            }
        }
    }

    @Provides
    @Singleton
    fun provideSpUtils(application: Application): SpUtils {
        return SpUtils(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideFileRepository(app: Application): FileRepository {
        return FileRepositoryImpl(app.applicationContext)
    }

    @Provides
    @Singleton
    fun provideSpUtilsManager(spUtils: SpUtils): SpUtilsManager {
        return SpUtilsManager(spUtils)
    }

    @Provides
    @Singleton
    fun provideAuthApi(app: Application): AuthApi {
        return getRetrofit(app).create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAccountApi(app: Application): AccountApi {
        return getRetrofit(app).create(AccountApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProfileApi(app: Application): ProfileApi {
        return getRetrofit(app).create(ProfileApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApi(app: Application): CategoryApi {
        return getRetrofit(app).create(CategoryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTransactionApi(app: Application): TransactionApi {
        return getRetrofit(app).create(TransactionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideBudgetApi(app: Application): BudgetApi {
        return getRetrofit(app).create(BudgetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExportApi(app: Application): ExportApi {
        return getRetrofit(app).create(ExportApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAzureOpenAIRepository(): com.honeypot.app.ai.AzureOpenAIRepository {
        return com.honeypot.app.ai.AzureOpenAIRepository()
    }
}
