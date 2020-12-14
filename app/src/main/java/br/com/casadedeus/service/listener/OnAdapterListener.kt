package br.com.casadedeus.service.listener

interface OnAdapterListener {
    interface OnItemClickListener<in V>{
        fun onItemClick(item: V)
    }
    //fun onClickListener(view: View)
    //fun onItemLongPressClickListener(view: View, position:Int)
}