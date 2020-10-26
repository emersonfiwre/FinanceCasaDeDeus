package br.com.casadedeus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.Expenditure
import kotlinx.android.synthetic.main.card_expenditure.view.*
import kotlinx.android.synthetic.main.card_year.view.*

class ExpenditureAdapter(
    private val context: Context,
    private val expenditurelist: List<Expenditure>
) : RecyclerView.Adapter<ExpenditureAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_expenditure, parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val expenditure = expenditurelist[position]
        holder.day.text = expenditure.dia
        holder.desc.text = expenditure.desc
        holder.price.text = expenditure.valor
    }

    override fun getItemCount(): Int {
        return expenditurelist.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val day: TextView = itemView.lbl_day
        val desc: TextView = itemView.lbl_desc
        val price: TextView = itemView.lbl_price

    }
}