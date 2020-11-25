package br.com.casadedeus.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.casadedeus.R
import br.com.casadedeus.view.fragment.MonthFragment
import br.com.casadedeus.view.fragment.YearFragment

class MainActivity : AppCompatActivity() {
    private val yearFragment = YearFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Design
        //https://dribbble.com/shots/14359607-Personal-Financial-Manager-Mobile-App------------
        //https://dribbble.com/shots/14295333-Online-banking-finance-app-concept
        //https://dribbble.com/shots/7407699--Sign-Up-Sign-In-Modal-Windows
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_root, yearFragment, "yearFragment")
                .commit()
        }

    }

    fun fabMonthOnClick(view: View) {
        yearFragment.onClick(view)
    }

    override fun onBackPressed() {
        val monthFragment: MonthFragment? =
            supportFragmentManager.findFragmentByTag("monthFragment") as MonthFragment?
        if (monthFragment != null && monthFragment.isVisible) {
            if (monthFragment.onBackPressed()) {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }


}