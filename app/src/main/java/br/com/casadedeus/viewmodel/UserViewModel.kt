package br.com.casadedeus.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.constants.UserConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.SecurityPreferences
import br.com.casadedeus.service.repository.UserRepository
import br.com.casadedeus.service.utils.Utils
import com.google.firebase.firestore.auth.User

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val mContext: Context = application.applicationContext
    private val mSecurityPreferences = SecurityPreferences(mContext)
    private val mRepository = UserRepository(mContext)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mCreateUser = MutableLiveData<ValidationListener>()
    var createUser: LiveData<ValidationListener> = mCreateUser

    private val mForgotPassword = MutableLiveData<ValidationListener>()
    var forgotPassword: LiveData<ValidationListener> = mForgotPassword

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
                mSecurityPreferences.store(UserConstants.SHARED.USER_KEY, result.key)
                mSecurityPreferences.store(UserConstants.SHARED.USER_NAME, result.name)
                // Informa sucesso
                mLogin.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationListener(message)
            }

        })
    }

    fun create(userModel: UserModel, confirmPass: String) {
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
        if (confirmPass.isNullOrEmpty() || !userModel.password.equals(confirmPass)) {
            mCreateUser.value =
                ValidationListener("A senhas estão diferentes. Por favor, confime a senha.")
            return
        }
        //val user = UserModel(email = email, password = password)
        mRepository.create(userModel, object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
                // Salvar dados do usuário no SharePreferences
                mSecurityPreferences.store(UserConstants.SHARED.USER_KEY, userModel.key)
                mSecurityPreferences.store(UserConstants.SHARED.USER_NAME, result.name)

                // Informa sucesso
                mCreateUser.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mCreateUser.value = ValidationListener(message)
            }

        })
    }

    fun verifcationEmail() {
        mRepository.verificationEmail(object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                //
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationListener(message)
            }

        })
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            mForgotPassword.value = ValidationListener("Digite um email.")
            return
        }
        if (!Utils.validateEmailFormat(email)) {
            mForgotPassword.value = ValidationListener("Digite um email válido.")
            return
        }
        mRepository.resetUserPassword(email, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mForgotPassword.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mForgotPassword.value = ValidationListener(message)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val key = mSecurityPreferences.get(UserConstants.SHARED.USER_KEY)
        val userKey = mSecurityPreferences.get(UserConstants.SHARED.USER_NAME)

        // Se token e person key forem diferentes de vazio, usuário está logado
        val logged = (key != "" && userKey != "")

        // Atualiza o valor
        mLoggedUser.value = logged
    }

}