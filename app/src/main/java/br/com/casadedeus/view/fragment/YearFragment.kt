package br.com.casadedeus.view.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.view.adapter.MonthAdapter
import br.com.casadedeus.viewmodel.MonthViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_year.view.*

class YearFragment : Fragment(), OnAdapterListener.OnClickFragmentListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: MonthViewModel
    private val mAdapter: MonthAdapter = MonthAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_year, container, false)

        val activity = activity as Context
        val fabMonth = view.findViewById<FloatingActionButton>(R.id.fab_month)

        val rvMonth = view.findViewById<RecyclerView>(R.id.rv_month)
        //****************
        val revenueYear = view.findViewById<TextView>(R.id.revenue_year)
        observe()

        //****************
        //adapter.onMonthClickListener = context as OnClickListener.OnMonthClickListener

        rvMonth?.adapter = mAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        rvMonth?.layoutManager = linearLayoutManager
        rvMonth?.setHasFixedSize(true)

        view.back_year.setOnClickListener { getActivity()?.onBackPressed() }

        mViewModel.load()

        return view
    }

    private fun observe() {
        mViewModel.monthlist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
        })
    }

    override fun onClick(view: View) {
        val monthYearPickerDialog: MonthYearPickerDialog = MonthYearPickerDialog.newInstance(true)
        monthYearPickerDialog.listener = this
        monthYearPickerDialog.show(activity!!.supportFragmentManager, "monthPickerDialog")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Toast.makeText(context, "Modulo em construção", Toast.LENGTH_SHORT).show()
    }


}