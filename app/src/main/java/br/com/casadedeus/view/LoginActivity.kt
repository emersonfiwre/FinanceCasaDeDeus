package br.com.casadedeus.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: UserViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 120
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Design
        //https://dribbble.com/shots/5739539-Log-in-Sign-Up-Screen
        //https://dribbble.com/shots/5271131-Login-Sign-up-screen

        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        //Eventos de clicks
        setListeners()

        // Cria observadores
        observe()

        setupGoogleSignIn()
    }


    private fun setupGoogleSignIn() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setListeners() {
        btn_login.setOnClickListener(this)
        btn_cadastrar.setOnClickListener(this)
        txt_forgot_password.setOnClickListener(this)
        btn_singin_google.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.btn_login) {
            loading()
            val user = edit_user.text.toString()
            val password = edit_password.text.toString()
            mViewModel.doLogin(user, password)

        } else if (id == R.id.btn_cadastrar) {
            startActivity(Intent(this, RegisterActivity::class.java))
        } else if (id == R.id.txt_forgot_password) {
            forgotPassword()
        } else if (id == R.id.btn_singin_google) {
            loading()
            signInGoogle()
        }

    }

    private fun observe() {
        mViewModel.login.observe(this, Observer {
            loading(false)
            if (it.success()) {
                successLogin()
            } else {
                when (it.failure()) {
                    getString(R.string.confirm_email) -> {
                        sendVerificationEmail(it.failure())
                    }
                    else -> {
                        Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
                    }
                }

            }
        })
        mViewModel.forgotPassword.observe(this, Observer {
            if (it.success()) {
                Toast.makeText(this, getString(R.string.sent_password_reset), Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    mViewModel.doLoginWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Toast.makeText(this, getString(R.string.error_login_with_google), Toast.LENGTH_SHORT)
                        .show()
                    Log.e(ViewConstants.LOG.LOGIN_ACTIVITY_ERROR, "Google sign in failed", e)
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this, getString(R.string.error_login_with_google), Toast.LENGTH_SHORT)
                Log.e(ViewConstants.LOG.LOGIN_ACTIVITY_ERROR, exception.toString())
                exception!!.printStackTrace()
            }
        }

    }

    private fun successLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loading(isLoad: Boolean = true) {
        if (isLoad) {
            btn_login.visibility = View.GONE
            pg_login.visibility = View.VISIBLE
        } else {
            btn_login.visibility = View.VISIBLE
            pg_login.visibility = View.GONE
        }

    }

    private fun sendVerificationEmail(title: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(getString(R.string.request_email_confirm))
        dialogBuilder.setPositiveButton(getString(R.string.resend_email_verification)) { dialog, which -> mViewModel.verifcationEmail() }
        dialogBuilder.setNegativeButton("OK") { dialog, which -> }
        dialogBuilder.show()
    }

    private fun forgotPassword() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_forgot_password, null)
        dialogBuilder.setView(dialogView)
        val editText: EditText = dialogView.findViewById(R.id.edit_email)
        dialogBuilder.setPositiveButton(getString(R.string.reset_password)) { dialog, which ->
            mViewModel.resetPassword(editText.text.toString())
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}