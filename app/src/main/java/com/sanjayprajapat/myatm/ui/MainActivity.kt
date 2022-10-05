package com.sanjayprajapat.myatm.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.sanjayprajapat.myatm.R
import com.sanjayprajapat.myatm.adapter.YourTransactionsAdapter
import com.sanjayprajapat.myatm.databinding.ActivityMainBinding
import com.sanjayprajapat.myatm.data.db.TransactionData
import com.sanjayprajapat.myatm.ui.base.BaseActivity
import com.sanjayprajapat.myatm.ui.transactions.TransactionViewModel
import com.sanjayprajapat.myatm.utils.SharedPreferencesHelper
import com.sanjayprajapat.myatm.utils.hideKeyboard
import com.sanjayprajapat.myatm.utils.safeToInt
import com.sanjayprajapat.myatm.utils.safeToString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var binding: ActivityMainBinding? = null
    val mViewModel by viewModels<TransactionViewModel> ()
    private val mLastTransactionsAdapter by lazy{
        YourTransactionsAdapter(arrayListOf())
    }
    companion object{
        const val TAG ="MainActivity"
    }
    @set:Inject
    internal var sharedPref: SharedPreferencesHelper? = null
    private var atmBalance:Int ? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        binding?.apply {
            setContentView(root)
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }
        init()
    }

    override fun initArguments() {
//        mViewModel.withdrawFromAtm()
        atmBalance = sharedPref?.getFixedAtmAmountWithNotes()?.totalAtmAmount.safeToInt()
    }

    override fun initViews() {
        sharedPref?.setInitialAtmAmount() // set initial fixed amount to the ATM
        updateAtmDetails()
        mViewModel.getAllYourTransactions()
        mViewModel.getLastTransactions()
        binding?.incYourTransactions?.rcLastTransactions?.apply {
            adapter = mLastTransactionsAdapter
        }
    }

    override fun setupListener() {
        binding?.txtLastTransactions?.setOnClickListener {
            mViewModel.clearTables()
        }
        binding?.btnWithdraw?.setOnClickListener {
            if(validate()){
                mViewModel.withdrawFromAtm(binding?.etWithdrawAmount?.text.toString().safeToInt())
                // after withdrawl
//                var atmBalData = sharedPref?.getFixedAtmAmountWithNotes()
//                var rs200Count = atmBalData?.rs200Count.safeToInt()
//                var rs100Count = atmBalData?.rs100Count.safeToInt()
//                var rs2000Count = atmBalData?.rs2000Count.safeToInt()
//                var rs500Count = atmBalData?.rs500Count.safeToInt()
//                var tRs2000Count:Int = 0
//                var tRs100Count:Int=0
//                var tRs200Count:Int=0
//                var tRs500Count:Int=0
//
//                var atmAmount:Int = atmBalData?.totalAtmAmount.safeToInt();
//                val widthdrawAmount = binding?.etWithdrawAmount?.text.toString().safeToInt()
//                var tempWithdrawAmount = widthdrawAmount
//                if(tempWithdrawAmount/2000 !=0){
//                    tRs2000Count =tempWithdrawAmount/2000
//                    tempWithdrawAmount -= 2000 * tRs2000Count
//                    atmAmount -= 2000 * tRs2000Count
//                    rs2000Count -= tRs2000Count
//                    Log.d(TAG, "btnWithdraw: tempWithdrawAmount")
//                }
//                if(tempWithdrawAmount/500 !=0){
//                     tRs500Count =tempWithdrawAmount/500
//                    tempWithdrawAmount -= 500 * tRs500Count
//                    atmAmount -= 500* tRs500Count
//                    rs500Count -= tRs500Count
//                    println(tempWithdrawAmount)
//                    Log.d(TAG, "btnWithdraw: tempWithdrawAmount")
//                }
//                if(tempWithdrawAmount/200 !=0){
//                     tRs200Count = tempWithdrawAmount/200
//                    tempWithdrawAmount -= 200 * tRs200Count
//                    atmAmount -= 200 * tRs200Count
//                    rs200Count -= tRs200Count
//                    println(tempWithdrawAmount)
//                    Log.d(TAG, "btnWithdraw: tempWithdrawAmount")
//                }
//                if(tempWithdrawAmount/100 !=0){
//                    tRs100Count = tempWithdrawAmount/100
//                    tempWithdrawAmount -= 100 * tRs100Count
//                    atmAmount -= 100 * tRs100Count
//                    rs100Count -= tRs100Count
//                    println(tempWithdrawAmount)
//                    Log.d(TAG, "btnWithdraw: tempWithdrawAmount")
//                }
//                println("tempWithdrawAmount $tempWithdrawAmount")
//                println("widthdrawAmount $widthdrawAmount")
//                println("atmAmount $atmAmount")
//
//                sharedPref?.setFixedAtmAmountWithNotes(TransactionData(transactionId = 1,
//                    totalAtmAmount = atmAmount,rs100Count =rs100Count,rs200Count = rs200Count,rs500Count = rs500Count,rs2000Count=rs2000Count,
//                ))
//                Log.d(TAG, "AFTERWITHDRAW : ${sharedPref?.getFixedAtmAmountWithNotes()}")
////                mViewModel.insertTransactionData(TransactionData(totalAtmAmount = binding?.etWithdrawAmount?.text.safeToInt(),
////                rs100Count = 1, rs200Count = 2, rs500Count = 3, rs2000Count = 23))
//                mViewModel.insertTransactionData(TransactionData(transactionAtmAmount = binding?.etWithdrawAmount?.text.safeToInt(),
//                    rs100Count =tRs100Count,rs200Count = tRs200Count,rs500Count = tRs500Count,rs2000Count=tRs2000Count))
                updateAtmDetails()
            }
        }
    }
    override fun loadData() {
        mViewModel.allYourTransactions.observe(this, Observer {
            it?:return@Observer
            mLastTransactionsAdapter.updateData(it)
            Log.d(TAG, "allYourTransactions: $it")
        })
        mViewModel.yourLastTransactions.observe(this, Observer {
            it?:return@Observer
            updateLastTransactionDetails(it)
            Log.d(TAG, "yourLastTransactions: $it")
        })
    }
    private fun validate():Boolean{
        val withdrawAmount = binding?.etWithdrawAmount?.text.toString().safeToInt()
        binding?.btnWithdraw?.hideKeyboard(this)
        if(binding?.etWithdrawAmount?.text.toString().isEmpty()){
            showMsg(getString(R.string.please_enter_withdraw_amount))
            return false
        }else if(!(withdrawAmount%100==0 || withdrawAmount%200==0||withdrawAmount%2000==0||withdrawAmount%5000==0) ){
            showMsg(getString(R.string.please_enter_valid_amount))
            return false
        }else if(withdrawAmount>sharedPref?.getFixedAtmAmountWithNotes()?.totalAtmAmount.safeToInt()){
            showMsg(getString(R.string.insufficient_balance))
            return false
        }
        return true
    }

    private fun updateLastTransactionDetails(transactionData: TransactionData?){
        transactionData?.let {
            binding?.incLastTransactionDetails?.apply {
                rs2000Count.text = transactionData.rs2000Count.safeToString()
                rs100Count.text = transactionData.rs100Count.safeToString()
                rs500Count.text = transactionData.rs500Count.safeToString()
                rs200Count.text = transactionData.rs200Count.safeToString()
                txtTotalAmmountRs.text = transactionData.transactionAtmAmount.safeToString()
            }
        }
    }
    private fun updateAtmDetails(){
        val atmBalanceData = sharedPref?.getFixedAtmAmountWithNotes()
        Log.d(TAG, "updateAtmDetails: $atmBalanceData")
        binding?.incAtmAmountDetails?.apply {
            rs100Count.text = atmBalanceData?.rs100Count.safeToString()
            rs500Count.text = atmBalanceData?.rs500Count.safeToString()
            rs200Count.text = atmBalanceData?.rs200Count.safeToString()
            rs2000Count.text = atmBalanceData?.rs2000Count.safeToString()
            txtTotalAmmountRs.text = atmBalanceData?.totalAtmAmount.safeToString()
        }
    }
}