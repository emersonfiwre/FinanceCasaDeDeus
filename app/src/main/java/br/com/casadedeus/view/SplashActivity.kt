package br.com.casadedeus.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel

class SplashActivity : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mViewModel.verifyLoggedUser()
        observer()
        //Fazer o currency onde coloca os valores
        //Deixar o usuário editar o nome dele
        //Trocar o nome do app e o package
        //Trocar o icone do app // feito
        //Trocar o nome do app //
        //Trocar as cores do app // feito
        //Verificar o reenvio do email de confirmação// os formularios no firebase
        //Passar os textos para o arquivo de string
    }

    private fun observer() {
        mViewModel.loggedUser.observe(this, Observer {
            Handler().postDelayed({
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }, 2000)
        })
    }
}