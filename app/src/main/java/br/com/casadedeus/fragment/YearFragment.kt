package br.com.casadedeus.fragment

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.`interface`.OnClickListener
import br.com.casadedeus.adapter.MonthAdapter
import br.com.casadedeus.model.MonthModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_year.view.*

class YearFragment : Fragment(), OnClickListener.OnClickFragmentListener,
    DatePickerDialog.OnDateSetListener{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_year, container, false)

        val activity = activity as Context
        val fabMonth = view.findViewById<FloatingActionButton>(R.id.fab_month)

        val rvMonth = view.findViewById<RecyclerView>(R.id.rv_month)
        //****************
        val revenueYear = view.findViewById<TextView>(R.id.revenue_year)
        //****************
        val model = MonthModel()
        val adapter = MonthAdapter(model.getMonths(), activity)

        adapter.onMonthClickListener = context as OnClickListener.OnMonthClickListener

        rvMonth?.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        rvMonth?.layoutManager = linearLayoutManager
        rvMonth?.setHasFixedSize(true)

        view.back_year.setOnClickListener { getActivity()?.onBackPressed() }

        return view
    }

    override fun onClick(view: View) {
        val monthYearPickerDialog:MonthYearPickerDialog = MonthYearPickerDialog.newInstance(true)
        monthYearPickerDialog.listener = this
        monthYearPickerDialog.show(activity!!.supportFragmentManager,"monthPickerDialog")
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Toast.makeText(context,"Modulo em construção",Toast.LENGTH_SHORT).show()
    }


}