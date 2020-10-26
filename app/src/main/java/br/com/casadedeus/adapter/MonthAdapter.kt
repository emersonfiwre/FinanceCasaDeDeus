package br.com.casadedeus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.`interface`.OnClickListener
import kotlinx.android.synthetic.main.card_month.view.*

class MonthAdapter(
    private val monthlist: Array<String>,
    private val context: Context
) : RecyclerView.Adapter<MonthAdapter.MyViewHolder>() {
    lateinit var onMonthClickListener: OnClickListener.OnMonthClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_month, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.month.text = monthlist[position]
        holder.itemView.setOnClickListener {
            onMonthClickListener.onMonthClick(monthlist[position])
        }
    }


    override fun getItemCount(): Int {
        return monthlist.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month = itemView.lbl_month
    }
}
