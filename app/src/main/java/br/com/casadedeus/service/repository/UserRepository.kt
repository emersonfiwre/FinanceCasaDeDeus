package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.auth.*
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
                    if (user != null) {
                        get(user.uid, listener)// nao sei se vai funcionar, se funcionar fazer isso com save
                    }

                } else {
//                    listener.onFailure(task.exception!!.message!!)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        //listener.onFailure("FirebaseAuthWeakPasswordException: ${e.message}")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        listener.onFailure(context.getString(R.string.email_badly_formatted))
                        Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    } catch (e: FirebaseAuthUserCollisionException) {
                        //listener.onFailure("FirebaseAuthUserCollisionException: ${e.message}")
                    } catch (e: Exception) {
                        listener.onFailure(context.getString(R.string.user_not_found_login))
                        Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    }
                    //Verificar esses errors
                    /*if (user != null) {
                        if (user!!.password == userModel.password) {
                            listener.onSuccess(user!!)
                        } else {
                            listener.onFailure(context.getString(R.string.password_incorrect))
                        }
                    } else {
                        listener.onFailure(context.getString(R.string.email_not_found))
                    }*/
                }
            }
    }

    private fun get(uid: String, listener: OnCallbackListener<UserModel>) {
        var user: UserModel? = null
        mDatabase.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val key = document.id
                    val name = document.data?.get("name") as String
                    val email = document.data?.get("email") as String
                    val password = document.data?.get("password") as String
                    user = UserModel(
                        key,
                        name,
                        email,
                        password
                    )
                    listener.onSuccess(user!!)
                } else {
                    listener.onFailure(context.getString(R.string.user_not_found))
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

                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    //user.uid
                    val user = hashMapOf(
                        "email" to userModel.email,
                        "name" to userModel.name,
                        "password" to userModel.password // acho que não preciso guardar a senha
                    )
                    mDatabase.collection("users").document(firebaseUser.uid)
                        //.set(userModel)//testing
                        .set(user)//recommend
                        .addOnSuccessListener {
                            listener.onSuccess(true)
                        }
                        .addOnFailureListener {
                            val message = it.message.toString()
                            Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, message)
                            listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                        }
                } else {
                    listener.onFailure(task.exception!!.message.toString())
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        listener.onFailure("FirebaseAuthWeakPasswordException: ${e.message}")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        listener.onFailure("FirebaseAuthInvalidCredentialsException: ${e.message}")
                        Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    } catch (e: FirebaseAuthUserCollisionException) {
                        listener.onFailure("FirebaseAuthUserCollisionException: ${e.message}")
                    } catch (e: Exception) {
                        listener.onFailure("Exception: ${e.message}")
                        Log.e(TransactionConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    }
                }
                // ...
            }

    }

    fun update(userModel: UserModel, listener: OnCallbackListener<Boolean>) {// fazer tela pro usuario editar suas informacoes
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

    fun logout() {
        mAuth.signOut()
    }
}