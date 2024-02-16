package io.github.joaogouveia89.inspirify.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.joaogouveia89.inspirify.data.api.retrofit.RetrofitZenQuotes
import io.github.joaogouveia89.inspirify.data.local.DATABASE_NAME
import io.github.joaogouveia89.inspirify.data.local.LocalDb
import javax.inject.Singleton

@Module
class DataModule(val context: Context) {
    @Provides
    @Singleton
    fun provideRetrofitZenQuotes() = RetrofitZenQuotes()

    @Provides
    @Singleton
    fun provideLocalDb() = Room.databaseBuilder(
        context,
        LocalDb::class.java, DATABASE_NAME
    ).build()
}