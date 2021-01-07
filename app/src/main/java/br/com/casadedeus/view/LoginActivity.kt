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
        button_login.setOnClickListener(this)
        button_cadastrar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.button_login) {

//            val user = edit_user.text.toString()
//            val password = edit_password.text.toString()
//            mViewModel.doLogin(user, password)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        } else if (id == R.id.button_cadastrar) {
            Toast.makeText(this, "Cadastrar em implementação", Toast.LENGTH_SHORT).show()
        }

    }

    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
//                val intent = Intent(this, SelectYearActivity::class.java)
//                startActivity(intent)
            } else {
                //Toast.makeText(this,it.failure(),Toast.LENGTH_SHORT).show()
            }
        })
    }
}