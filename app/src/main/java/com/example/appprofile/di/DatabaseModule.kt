package com.example.appprofile.di

import android.content.Context
import androidx.room.Room
import com.example.appprofile.data.local.AppDatabase
import com.example.appprofile.data.local.ProfileDao
import com.example.appprofile.data.local.PostDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "profile_db"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideProfileDao(db: AppDatabase): ProfileDao = db.profileDao()

    @Provides
    @Singleton
    fun providePostDao(db: AppDatabase): PostDao = db.postDao()
}
