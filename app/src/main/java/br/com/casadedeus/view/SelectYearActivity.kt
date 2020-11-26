package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.model.constants.ViewConstants
import br.com.casadedeus.view.adapter.YearAdapter
import br.com.casadedeus.view.fragment.MonthYearPickerDialog
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.viewmodel.YearViewModel
import kotlinx.android.synthetic.main.activity_select_year.*

class SelectYearActivity : AppCompatActivity(), View.OnClickListener,
    OnAdapterListener.OnItemClickListener<String>,
    DatePickerDialog.OnDateSetListener {
    lateinit var rvYear: RecyclerView
    private var mAdapter = YearAdapter()
    private lateinit var mViewModel: YearViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_year)

        mViewModel = ViewModelProvider(this).get(YearViewModel::class.java)
        //Design
        //https://dribbble.com/tags/android_ui
        setListeners()

        observe()

        rvYear = findViewById(R.id.rv_year)
        mAdapter.attachListener(this)
        setupRv()
        mViewModel.load()
    }

    private fun setListeners() {
        fab_year.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v?.id

        if (id == R.id.fab_year) {

            val monthYearPickerDialog: MonthYearPickerDialog =
                MonthYearPickerDialog.newInstance(false)
            monthYearPickerDialog.listener = this
            monthYearPickerDialog.show(supportFragmentManager, ViewConstants.TAGS.YEARPICKER)

        }
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        println("selecionou uma data $year $month $dayOfMonth")

        Toast.makeText(this, "selecionou uma data $year $month $dayOfMonth", Toast.LENGTH_LONG)
            .show()

        /*p1 year
        p2 month
        p3 day*/
    }

    override fun onItemClick(item: String) {
        val intent = Intent(this, MainActivity::class.java)
        val bundle = Bundle()
        bundle.putString(ViewConstants.KEYS.TITLEYEAR, item)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}