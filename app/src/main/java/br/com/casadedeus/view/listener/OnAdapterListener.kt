package br.com.casadedeus.view.listener

import android.view.View

interface OnAdapterListener {
    interface OnItemClickListener<in V>{
        fun onItemClick(item: V)
    }
    //fun onClickListener(view: View)
    //fun onItemLongPressClickListener(view: View, position:Int)
}