package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.adapter.MonthAdapter
import br.com.casadedeus.model.MonthModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Design
        //https://dribbble.com/shots/14359607-Personal-Financial-Manager-Mobile-App------------
        //https://dribbble.com/shots/14295333-Online-banking-finance-app-concept
        val rvMonth = findViewById<RecyclerView>(R.id.rv_month)
        val model = MonthModel()
        val adapter = MonthAdapter(model.getMonths(),this)
        rvMonth.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        rvMonth.layoutManager = linearLayoutManager
        rvMonth.setHasFixedSize(true)

    }

}