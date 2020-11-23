package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.view.adapter.YearAdapter
import br.com.casadedeus.view.fragment.MonthYearPickerDialog
import br.com.casadedeus.viewmodel.YearViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class SelectYearActivity : AppCompatActivity(), OnAdapterListener.OnItemClickListener,
    DatePickerDialog.OnDateSetListener {
    lateinit var rvYear: RecyclerView
    lateinit var fabYear: FloatingActionButton
    private var mAdapter = YearAdapter()
    private lateinit var mViewModel: YearViewModel

    val mCalendar = Calendar.getInstance();
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR);
    val month = c.get(Calendar.MONTH);
    val day = c.get(Calendar.DAY_OF_MONTH);


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_year)

        mViewModel = ViewModelProvider(this).get(YearViewModel::class.java)
        //Design
        //https://dribbble.com/tags/android_ui
        observe()
        rvYear = findViewById(R.id.rv_year)
        fabYear = findViewById(R.id.fab_year)
        setupRv()
        mViewModel.load()
    }

    private fun observe() {
        mViewModel.yearlist.observe(this, androidx.lifecycle.Observer {
            mAdapter.notifyChanged(it)
        })
    }

    private fun setupRv() {
        //adapter.onItemClick = this
        rvYear.adapter = mAdapter
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
        val monthYearPickerDialog: MonthYearPickerDialog = MonthYearPickerDialog.newInstance(false)
        monthYearPickerDialog.listener = this
        monthYearPickerDialog.show(supportFragmentManager, "yearPickerDialog")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        println("selecionou uma data $p1 $p2 $p3")
        /*p1 year
        p2 month
        p3 day*/

    }

}