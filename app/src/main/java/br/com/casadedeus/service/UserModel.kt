package br.com.casadedeus.service

import br.com.casadedeus.beans.User
import br.com.casadedeus.service.repository.UserRepository
import java.lang.Exception

class UserModel {
    private val userRepository = UserRepository()

    @Throws(Exception::class)
    fun getLogin(user:User):   Boolean{
        return if(user.email.isNotEmpty() && user.password.isNotEmpty()){
            userRepository.getLogin(user)
        }else{
            throw Exception("Os campos nome ou senha estão vázios")
        }
    }

}