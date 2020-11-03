package br.com.casadedeus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Design
        //https://dribbble.com/shots/5739539-Log-in-Sign-Up-Screen
        //https://dribbble.com/shots/5271131-Login-Sign-up-screen
    }

    fun onLogin(view: View){
        val intent = Intent(this, SelectYearActivity:: class.java)
        startActivity(intent)

    }
}