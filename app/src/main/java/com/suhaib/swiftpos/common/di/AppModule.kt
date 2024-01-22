package com.suhaib.swiftpos.common.di

import android.content.Context
import androidx.room.Room
import com.suhaib.swiftpos.common.db.AbsSwiftDB
import com.suhaib.swiftpos.common.db.SwiftDao
import com.suhaib.swiftpos.common.di.dispatcher.DispatcherProvider
import com.suhaib.swiftpos.common.di.dispatcher.DispatcherProviderImpl
import com.suhaib.swiftpos.common.repository.StoreRepository
import com.suhaib.swiftpos.common.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()

    @Singleton
    @Provides
    fun providesCoroutineScope(dispatcherProvider: DispatcherProvider): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.default)

    @Provides
    @Singleton
    fun provideSwiftDB(@ApplicationContext context: Context): AbsSwiftDB = Room.databaseBuilder(context, AbsSwiftDB::class.java, "swift_data")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideSwiftDao(swiftDB: AbsSwiftDB): SwiftDao = swiftDB.swiftDao()

    @Provides
    fun provideStoreRepository(swiftDao: SwiftDao): StoreRepository = StoreRepository(swiftDao)

    @Provides
    fun provideTransactionRepository(swiftDao: SwiftDao): TransactionRepository = TransactionRepository(swiftDao)
}