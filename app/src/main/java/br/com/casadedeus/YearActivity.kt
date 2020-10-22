package br.com.casadedeus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.casadedeus.`interface`.ItemClickListener
import br.com.casadedeus.adapter.YearAdapter

class YearActivity : AppCompatActivity(), ItemClickListener {
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
        adapter.setOnItemClickListener(this)
        rvYear.adapter = adapter
        rvYear.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvYear.layoutManager = layoutManager
    }
    override fun onItemClickListener(view: View, position: Int) {
        println("testando o click: $position")
    }
}