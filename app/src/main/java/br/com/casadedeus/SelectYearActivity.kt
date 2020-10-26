package br.com.casadedeus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.`interface`.OnClickListener
import br.com.casadedeus.adapter.YearAdapter
import br.com.casadedeus.model.YearModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SelectYearActivity : AppCompatActivity(), OnClickListener.OnItemClickListener {
    lateinit var rvYear: RecyclerView
    lateinit var fabYear: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_year)
        //Design
        //https://dribbble.com/tags/android_ui
        rvYear = findViewById(R.id.rv_year)
        fabYear = findViewById(R.id.fab_year)
        setupRv()
    }
    private fun setupRv(){
        val model = YearModel()
        var adapter = YearAdapter(model.getYears(),this)
        adapter.onItemClick = this
        rvYear.adapter = adapter
        rvYear.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        rvYear.layoutManager = layoutManager
    }
    override fun onItemClick(view: View, position: Int) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun fabYearOnClick(view:View){
        //Toast.makeText(this,"OnClick Add Year", Toast.LENGTH_SHORT).show()

    }

}