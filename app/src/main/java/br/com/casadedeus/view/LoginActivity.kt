package br.com.casadedeus.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_password

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: UserViewModel
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

    }

    private fun setListeners() {
        btn_login.setOnClickListener(this)
        btn_cadastrar.setOnClickListener(this)
        txt_forgot_password.setOnClickListener(this)
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
        }

    }

    private fun observe() {
        mViewModel.login.observe(this, Observer {
            loading(false)
            if (it.success()) {
                Toast.makeText(this, "Login com sucesso!", Toast.LENGTH_SHORT).show()
                //successLogin()
            } else {
                when (it.failure()) {
                    "Por favor, confirme seu e-mail." -> {
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
                Toast.makeText(this, "E-mail de redefinação de senha enviado.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })

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

    private fun successLogin() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun sendVerificationEmail(title: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage("Para prosseguir é necessário que você confirme seu e-mail.")
        dialogBuilder.setPositiveButton("Reenviar o email de verificação") { dialog, which -> mViewModel.verifcationEmail() }
        dialogBuilder.setNegativeButton("OK") { dialog, which -> }
        dialogBuilder.show()
    }

    private fun forgotPassword() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_forgot_password, null)
        dialogBuilder.setView(dialogView)
        val editText: EditText = dialogView.findViewById(R.id.edit_email)
        dialogBuilder.setPositiveButton("Redefinir senha") { dialog, which ->
            mViewModel.resetPassword(editText.text.toString())
        }
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { dialog, which -> }
        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }
}