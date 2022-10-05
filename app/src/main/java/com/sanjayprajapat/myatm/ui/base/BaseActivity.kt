package com.sanjayprajapat.myatm.ui.base


import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


abstract class BaseActivity : AppCompatActivity() {

    protected fun init() {
        initArguments()
        initViews()
        setupListener()
        loadData()
    }

    protected interface OptionClickedListener {
        fun onBackBtnPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun showMsg(msg:String?){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }
    abstract fun initArguments()
    abstract fun initViews()
    abstract fun setupListener()
    abstract fun loadData()
}