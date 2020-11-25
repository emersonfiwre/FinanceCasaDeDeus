package br.com.casadedeus.view.listener

import android.view.View

interface OnFragmentListener {
    interface OnClickFragmentListener {
        fun onClick(view: View)
    }
    interface OnBackPressedFragmentListener {
        fun onBackPressed():Boolean
    }
    //fun onClickListener(view: View)
    //fun onItemLongPressClickListener(view: View, position:Int)
}