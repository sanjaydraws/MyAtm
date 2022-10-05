package com.sanjayprajapat.myatm.utils

import org.jetbrains.annotations.NotNull


@NotNull
fun Any?.safeToString(): String {
    return try {
        this?.toString()?:""
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        ""
    }
}


@NotNull
fun Any?.safeToInt(): Int {
    return try {
        this?.toString()?.toInt() ?: 0
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        0
    }
}