package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserRepository(private val context: Context) {
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mAuth: FirebaseAuth = Firebase.auth

    fun login(userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        mAuth.signInWithEmailAndPassword(userModel.email, userModel.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("UserRepository", "signInWithEmail:success")
                    val user = mAuth.currentUser

                } else {
                }
            }

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

        mAuth.createUserWithEmailAndPassword(userModel.email, userModel.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("UserRepository", "createUserWithEmail:success")
                    val user = mAuth.currentUser
                } else {

                }

                // ...
            }
        /*mDatabase.collection("users")
            .add(userModel)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }*/
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