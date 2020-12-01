package br.com.casadedeus.view.listener

interface OnCallbackListener<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String)
}