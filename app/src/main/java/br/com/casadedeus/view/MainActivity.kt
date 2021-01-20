package br.com.casadedeus.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import br.com.casadedeus.R
import kotlinx.android.synthetic.main.activity_main.*


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
        /*
        Antiga chamada do fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_root, TransactionFragment(), ViewConstants.TAGS.TRANSACTION_FRAG)
                .commit()
        }*/

        // Navegação
        bottom_navigation.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //return super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_add_planning, menu)
        return true

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        print("call activity")
        Toast.makeText(this, "Clickme", Toast.LENGTH_SHORT).show()
        return when (item.itemId) {
            R.id.nav_add_planning -> {
                Toast.makeText(this, "Clickme", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                Toast.makeText(this, "clickNo", Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
        }
    }

}