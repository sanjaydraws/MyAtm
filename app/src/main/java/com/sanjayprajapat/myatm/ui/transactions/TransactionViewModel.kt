package com.sanjayprajapat.myatm.ui.transactions

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanjayprajapat.myatm.data.db.TransactionDao
import com.sanjayprajapat.myatm.data.db.TransactionData
import com.sanjayprajapat.myatm.data.db.TransactionDatabase
import com.sanjayprajapat.myatm.ui.MainActivity
import com.sanjayprajapat.myatm.utils.SharedPreferencesHelper
import com.sanjayprajapat.myatm.utils.safeToInt
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TransactionViewModel @Inject constructor(
    @ApplicationContext val context:Context,
    val transactionDao: TransactionDao
):ViewModel() {

    private val _allYourTransactions:MutableLiveData<List<TransactionData>> =MutableLiveData()
    val allYourTransactions:LiveData<List<TransactionData>> = _allYourTransactions

    private val _yourLastTransactions:MutableLiveData<TransactionData> =MutableLiveData()
    val yourLastTransactions:LiveData<TransactionData> = _yourLastTransactions


    @set:Inject
     var sharedPref: SharedPreferencesHelper? = null



    fun getAllYourTransactions(){
        viewModelScope.launch(Dispatchers.IO) {
            val yourTransactions = transactionDao.getAllTransaction()
            _allYourTransactions.postValue(yourTransactions)
        }
    }

    fun insertTransactionData (transactionData:TransactionData?){
        viewModelScope.launch(Dispatchers.IO) {
            transactionDao.insertTransaction(transactionData)
            getAllYourTransactions() // to get all transactions
            getLastTransactions() // to get all last transactions
        }
    }

    fun getLastTransactions(){
        viewModelScope.launch(Dispatchers.IO) {
            val lastTransactions = transactionDao.getLastTransaction()
            _yourLastTransactions.postValue(lastTransactions)
        }
    }

    fun clearTables() {
        viewModelScope.launch(Dispatchers.IO) {
            TransactionDatabase.getInstance(context).clearAllTables()
        }
    }

    fun withdrawFromAtm(withdrawAmount:Int?){
        viewModelScope.launch {
            Log.d("withdrawFromAtm", "withdrawFromAtm:${sharedPref?.getFixedAtmAmountWithNotes()}")

            // after withdrawl
            var atmBalData = sharedPref?.getFixedAtmAmountWithNotes()
            var rs200Count = atmBalData?.rs200Count.safeToInt()
            var rs100Count = atmBalData?.rs100Count.safeToInt()
            var rs2000Count = atmBalData?.rs2000Count.safeToInt()
            var rs500Count = atmBalData?.rs500Count.safeToInt()
            var tRs2000Count:Int = 0
            var tRs100Count:Int=0
            var tRs200Count:Int=0
            var tRs500Count:Int=0

            var atmAmount:Int = atmBalData?.totalAtmAmount.safeToInt();
            val widthdrawAmount = withdrawAmount.safeToInt()
            var tempWithdrawAmount = widthdrawAmount
            if(tempWithdrawAmount/2000 !=0){
                tRs2000Count =tempWithdrawAmount/2000
                tempWithdrawAmount -= 2000 * tRs2000Count
                atmAmount -= 2000 * tRs2000Count
                rs2000Count -= tRs2000Count
            }
            if(tempWithdrawAmount/500 !=0){
                tRs500Count =tempWithdrawAmount/500
                tempWithdrawAmount -= 500 * tRs500Count
                atmAmount -= 500* tRs500Count
                rs500Count -= tRs500Count
            }
            if(tempWithdrawAmount/200 !=0){
                tRs200Count = tempWithdrawAmount/200
                tempWithdrawAmount -= 200 * tRs200Count
                atmAmount -= 200 * tRs200Count
                rs200Count -= tRs200Count
            }
            if(tempWithdrawAmount/100 !=0){
                tRs100Count = tempWithdrawAmount/100
                tempWithdrawAmount -= 100 * tRs100Count
                atmAmount -= 100 * tRs100Count
                rs100Count -= tRs100Count
            }
            sharedPref?.setFixedAtmAmountWithNotes(TransactionData(transactionId = 1,
                totalAtmAmount = atmAmount,rs100Count =rs100Count,rs200Count = rs200Count,rs500Count = rs500Count,rs2000Count=rs2000Count,
            ))
            insertTransactionData(TransactionData(transactionAtmAmount = widthdrawAmount.safeToInt(),
                rs100Count =tRs100Count,rs200Count = tRs200Count,rs500Count = tRs500Count,rs2000Count=tRs2000Count))
        }
    }

}