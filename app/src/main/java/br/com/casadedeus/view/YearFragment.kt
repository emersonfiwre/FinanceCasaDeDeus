package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.YearModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.OnAdapterListener
import br.com.casadedeus.view.adapter.YearAdapter
import br.com.casadedeus.viewmodel.YearViewModel
import kotlinx.android.synthetic.main.fragment_year.view.*

class YearFragment : Fragment(), View.OnClickListener,
    OnAdapterListener.OnItemClickListener<YearModel>,
    DatePickerDialog.OnDateSetListener {
    private lateinit var mViewModel: YearViewModel
    private var mAdapter = YearAdapter()
    private lateinit var mViewRoot: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewRoot = inflater.inflate(R.layout.fragment_year, container, false)

        mViewModel = ViewModelProvider(this).get(YearViewModel::class.java)
        //Design
        //https://dribbble.com/tags/android_ui
        //val rvYear = findViewById(R.id.rv_year)

        setupRecycler()

        setListeners()

        observe()

        mViewModel.load()

        return mViewRoot
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context)
        mViewRoot.rv_year.layoutManager = layoutManager
        mAdapter.attachListener(this)
        mViewRoot.rv_year.adapter = mAdapter
        mViewRoot.rv_year.setHasFixedSize(true)
    }

    private fun setListeners() {
        mViewRoot.fab_year.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.yearlist.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            mAdapter.notifyChanged(it)
        })

        mViewModel.yearsave.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load()
            } else {
                println(it.failure())
                Toast.makeText(context, it.failure(), Toast.LENGTH_LONG).show()
            }
        })
        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(context, "Validation com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load()
            } else {
                println(it.failure())
                Toast.makeText(context, it.failure(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onClick(v: View?) {
        val id = v?.id

        if (id == R.id.fab_year) {
            val monthYearPickerDialog: MonthYearPickerDialog =
                MonthYearPickerDialog.newInstance(false)
            monthYearPickerDialog.listener = this
            activity?.supportFragmentManager?.let {
                monthYearPickerDialog.show(
                    it,
                    ViewConstants.TAGS.YEAR_PICKER
                )
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        println("selecionou uma data $year $month $dayOfMonth")
        //Toast.makeText(this, "selecionou uma data $year $month $dayOfMonth", Toast.LENGTH_LONG).show()
        mViewModel.save(year.toString())

        /*p1 year
        p2 month
        p3 day*/
    }


    override fun onItemClick(item: YearModel) {
        val monthFragment = MonthFragment.newInstance(item)
        activity!!.supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
            .replace(R.id.container_root, monthFragment, ViewConstants.TAGS.YEAR_FRAG)
            .addToBackStack(null)
            .commit()
    }

}