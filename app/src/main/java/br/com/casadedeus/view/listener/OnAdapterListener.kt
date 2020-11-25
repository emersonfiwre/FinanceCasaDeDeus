package br.com.casadedeus.view.listener

import android.view.View

interface OnAdapterListener {
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    //fun onClickListener(view: View)
    //fun onItemLongPressClickListener(view: View, position:Int)
}