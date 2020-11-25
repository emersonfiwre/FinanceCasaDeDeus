package br.com.casadedeus.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.view.listener.OnAdapterListener
import kotlinx.android.synthetic.main.card_year.view.*


class YearAdapter : RecyclerView.Adapter<YearAdapter.MyViewHolder>() {

    private var mYearList: List<String> = arrayListOf()
    lateinit var mListener: OnAdapterListener.OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_year, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = mYearList[position]
        holder.itemView.setOnClickListener {
            /*val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)*/
            mListener.onItemClick(holder.itemView, position)
        }
    }
    fun notifyChanged(it: List<String>?) {
        if (it != null) {
            mYearList = it
            notifyDataSetChanged()
        }
    }
    fun attachListener(listener: OnAdapterListener.OnItemClickListener) {
        mListener = listener
    }

    override fun getItemCount() = mYearList.size

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.lbl_year
    }
}

