package br.com.casadedeus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import kotlinx.android.synthetic.main.card_year.view.*


class YearAdapter(private val yearlist:Array<String>, private val context:Context): RecyclerView.Adapter<YearAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_year,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = yearlist[position]
    }

    override fun getItemCount() =  yearlist.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.lbl_year
    }
}

