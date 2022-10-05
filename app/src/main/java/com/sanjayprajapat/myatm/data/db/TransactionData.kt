package com.sanjayprajapat.myatm.data.db

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class TransactionData (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "transactionId")val transactionId:Int? = null,
    @ColumnInfo(name = "atmAmount")val transactionAtmAmount:Int? = null,
    @ColumnInfo(name = "rs100Count")val rs100Count:Int? = null,
    @ColumnInfo(name = "rs200Count")val rs200Count:Int? = null,
    @ColumnInfo(name = "rs500Count")val rs500Count:Int? = null,
    @ColumnInfo(name = "rs2000Count")val rs2000Count:Int? = null,
    @ColumnInfo(name = "totalAtmAmount")val totalAtmAmount:Int? = null,

    )