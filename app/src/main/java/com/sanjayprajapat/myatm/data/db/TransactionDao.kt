package com.sanjayprajapat.myatm.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface TransactionDao {

    /**
     * insert withdraw amount
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(orderData: TransactionData?)

    /**
     * to get all your transactions
     * */
    @Query("SELECT * FROM TransactionData ORDER BY transactionId DESC")
    suspend fun getAllTransaction():List<TransactionData>

    /**
    * to get last transactions
    * */
    @Query("SELECT * FROM TransactionData ORDER BY transactionId DESC LIMIT 1")
    fun getLastTransaction(): TransactionData


}