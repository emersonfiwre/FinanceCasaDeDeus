package br.com.casadedeus.`interface`

import android.view.View

interface OnClickListener {
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
    interface OnMonthClickListener {
        fun onMonthClick(month:String)
    }
    interface OnClickFragmentListener {
        fun onClick(view: View)
    }
    //fun onClickListener(view: View)
    //fun onItemLongPressClickListener(view: View, position:Int)
}