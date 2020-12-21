package br.com.casadedeus.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.casadedeus.R
import br.com.casadedeus.service.constants.ViewConstants

class MainActivity : AppCompatActivity() {
    //    private lateinit var mYearFragment: YearFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Design
        //https://dribbble.com/shots/14359607-Personal-Financial-Manager-Mobile-App------------
        //https://dribbble.com/shots/14295333-Online-banking-finance-app-concept
        //https://dribbble.com/shots/7407699--Sign-Up-Sign-In-Modal-Windows
        // Carrega dados do usuário, caso haja
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_root, TransactionFragment(), ViewConstants.TAGS.YEAR_FRAG)
                .commit()
        }

    }



    override fun onBackPressed() {
        val transactionFragment: TransactionFragment? =
            supportFragmentManager.findFragmentByTag(ViewConstants.TAGS.MONTH_FRAG) as TransactionFragment?
        if (transactionFragment != null && transactionFragment.isVisible) {
            if (transactionFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }


}