package com.honeypot.app.di

import android.content.Context
import androidx.room.Room
import com.honeypot.app.room.dao.PendingTransactionDao
import com.honeypot.app.room.database.HoneyPotDatabase
import com.honeypot.app.room.repository.PendingTransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): HoneyPotDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = HoneyPotDatabase::class.java,
            name = "honeypot_db"
        )
            .addMigrations(HoneyPotDatabase.MIGRATION_2_3)
            .build()
    }

    @Provides
    @Singleton
    fun providePendingTransactionDao(database: HoneyPotDatabase): PendingTransactionDao {
        return database.pendingTransactionDao()
    }


    @Singleton
    @Provides
    fun providePendingTransactionRepo(pendingTransactionDao: PendingTransactionDao): PendingTransactionRepo {
        return PendingTransactionRepo(pendingTransactionDao)
    }


}
