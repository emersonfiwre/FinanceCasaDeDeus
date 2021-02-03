package br.com.casadedeus.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_password
import kotlinx.android.synthetic.main.activity_register.*

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

        }

    }

    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                Toast.makeText(this, "Login com sucesso!", Toast.LENGTH_SHORT).show()
                //successLogin()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
            }
            loading(false)
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
}