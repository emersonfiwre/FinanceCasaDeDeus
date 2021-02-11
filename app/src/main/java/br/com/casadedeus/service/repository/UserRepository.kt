package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.service.constants.GoalConstants
import br.com.casadedeus.service.constants.UserConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class UserRepository(private val context: Context) {
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mAuth: FirebaseAuth = Firebase.auth


    fun currentUser(listener: OnCallbackListener<UserModel>) {
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            mDatabase.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val key = document.id
                        val name = document.data?.get(UserConstants.NAME) as String
                        val email = document.data?.get(UserConstants.EMAIL) as String
                        val user = UserModel(
                            key = key,
                            name = name,
                            email = email,
                            profilePhotoUrl = currentUser.photoUrl
                        )
                        listener.onSuccess(user)
                    } else {
                        listener.onFailure(context.getString(R.string.user_not_found_login))
                    }
                }.addOnFailureListener {
                    val message = it.message.toString()
                    Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                }
        } else {
            listener.onFailure("Desculpe, não foi possível recuperar o usuário.")
        }

    }

    fun login(userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        mAuth.signInWithEmailAndPassword(userModel.email, userModel.password)
            .addOnSuccessListener { task ->
                // Sign in success, update UI with the signed-in user's information
                if (mAuth.currentUser!!.isEmailVerified) {
                    val user = mAuth.currentUser
                    if (user != null) {
                        userModel.key = user.uid
                        get(userModel, listener)
                    }
                } else {
                    listener.onFailure("Por favor, confirme seu e-mail.")
                }
            }.addOnFailureListener {
                val errorCode = (it as FirebaseAuthException).errorCode
                val errorMessage = UserConstants.ERRORS.AUTH_ERRORS[errorCode]
                    ?: R.string.error_login_default_error
                listener.onFailure(context.getString(errorMessage))

                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
            }
    }

    private fun get(
        userModel: UserModel,
        listener: OnCallbackListener<UserModel>,
        isSave: Boolean = false
    ) {
        mDatabase.collection("users").document(userModel.key).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val key = document.id
                    val name = document.data?.get(UserConstants.NAME) as String
                    val email = document.data?.get(UserConstants.EMAIL) as String
                    val user = UserModel(
                        key,
                        name,
                        email
                    )
                    listener.onSuccess(user)
                } else {
                    if (isSave) {
                        save(userModel.key, userModel, listener)
                    } else {
                        listener.onFailure(context.getString(R.string.user_not_found_login))
                    }
                }
            }.addOnFailureListener {
                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun loginWithGoogle(idToken: String, listener: OnCallbackListener<UserModel>) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAuth.currentUser
                    get(
                        UserModel(
                            key = user?.uid!!,
                            name = user.displayName!!,
                            email = user.email!!
                        ), listener, true
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Log.e(
                        UserConstants.ERRORS.USER_REPOSITORY,
                        "signInWithCredential:failure",
                        task.exception
                    )
                    listener.onFailure("Erro ao realizar login com Google")
                }

            }
    }

    fun create(userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        mAuth.createUserWithEmailAndPassword(userModel.email, userModel.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            save(firebaseUser.uid, userModel, listener)
                        } else {
                            listener.onFailure("Desculpe, não foi possível enviar o email de verificação. Por favor, tente mais tarde.")
                            Log.e(
                                UserConstants.ERRORS.USER_REPOSITORY,
                                it.exception!!.message!!
                            )
                        }
                    }

                } else {
                    //listener.onFailure(task.exception!!.message.toString())
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        listener.onFailure("FirebaseAuthWeakPasswordException: ${e.message}")
                        //listener.onFailure("A senha deve ter no minimo 6 caracteres.")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        listener.onFailure(context.getString(R.string.email_badly_formatted))
                        //listener.onFailure("FirebaseAuthInvalidCredentialsException: ${e.message}")
                        Log.e(UserConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    } catch (e: FirebaseAuthUserCollisionException) {
                        listener.onFailure("FirebaseAuthUserCollisionException: ${e.message}")
                    } catch (e: Exception) {
                        //listener.onFailure("Exception: ${e.message}")
                        Log.e(UserConstants.ERRORS.USER_REPOSITORY, e.message!!)
                    }
                }
            }

    }

    private fun save(uid: String, userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        /*val user = hashMapOf(
            "email" to userModel.email,
            "name" to userModel.name,
            "password" to userModel.password
        )// acho que não preciso guardar a senha*/

        mDatabase.collection("users").document(uid)
            .set(userModel)//testing
            //.set(user)//recommend
            .addOnSuccessListener {
                listener.onSuccess(userModel.apply { key = uid })
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun verificationEmail(listener: OnCallbackListener<Boolean>) {
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                listener.onFailure("E-mail de verificaçaão enviado com sucesso.")
            } else {
                listener.onFailure("Desculpe, não foi possível enviar o email de verificação. Por favor, tente mais tarde.")
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, it.exception!!.message!!)
            }
        }
    }

    fun resetUserPassword(email: String, listener: OnCallbackListener<Boolean>) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            listener.onSuccess(true)
        }.addOnFailureListener {
            val message = it.message.toString()
            Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
            listener.onFailure("Este email não está cadastrado. Por favor, realize o cadastro.")
            //listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
        }
    }

    fun update(
        userModel: UserModel,
        listener: OnCallbackListener<Boolean>
    ) {// fazer tela pro usuario editar suas informacoes
        mDatabase.collection("users")
            .document(userModel.key)
            .update(UserConstants.NAME, userModel.name)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun logout(listener: OnCallbackListener<Boolean>) {
        try {
            mAuth.signOut()
            listener.onSuccess(true)
        } catch (e: Exception) {
            listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
        }

    }
}