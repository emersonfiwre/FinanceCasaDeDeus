package br.com.casadedeus.service.listener

interface OnItemClickListener<in V> {
    fun onItemClick(item: V)
    fun onDeleteClick(id: String)
}
//fun onClickListener(view: View)
//fun onItemLongPressClickListener(view: View, position:Int)
