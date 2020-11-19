package br.com.casadedeus.viewmodel

import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.User
import br.com.casadedeus.model.UserModel

class UserViewModel: ViewModel() {
    private val userModel = UserModel()

    fun login(user:User){
        userModel.getLogin(user)

    }
}