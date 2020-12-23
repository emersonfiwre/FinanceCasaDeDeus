package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository(private val context: Context) {
    private val mDatabase = FirebaseFirestore.getInstance()

    fun login(userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        var user: UserModel? = null
        mDatabase.collection("users").whereEqualTo("email", userModel.email)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val name = document.data["name"] as String
                        val email = document.data["email"] as String
                        val password = document.data["password"] as String
                        user = UserModel(
                            key,
                            name,
                            email,
                            password
                        )
                    }
                }
                if (user != null) {
                    if (user!!.password == userModel.password) {
                        listener.onSuccess(user!!)
                    } else {
                        listener.onFailure(context.getString(R.string.password_incorrect))
                    }
                } else {
                    listener.onFailure(context.getString(R.string.email_not_found))
                }
            }.addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun save(userModel: UserModel, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users")
            .add(userModel)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun update(userModel: UserModel, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users")
            .document(userModel.key)
            .set(userModel)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }
}