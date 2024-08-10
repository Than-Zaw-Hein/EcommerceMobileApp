package com.tzh.sneakerland.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.tzh.sneakerland.data.local.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(@ApplicationContext conntext: Context) =
        SharedPreferencesHelper(conntext)



    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}