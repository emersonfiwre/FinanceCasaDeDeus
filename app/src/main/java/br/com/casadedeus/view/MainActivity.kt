package br.com.casadedeus.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.casadedeus.R
import br.com.casadedeus.beans.Year
import br.com.casadedeus.model.constants.ViewConstants

class MainActivity : AppCompatActivity() {
    private lateinit var mYearFragment: YearFragment
    private lateinit var year: Year
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Design
        //https://dribbble.com/shots/14359607-Personal-Financial-Manager-Mobile-App------------
        //https://dribbble.com/shots/14295333-Online-banking-finance-app-concept
        //https://dribbble.com/shots/7407699--Sign-Up-Sign-In-Modal-Windows
        // Carrega dados do usuário, caso haja
        loadData()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_root, mYearFragment, ViewConstants.TAGS.YEAR_FRAG)
                .commit()
        }
    }

    private fun loadData() {
        val bundle = intent.extras
        if (bundle != null) {
            year = bundle.getSerializable(ViewConstants.KEYS.EXTRAS_YEAR) as Year
            mYearFragment = YearFragment.newInstance(year)
        }
    }


    override fun onBackPressed() {
        val monthFragment: MonthFragment? =
            supportFragmentManager.findFragmentByTag(ViewConstants.TAGS.MONTH_FRAG) as MonthFragment?
        if (monthFragment != null && monthFragment.isVisible) {
            if (monthFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }


}