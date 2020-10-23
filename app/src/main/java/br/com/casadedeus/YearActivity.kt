package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.adapter.MonthAdapter
import br.com.casadedeus.model.MonthModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class YearActivity : AppCompatActivity() {
    lateinit var fabMonth: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)
        //Design
        //https://dribbble.com/shots/14359607-Personal-Financial-Manager-Mobile-App------------
        //https://dribbble.com/shots/14295333-Online-banking-finance-app-concept
        val fabMonth = findViewById<FloatingActionButton>(R.id.fab_month)
        val rvMonth = findViewById<RecyclerView>(R.id.rv_month)
        val model = MonthModel()
        val adapter = MonthAdapter(model.getMonths(),this)
        rvMonth.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        rvMonth.layoutManager = linearLayoutManager
        rvMonth.setHasFixedSize(true)

    }

    fun fabMonthOnClick(view: View){
        Toast.makeText(this,"OnClick Add Month",Toast.LENGTH_SHORT).show()
    }

}