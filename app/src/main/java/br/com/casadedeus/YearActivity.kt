package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.casadedeus.adapter.YearAdapter

class YearActivity : AppCompatActivity() {
    lateinit var rvYear: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)
        rvYear = findViewById(R.id.rv_year)
        setupRv()
    }
    private fun setupRv(){
        val list = arrayOf("2020","2019","2018")
        var adapter = YearAdapter(list,this)
        rvYear.adapter = adapter
        rvYear.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvYear.layoutManager = layoutManager
    }
}