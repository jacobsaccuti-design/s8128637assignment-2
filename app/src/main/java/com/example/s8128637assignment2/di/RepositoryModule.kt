package com.example.s8128637assignment2.di

import com.example.s8128637assignment2.data.repository.AuthRepository
import com.example.s8128637assignment2.data.repository.AuthRepositoryImpl
import com.example.s8128637assignment2.data.repository.DashboardRepository
import com.example.s8128637assignment2.data.repository.DashboardRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(impl: DashboardRepositoryImpl): DashboardRepository
}
