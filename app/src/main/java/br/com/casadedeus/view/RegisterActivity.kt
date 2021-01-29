package br.com.casadedeus.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.casadedeus.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListeners()
    }

    private fun setListeners() {
        img_back_singup.setOnClickListener(this)
        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_back_singup -> onBackPressed()
            R.id.btn_register ->{}

        }
    }

}