package br.com.casadedeus.view.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnAdapterListener
import br.com.casadedeus.service.utils.Utils
import kotlinx.android.synthetic.main.card_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var mTransactionModelList: List<TransactionModel> = arrayListOf()
    private lateinit var mListener: OnAdapterListener.OnItemClickListener<TransactionModel>

    //val format = SimpleDateFormat("EEE MMM dd kk:mm:ss zXXX yyyy", local)// dia por extenso

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_transaction, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenditure = mTransactionModelList[position]
        holder.bindTransaction(expenditure)
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

    fun attachListener(listener: OnAdapterListener.OnItemClickListener<TransactionModel>) {
        mListener = listener
    }

    class MyViewHolder(itemView: View, val listener: OnAdapterListener.OnItemClickListener<TransactionModel>?) : RecyclerView.ViewHolder(itemView) {
        private val mDateFormat =
            SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

        private var mDay: TextView = itemView.lbl_day
        private var mDesc: TextView = itemView.lbl_desc
        private var mPrice: TextView = itemView.lbl_price
        private var mCardTransaction: View = itemView.card_transaction

        fun bindTransaction(transaction: TransactionModel) {
            if (transaction.day != null){
                mDay.text = mDateFormat.format(transaction.day)
            }
            mDesc.text = transaction.description
            mPrice.text = Utils.doubleToReal(transaction.amount)


            mCardTransaction.setOnClickListener { listener?.onItemClick(transaction) }

            mCardTransaction.setOnLongClickListener {
                AlertDialog.Builder(itemView.context)
                    .setTitle(R.string.title_want_remove_transaction)
                    .setMessage(R.string.desc_want_remove_transaction)
                    .setPositiveButton(R.string.remove) { dialog, which ->
                        //listener.onDeleteClick(task.id)
                    }
                    .setNeutralButton(R.string.cancel, null)
                    .show()
                true
            }
        }

    }
}