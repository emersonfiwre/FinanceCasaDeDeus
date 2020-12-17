package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.repository.UserRepository

class UserViewModel : ViewModel() {
    private val mRepository = UserRepository()

    private val mLogin = MutableLiveData<Boolean>()
    var login: LiveData<Boolean> = mLogin

    fun login(email: String, password: String) {
        val user = UserModel(email = email, password = password)
        mLogin.value = mRepository.getLogin(user)

        mLogin.value = false


    }
}