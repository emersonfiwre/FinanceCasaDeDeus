package br.com.casadedeus.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.view.*

class SplashActivity : AppCompatActivity() {
    private lateinit var mViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        mViewModel.verifyLoggedUser()
        observer()
    }

    private fun observer() {
        mViewModel.loggedUser.observe(this, Observer {
            Handler().postDelayed({
                if (it) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }, 2000)
        })
    }
}