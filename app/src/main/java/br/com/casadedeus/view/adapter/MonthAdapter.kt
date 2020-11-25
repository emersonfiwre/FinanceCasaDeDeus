package br.com.casadedeus.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.view.listener.OnFragmentListener
import kotlinx.android.synthetic.main.card_month.view.*

class MonthAdapter : RecyclerView.Adapter<MonthAdapter.MyViewHolder>() {

    private var mMonthList: List<String> = arrayListOf()
    lateinit var mListener: OnAdapterListener.OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_month, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.month.text = mMonthList[position]
        holder.itemView.setOnClickListener {
            mListener.onItemClick(holder.itemView,position)
        }
    }

    fun notifyChanged(it: List<String>?) {
        if (it != null) {
            mMonthList = it
            notifyDataSetChanged()
        }

    }
    fun attachListener(listener: OnAdapterListener.OnItemClickListener) {
        mListener = listener
    }

    override fun getItemCount(): Int {
        return mMonthList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month = itemView.lbl_month
    }
}
