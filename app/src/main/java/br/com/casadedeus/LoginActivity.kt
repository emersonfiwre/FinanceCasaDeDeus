package br.com.casadedeus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLogin(view: View){
        println("click open MainActivity")
        val intent = Intent(this, YearActivity:: class.java)
        startActivity(intent)

    }
}