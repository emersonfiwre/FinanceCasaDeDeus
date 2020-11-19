package br.com.casadedeus.model

import br.com.casadedeus.beans.User
import br.com.casadedeus.model.repository.UserRepository
import java.lang.Exception

class UserModel {
    private val userRepository = UserRepository()

    fun getLogin(user:User){
        if(user.name.isNotEmpty() && user.password.isNotEmpty()){
            userRepository.getLogin(user)
        }else{
           throw Exception("Os campos nome ou senha estão vázios")
        }
    }

}