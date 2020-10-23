package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.casadedeus.`interface`.ItemClickListener
import br.com.casadedeus.adapter.YearAdapter
import br.com.casadedeus.model.YearModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemClickListener {
    lateinit var rvYear: RecyclerView
    lateinit var fabYear: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Design
        //https://dribbble.com/tags/android_ui
        rvYear = findViewById(R.id.rv_year)
        fabYear = findViewById(R.id.fab_year)
        setupRv()
    }
    private fun setupRv(){
        val model = YearModel()
        var adapter = YearAdapter(model.getYears(),this)
        adapter.setOnItemClickListener(this)
        rvYear.adapter = adapter
        rvYear.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvYear.layoutManager = layoutManager
    }
    override fun onItemClickListener(view: View, position: Int) {
        println("testando o click: $position")
    }
    fun fabYearOnClick(view:View){
        Toast.makeText(this,"OnClick Add Year", Toast.LENGTH_SHORT).show()
    }
}