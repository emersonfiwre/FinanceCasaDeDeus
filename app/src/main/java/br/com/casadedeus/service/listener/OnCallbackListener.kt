package br.com.casadedeus.service.listener

interface OnCallbackListener<T> {
    fun onSuccess(result: T)
    fun onFailure(message: String)
}