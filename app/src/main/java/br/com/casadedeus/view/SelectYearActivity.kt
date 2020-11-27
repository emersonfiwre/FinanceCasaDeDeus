package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var mAdapter = YearAdapter()
    private lateinit var mViewModel: YearViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_year)

        mViewModel = ViewModelProvider(this).get(YearViewModel::class.java)
        //Design
        //https://dribbble.com/tags/android_ui
        setupRecycler()

        setListeners()

        observe()

        mViewModel.load()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.load()
    }

    private fun setupRecycler() {
        //adapter.onItemClick = this
        val layoutManager = LinearLayoutManager(this)
        rv_year.layoutManager = layoutManager
        mAdapter.attachListener(this)
        rv_year.adapter = mAdapter
        rv_year.setHasFixedSize(true)
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
        mViewModel.yearsave.observe(this, Observer {
            if (it) {
                Toast.makeText(this, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load()
            } else {
                Toast.makeText(this, "Houve algum erro ao inserir o ano", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        println("selecionou uma data $year $month $dayOfMonth")
        //Toast.makeText(this, "selecionou uma data $year $month $dayOfMonth", Toast.LENGTH_LONG).show()
        mViewModel.save(year.toString())

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