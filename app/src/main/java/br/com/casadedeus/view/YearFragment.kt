package br.com.casadedeus.view

import android.app.DatePickerDialog
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
import br.com.casadedeus.beans.Month
import br.com.casadedeus.model.constants.ViewConstants
import br.com.casadedeus.view.adapter.MonthAdapter
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.viewmodel.MonthViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_year.view.*


class YearFragment private constructor() : Fragment(), View.OnClickListener,
    OnAdapterListener.OnItemClickListener<Month>,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: MonthViewModel
    private val mAdapter: MonthAdapter = MonthAdapter()
    private lateinit var mViewRoot: View
    private lateinit var mPath: String

    companion object {
        fun newInstance(year: String): YearFragment {
            val args = Bundle()
            args.putString(ViewConstants.KEYS.TITLEYEAR, year);
            val fragment = YearFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
        mViewRoot = inflater.inflate(R.layout.fragment_year, container, false)
        //****************
        val fabMonth = mViewRoot.findViewById<FloatingActionButton>(R.id.fab_month)
        val rvMonth = mViewRoot.findViewById<RecyclerView>(R.id.rv_month)
        val revenueYear = mViewRoot.findViewById<TextView>(R.id.revenue_year)
        //****************

        //mAdapter.attachListener(context as OnAdapterListener.OnItemClickListener)
        //view.back_year.setOnClickListener { getActivity()?.onBackPressed() }
        val mYear = arguments?.getString(ViewConstants.KEYS.TITLEYEAR) as String
        mViewRoot.txt_year.text = mYear

        mPath = "users/WqVSBEFTfLTRSPLNV52k/years/$mYear"

        setupRecycler()

        observe()

        setListeners()

        mViewModel.load(mPath)

        return mViewRoot
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_month?.layoutManager = linearLayoutManager
        mAdapter.attachListener(this)
        mViewRoot.rv_month?.adapter = mAdapter
        mViewRoot.rv_month?.setHasFixedSize(true)
    }

    private fun setListeners() {
        mViewRoot.back_year.setOnClickListener(this)
        mViewRoot.fab_month.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.monthlist.observe(viewLifecycleOwner, Observer {
            println("callback observer $it")
            mAdapter.notifyChanged(it)
        })
        mViewModel.monthsave.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load(mPath)
            } else {
                Toast.makeText(context, "Houve algum erro ao inserir o ano", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onClick(v: View?) {
        val id = v?.id
        if (id == R.id.fab_month) {
            val monthYearPickerDialog: MonthYearPickerDialog =
                MonthYearPickerDialog.newInstance(true)
            monthYearPickerDialog.listener = this
            monthYearPickerDialog.show(
                activity!!.supportFragmentManager,
                ViewConstants.TAGS.MONTHPICKER
            )
        } else if (id == R.id.back_year) {
            activity?.onBackPressed()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //Toast.makeText(context, "Modulo em construção", Toast.LENGTH_SHORT).show()
        mViewModel.save(month)
    }


    override fun onItemClick(item: Month) {
        val monthFragment = MonthFragment.newInstance("$mPath/months/${item.montTitle}")
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_root, monthFragment, ViewConstants.TAGS.MONTH)
            .addToBackStack(null)
            .commit()
    }


}
