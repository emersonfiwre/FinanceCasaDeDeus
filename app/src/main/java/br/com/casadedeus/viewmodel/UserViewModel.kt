package br.com.casadedeus.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.UserConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.SecurityPreferences
import br.com.casadedeus.service.repository.UserRepository
import br.com.casadedeus.service.utils.Utils

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val context: Context = application.applicationContext
    private val mSecurityPreferences = SecurityPreferences(context)
    private val mRepository = UserRepository(context)

    private val mLogin = MutableLiveData<ValidationListener>()
    var login: LiveData<ValidationListener> = mLogin

    private val mCreateUser = MutableLiveData<ValidationListener>()
    var createUser: LiveData<ValidationListener> = mCreateUser

    private val mForgotPassword = MutableLiveData<ValidationListener>()
    var forgotPassword: LiveData<ValidationListener> = mForgotPassword

    private val mResetName = MutableLiveData<ValidationListener>()
    var resetName: LiveData<ValidationListener> = mResetName

    //Profile
    private val mCurrentUser = MutableLiveData<UserModel>()
    var currentUser: LiveData<UserModel> = mCurrentUser

    private val mValidation = MutableLiveData<ValidationListener>()
    var validation: LiveData<ValidationListener> = mValidation

    // Login usando SharedPreferences
    private val mLoggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = mLoggedUser

    fun doLogin(email: String?, password: String?) {
        if (email.isNullOrEmpty()) {
            mLogin.value = ValidationListener(context.getString(R.string.please_insert_email))
            return
        }
        if (password.isNullOrEmpty()) {
            mLogin.value = ValidationListener(context.getString(R.string.please_insert_password))
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

    fun doLoginWithGoogle(idToken: String) {
        mRepository.loginWithGoogle(idToken, object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
                mSecurityPreferences.store(UserConstants.SHARED.USER_KEY, result.key)
                mSecurityPreferences.store(UserConstants.SHARED.USER_NAME, result.name)
                mLogin.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mLogin.value = ValidationListener(message)
            }
        })
    }

    fun create(userModel: UserModel, confirmPass: String) {
        if (userModel.email.isEmpty()) {
            mCreateUser.value = ValidationListener(context.getString(R.string.please_insert_name))
            return
        }
        if (userModel.email.isEmpty()) {
            mCreateUser.value = ValidationListener(context.getString(R.string.please_insert_email))
            return
        }
        if (userModel.password.isEmpty()) {
            mCreateUser.value =
                ValidationListener(context.getString(R.string.please_insert_password))
            return
        }
        if (confirmPass.isEmpty() || !userModel.password.equals(confirmPass)) {
            mCreateUser.value = ValidationListener(context.getString(R.string.different_passwords))
            return
        }
        //val user = UserModel(email = email, password = password)
        mRepository.create(userModel, object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
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
            mForgotPassword.value = ValidationListener(context.getString(R.string.type_email))
            return
        }
        if (!Utils.validateEmailFormat(email)) {
            mForgotPassword.value = ValidationListener(context.getString(R.string.type_email_valid))
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

    fun getCurrentUser() {
        mRepository.currentUser(object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
                mCurrentUser.value = result
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })

    }

    fun doLogout() {
        mRepository.logout(object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mSecurityPreferences.remove(UserConstants.SHARED.USER_KEY)
                mSecurityPreferences.remove(UserConstants.SHARED.USER_NAME)
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun resetName(name: String) {
        if (name.isEmpty()) {
            return
        }
        val key = mSecurityPreferences.get(UserConstants.SHARED.USER_KEY)

        mRepository.update(UserModel(key = key, name = name), object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mSecurityPreferences.store(UserConstants.SHARED.USER_NAME, name)
                mResetName.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mResetName.value = ValidationListener(message)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        /*val key = mSecurityPreferences.get(UserConstants.SHARED.USER_KEY)
        val userName = mSecurityPreferences.get(UserConstants.SHARED.USER_NAME)

        // Se token e person key forem diferentes de vazio, usuário está logado
        val logged = (key != "" && userName != "")*/
        mRepository.currentUser(object : OnCallbackListener<UserModel> {
            override fun onSuccess(result: UserModel) {
                mLoggedUser.value = true
            }

            override fun onFailure(message: String) {
                mSecurityPreferences.remove(UserConstants.SHARED.USER_KEY)
                mSecurityPreferences.remove(UserConstants.SHARED.USER_NAME)
                mLoggedUser.value = false
            }
        })

    }

}