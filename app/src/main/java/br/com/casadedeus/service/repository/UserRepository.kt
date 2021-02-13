package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
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
                        listener.onFailure(context.getString(R.string.user_not_found))
                    }
                }.addOnFailureListener {
                    val message = it.message.toString()
                    Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                    it.printStackTrace()
                }
        } else {
            listener.onFailure(context.getString(R.string.currentuser_not_found))
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
                    listener.onFailure(context.getString(R.string.confirm_email))
                }
            }.addOnFailureListener {
                val errorCode = (it as FirebaseAuthException).errorCode
                val errorMessage = UserConstants.ERRORS.AUTH_ERRORS[errorCode]
                    ?: R.string.error_login_default_error
                listener.onFailure(context.getString(errorMessage))

                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                it.printStackTrace()
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
                        listener.onFailure(context.getString(R.string.user_not_found))
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
                    Log.e(UserConstants.ERRORS.USER_REPOSITORY, task.exception!!.message!!)
                    listener.onFailure(context.getString(R.string.error_login_with_google))
                    task.exception!!.printStackTrace()
                }

            }
    }

    fun create(userModel: UserModel, listener: OnCallbackListener<UserModel>) {
        mAuth.createUserWithEmailAndPassword(userModel.email, userModel.password)
            .addOnSuccessListener { task ->
                mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val firebaseUser: FirebaseUser = task.user!!
                        save(firebaseUser.uid, userModel, listener)
                    } else {
                        listener.onFailure(context.getString(R.string.email_verification_error))
                        Log.e(UserConstants.ERRORS.USER_REPOSITORY, it.exception!!.message!!)
                        it.exception!!.printStackTrace()
                    }
                }

            }.addOnFailureListener {
                val errorCode = (it as FirebaseAuthException).errorCode
                val errorMessage = UserConstants.ERRORS.AUTH_ERRORS[errorCode]
                    ?: R.string.error_login_default_error
                listener.onFailure(context.getString(errorMessage))

                val message = it.message.toString()
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
                it.printStackTrace()
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
                it.printStackTrace()
            }
    }

    fun verificationEmail(listener: OnCallbackListener<Boolean>) {
        mAuth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                listener.onFailure(context.getString(R.string.email_verification_success))
            } else {
                Log.e(UserConstants.ERRORS.USER_REPOSITORY, it.exception!!.message!!)
                listener.onFailure(context.getString(R.string.email_verification_error))
                it.exception!!.printStackTrace()
            }
        }
    }

    fun resetUserPassword(email: String, listener: OnCallbackListener<Boolean>) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            listener.onSuccess(true)
        }.addOnFailureListener {
            val message = it.message.toString()
            Log.e(UserConstants.ERRORS.USER_REPOSITORY, message)
            listener.onFailure(context.getString(R.string.email_not_found))
            it.printStackTrace()
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