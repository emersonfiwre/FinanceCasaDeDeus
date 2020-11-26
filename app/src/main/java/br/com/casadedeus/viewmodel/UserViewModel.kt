package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.User
import br.com.casadedeus.model.UserModel
import java.lang.Exception

class UserViewModel : ViewModel() {
    private val mModel = UserModel()

    private val mLogin = MutableLiveData<Boolean>()
    var login: LiveData<Boolean> = mLogin

    fun login(email: String, password: String) {
        val user = User(email = email, password = password)
        try {
            mLogin.value = mModel.getLogin(user)
        } catch (e: Exception) {
            mLogin.value = false
            e.printStackTrace()
        }
    }
}