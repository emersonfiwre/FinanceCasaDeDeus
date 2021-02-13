package br.com.casadedeus.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.beans.UserModel
import br.com.casadedeus.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setListeners()

        //Cria observadores
        observe()
    }

    private fun observe() {
        mViewModel.createUser.observe(this, Observer {
            if (it.success()) {
                Toast.makeText(this, getString(R.string.confirm_email), Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
            }
            loading(false)
        })
    }

    private fun setListeners() {
        img_back_singup.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back_singup -> onBackPressed()
            R.id.btn_register -> {
                loading()
                val user = UserModel(
                    name = edit_name.text.toString(),
                    email = edit_email.text.toString(),
                    password = edit_password.text.toString()
                )
                mViewModel.create(user, edit_confirm_password.text.toString())
            }

        }
    }

    private fun loading(isLoad: Boolean = true) {
        if (isLoad) {
            btn_register.visibility = View.GONE
            pg_register.visibility = View.VISIBLE
        } else {
            btn_register.visibility = View.VISIBLE
            pg_register.visibility = View.GONE
        }

    }


}