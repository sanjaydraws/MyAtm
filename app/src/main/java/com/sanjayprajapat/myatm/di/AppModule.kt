package com.sanjayprajapat.myatm.di
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sanjayprajapat.myatm.data.db.TransactionDao
import com.sanjayprajapat.myatm.data.db.TransactionData
import com.sanjayprajapat.myatm.data.db.TransactionDatabase
import com.sanjayprajapat.myatm.utils.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val MY_SHARED_PREF ="my_shared_pref"


    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun customSharedPref(@ApplicationContext context: Context) : SharedPreferences {
        return   context.getSharedPreferences(
            MY_SHARED_PREF,
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideSharedPrefHelper(gson: Gson, sharedPreferences: SharedPreferences) = SharedPreferencesHelper(gson, sharedPreferences)


    @Provides
    @Singleton
    fun provideTransactionDao(@ApplicationContext context: Context):TransactionDao =
        TransactionDatabase.getInstance(context).transactionDao
//    @Provides
//    @Singleton
//    fun provideGsonHelper(gson:Gson?) = GsonHelper(gson)

//    @Provides
//    @Singleton
//    fun provideFirebaseAuth(): FirebaseAuth =  FirebaseAuth.getInstance()

}