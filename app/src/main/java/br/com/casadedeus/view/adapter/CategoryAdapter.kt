package br.com.casadedeus.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.service.listener.OnItemClickListener
import kotlinx.android.synthetic.main.card_category.view.*

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    private var mList: List<String> = arrayListOf()
    private var mListener: OnItemClickListener<String>? = null

    //val format = SimpleDateFormat("EEE MMM dd kk:mm:ss zXXX yyyy", local)// dia por extenso

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_category, parent, false)
        return MyViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = mList[position]
        holder.binCategory(category)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    fun notifyChanged(it: List<String>?) {
        if (it != null) {
            mList = it
            notifyDataSetChanged()
        }
    }


    fun attachListener(listener: OnItemClickListener<String>) {
        mListener = listener
    }

    class MyViewHolder(itemView: View, val listener: OnItemClickListener<String>?) :
        RecyclerView.ViewHolder(itemView) {
        private val mCardCategory = itemView.card_category
        private var mCategory: TextView = itemView.txt_category
        private var mImage: ImageView = itemView.img_category

        fun binCategory(category: String) {
            mCategory.text = category
            //mImage.setImageDrawable()

            mCardCategory.setOnClickListener { listener?.onItemClick(category) }

        }
    }
}