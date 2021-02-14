package br.com.casadedeus.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import java.util.concurrent.Executor

class SplashActivity : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mViewModel.verifyLoggedUser()
        observer()
        //Fazer o currency onde coloca os valores // feito
        //Deixar o usuário editar o nome dele //feito
        //Trocar o icone do app // feito
        //Trocar as cores do app // feito
        //Passar os textos para o arquivo de string, fazer string in english// feito
        //Colocar fontes customizadas*// feito
        //Verificar o reenvio do email de confirmação* os formularios no firebase// feito
        //Verificar se vai deixar o  usuario trocar o email// feito
        //Verificar se vai fazer o FingerPrint// feito

        //Trocar o nome do app//
        //Trocar o nome do app e o package

    }

    private fun observer() {
        mViewModel.loggedUser.observe(this, Observer {
            Handler().postDelayed({
                if (it.isProtected) {
                    if (it.isLogged) {
                        if (it.isFingerprint) {
                            showAuthentication()
                        } else {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                } else {
                    if (it.isLogged) {
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }, 2000)
        })

    }

    private fun showAuthentication() {
        //Executor
        val executor: Executor = ContextCompat.getMainExecutor(this)

        //BiometricPrompt
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                    super.onAuthenticationSucceeded(result)
                }

                private var contt = 0
                override fun onAuthenticationFailed() {
                    if (contt >= 5){
                        startActivity(Intent(applicationContext, LoginActivity::class.java))
                        finish()
                    }
                    contt++
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    startActivity(Intent(applicationContext, LoginActivity::class.java))
                    finish()
                    super.onAuthenticationError(errorCode, errString)
                }

            })
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.app_name))
            .setSubtitle(getString(R.string.authentication_to_access))
            .setDescription(getString(R.string.tap_cancel_call_login))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(info)
    }
}