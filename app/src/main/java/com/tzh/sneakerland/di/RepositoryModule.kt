package com.tzh.sneakerland.di

import com.tzh.sneakerland.data.repository.EcommerceRepositoryImpl
import com.tzh.sneakerland.domain.repository.EcommerceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindEcommerceRepository(
        ecommerceRepositoryImpl: EcommerceRepositoryImpl
    ): EcommerceRepository
}
