package com.sanjayprajapat.myatm.utils

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.sanjayprajapat.myatm.data.db.TransactionData

class SharedPreferencesHelper(private val gson: Gson?, private val sharedPreferences: SharedPreferences?){
    companion object{
        const val FIXED_ATM_AMOUNT = "fixed_atm_amount"
    }
    private inline fun edit(perform: (SharedPreferences.Editor?) -> Unit) {
        val editor = sharedPreferences?.edit()
        editor?.apply {
            perform.invoke(editor)
            apply()
        }
    }

    /**
     * put data in shared preferences
     */
    operator fun set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it?.putString(key, value) }
            is Int -> edit { it?.putInt(key, value) }
            is Boolean -> edit { it?.putBoolean(key, value) }
            is Float -> edit { it?.putFloat(key, value) }
            is Long -> edit { it?.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }


    fun <T> setObject(key: String? , objectData:T) {
         val  jsonValue  = gson?.toJson(objectData)
        edit{
            it?.putString(key, jsonValue)
        }
    }

    fun <T> getObject(key: String?, t: Class<T>): T? {
        val jsonValue = sharedPreferences?.getString(key, "")
        return gson?.fromJson(jsonValue, t)
    }

    /**
     * to set fixed atm amount
     * */
    fun setFixedAtmAmountWithNotes(transactionData: TransactionData?){
        setObject(FIXED_ATM_AMOUNT, transactionData)
    }
    fun getFixedAtmAmountWithNotes():TransactionData?{
        return getObject(FIXED_ATM_AMOUNT, TransactionData::class.java)
    }

    fun setInitialAtmAmount(){
        if(getFixedAtmAmountWithNotes() == null ){
            setFixedAtmAmountWithNotes(TransactionData(transactionId = 1, totalAtmAmount = 50000, rs2000Count = 10, rs500Count = 25, rs200Count = 50,
                rs100Count = 75))
        }
    }






}