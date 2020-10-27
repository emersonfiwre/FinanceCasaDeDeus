package br.com.casadedeus

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.`interface`.OnClickListener
import br.com.casadedeus.adapter.YearAdapter
import br.com.casadedeus.fragment.MonthYearPickerDialog
import br.com.casadedeus.model.YearModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.reflect.Field
import java.util.*

class SelectYearActivity : AppCompatActivity(), OnClickListener.OnItemClickListener,
    DatePickerDialog.OnDateSetListener {
    lateinit var rvYear: RecyclerView
    lateinit var fabYear: FloatingActionButton

    val mCalendar = Calendar.getInstance();
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR);
    val month = c.get(Calendar.MONTH);
    val day = c.get(Calendar.DAY_OF_MONTH);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_year)
        //Design
        //https://dribbble.com/tags/android_ui
        rvYear = findViewById(R.id.rv_year)
        fabYear = findViewById(R.id.fab_year)
        setupRv()
    }

    private fun setupRv() {
        val model = YearModel()
        var adapter = YearAdapter(model.getYears(), this)
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

    fun fabYearOnClick(view: View) {
        //Toast.makeText(this,"OnClick Add Year", Toast.LENGTH_SHORT).show()
        /*val datePickerDialog  = DatePickerDialog(
            this, this, year, month, day).show()*/
        val monthYearPickerDialog:MonthYearPickerDialog = MonthYearPickerDialog.newInstance(false)
        monthYearPickerDialog.listener = this
        monthYearPickerDialog.show(supportFragmentManager,"yearPickerDialog")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        println("selecionou uma data $p1 $p2 $p3")
        /*p1 year
        p2 month
        p3 day*/

    }

}