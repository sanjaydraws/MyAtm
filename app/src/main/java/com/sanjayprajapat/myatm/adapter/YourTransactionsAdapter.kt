package com.sanjayprajapat.myatm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanjayprajapat.myatm.databinding.ItemYourTransactionLayoutBinding
import com.sanjayprajapat.myatm.data.db.TransactionData


class YourTransactionsAdapter(
    var yourTransactionsList: List<TransactionData>? ): RecyclerView.Adapter<YourTransactionsAdapter.LastTransactionVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourTransactionsAdapter.LastTransactionVH {
        val binding = ItemYourTransactionLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LastTransactionVH(binding)
    }
    fun updateData(lastTransactionsList: List<TransactionData>? ){
        this.yourTransactionsList = lastTransactionsList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return yourTransactionsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: YourTransactionsAdapter.LastTransactionVH, position: Int) {
        yourTransactionsList?.get(position)?.let { holder.loadData(it) }
    }
    inner  class LastTransactionVH(val  binding :ItemYourTransactionLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun loadData(transactionData: TransactionData){
            binding.apply {
                rs100Count.text = transactionData.rs100Count.toString()
                rs200Count.text = transactionData.rs200Count.toString()
                rs500Count.text = transactionData.rs500Count.toString()
                rs2000Count.text = transactionData.rs2000Count.toString()
                txtTotalAmmountRs.text = transactionData.transactionAtmAmount.toString()
            }
        }
    }
}