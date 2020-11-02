package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.`interface`.OnClickListener
import br.com.casadedeus.adapter.MonthAdapter
import br.com.casadedeus.fragment.MonthFragment
import br.com.casadedeus.fragment.YearFragment
import br.com.casadedeus.model.MonthModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), OnClickListener.OnMonthClickListener {
    private val yearFragment = YearFragment()
    private lateinit var monthFragment: MonthFragment
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
        if (monthFragment.onBackPressed()) {
            if (supportFragmentManager.backStackEntryCount == 0) {
                super.onBackPressed()
                //additional code
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onMonthClick(month: String) {
        //println("escutou o onclick")
        monthFragment = MonthFragment.newInstance(month)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_root, monthFragment, "monthFragment")
            .addToBackStack(null)
            .commit()
    }

}