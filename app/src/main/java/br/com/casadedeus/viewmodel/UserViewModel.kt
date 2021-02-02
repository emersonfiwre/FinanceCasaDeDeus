package br.com.casadedeus.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.SecurityPreferences
import br.com.casadedeus.service.repository.UserRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val mContext: Context = application.applicationContext
    private val mSecurityPreferences = SecurityPreferences(application)
    private val mRepository = UserRepository(mContext)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mCreateUser = MutableLiveData<ValidationListener>()
    var createUser: LiveData<ValidationListener> = mCreateUser

    // Login usando SharedPreferences
    private val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = mLoggedUser

    fun doLogin(email: String?, password: String?) {
        if (email.isNullOrEmpty()) {
            mLogin.value = ValidationListener("Por favor, insira um email")
            return
        }
        if (password.isNullOrEmpty()) {
            mLogin.value = ValidationListener("Por favor, insira a senha")
            return
        }
        val user = UserModel(email = email, password = password)
        mRepository.login(user, object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
                // Salvar dados do usuário no SharePreferences
                mSecurityPreferences.store(TransactionConstants.SHARED.USER_KEY, result.key)
                result.name.let {
                    mSecurityPreferences.store(
                        TransactionConstants.SHARED.USER_NAME,
                        it
                    )
                }

                // Informa sucesso
                mLogin.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationListener(message)
            }

        })
    }
    fun create(userModel: UserModel, confirmPass:String) {
        if (userModel.email.isNullOrEmpty()) {
            mCreateUser.value = ValidationListener("Por favor, insira seu nome.")
            return
        }
        if (userModel.email.isNullOrEmpty()) {
            mCreateUser.value = ValidationListener("Por favor, insira um email.")
            return
        }
        if (userModel.password.isNullOrEmpty()) {
            mCreateUser.value = ValidationListener("Por favor, insira a senha.")
            return
        }
        if (confirmPass.isNullOrEmpty()|| !userModel.password.equals(confirmPass)) {
            mCreateUser.value = ValidationListener("A senhas estão diferentes. Por favor, confime a senha.")
            return
        }
        //val user = UserModel(email = email, password = password)
        mRepository.create(userModel, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                // Salvar dados do usuário no SharePreferences
                mSecurityPreferences.store(TransactionConstants.SHARED.USER_KEY, userModel.key)

                userModel.name.let {
                    mSecurityPreferences.store(
                        TransactionConstants.SHARED.USER_NAME,
                        it
                    )
                }

                // Informa sucesso
                mCreateUser.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mCreateUser.value = ValidationListener(message)
            }

        })
    }


    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val key = mSecurityPreferences.get(TransactionConstants.SHARED.USER_KEY)
        val userKey = mSecurityPreferences.get(TransactionConstants.SHARED.USER_NAME)

        // Se token e person key forem diferentes de vazio, usuário está logado
        val logged = (key != "" && userKey != "")

        // Atualiza o valor
        mLoggedUser.value = logged
    }

}