package br.com.casadedeus.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity

import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.MainActivity
import br.com.casadedeus.R
import br.com.casadedeus.YearActivity
import br.com.casadedeus.`interface`.ItemClickListener
import kotlinx.android.synthetic.main.card_year.view.*


class YearAdapter(private val yearlist:Array<String>,
                  private val context:Context): RecyclerView.Adapter<YearAdapter.MyViewHolder>() {
    lateinit var itemClickListener: ItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_year,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = yearlist[position]
        holder.itemView.setOnClickListener{
            val intent = Intent(context, MainActivity:: class.java)
            context.startActivity(intent)

        }
    }
    fun setOnItemClickListener(itemClick:ItemClickListener){
        this.itemClickListener = itemClick
    }

    override fun getItemCount() =  yearlist.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title = itemView.lbl_year

        override fun onClick(p0: View?) {
            if (p0 != null) {
                println("chega aqui1")
                itemClickListener.onItemClickListener(p0, adapterPosition)
            }
        }

    }
}

