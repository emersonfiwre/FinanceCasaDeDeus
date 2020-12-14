package br.com.casadedeus.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.MonthModel
import br.com.casadedeus.service.listener.OnAdapterListener
import kotlinx.android.synthetic.main.card_month.view.*

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MyViewHolder>() {

    private var mMonthModelList: List<MonthModel> = arrayListOf()
    private lateinit var mListener: OnAdapterListener.OnItemClickListener<MonthModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_month, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.month.text = mMonthModelList[position].monthTitle
        holder.itemView.setOnClickListener {
            mListener.onItemClick(mMonthModelList[position])
        }
    }

    fun notifyChanged(it: List<MonthModel>?) {
        if (it != null) {
            mMonthModelList = it
            notifyDataSetChanged()
        }

    }

    fun attachListener(listener: OnAdapterListener.OnItemClickListener<MonthModel>) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return mMonthModelList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month = itemView.lbl_month
    }
}
