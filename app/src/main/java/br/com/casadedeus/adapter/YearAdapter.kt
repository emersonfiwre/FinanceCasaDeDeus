package br.com.casadedeus.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.`interface`.OnClickListener
import kotlinx.android.synthetic.main.card_year.view.*


class YearAdapter(
    private val yearlist: Array<String>,
    private val context: Context
) : RecyclerView.Adapter<YearAdapter.MyViewHolder>() {
    lateinit var onItemClick: OnClickListener.OnItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_year, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = yearlist[position]
        holder.itemView.setOnClickListener {
            /*val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)*/
            onItemClick.onItemClick(holder.itemView, position)
        }
    }

    override fun getItemCount() = yearlist.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.lbl_year
    }
}

