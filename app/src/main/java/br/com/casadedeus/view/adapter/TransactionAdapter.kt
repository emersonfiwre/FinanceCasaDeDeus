package br.com.casadedeus.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import kotlinx.android.synthetic.main.card_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var mTransactionModelList: List<TransactionModel> = arrayListOf()

    private val mDateFormat =
        SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

    //val format = SimpleDateFormat("EEE MMM dd kk:mm:ss zXXX yyyy", local)// dia por extenso

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_transaction, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenditure = mTransactionModelList[position]
        holder.day.text = mDateFormat.format(expenditure.day)
        holder.desc.text = expenditure.description
        holder.price.text = expenditure.amount.toString()
    }

    override fun getItemCount(): Int {
        return mTransactionModelList.size
    }

    fun notifyChanged(it: List<TransactionModel>?) {
        if (it != null) {
            mTransactionModelList = it
            notifyDataSetChanged()
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.lbl_day
        val desc: TextView = itemView.lbl_desc
        val price: TextView = itemView.lbl_price

    }
}