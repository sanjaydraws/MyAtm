package com.sanjayprajapat.myatm.utils

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton


 fun View?.hideKeyboard(context:Context?) {
    val inputMethodManager: InputMethodManager? =
        context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.hideSoftInputFromWindow(this?.applicationWindowToken, 0)
}