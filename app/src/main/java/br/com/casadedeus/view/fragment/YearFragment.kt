package br.com.casadedeus.view.fragment

import android.app.DatePickerDialog
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
import br.com.casadedeus.model.constants.ViewConstants
import br.com.casadedeus.view.adapter.MonthAdapter
import br.com.casadedeus.view.listener.OnAdapterListener
import br.com.casadedeus.viewmodel.MonthViewModel
import kotlinx.android.synthetic.main.fragment_year.view.*


class YearFragment private constructor() : Fragment(), View.OnClickListener,
    OnAdapterListener.OnItemClickListener<String>,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: MonthViewModel
    private val mAdapter: MonthAdapter = MonthAdapter()
    private lateinit var mViewRoot: View

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
    ): View? {
        mViewModel = ViewModelProvider(this).get(MonthViewModel::class.java)
        mViewRoot = inflater.inflate(R.layout.fragment_year, container, false)

        setupRecycler()

        val mYear = arguments?.getString(ViewConstants.KEYS.TITLEYEAR) as String
        mViewRoot.txt_year.text = mYear

        observe()

        setListeners()

        mViewModel.load()

        return mViewRoot
    }

    override fun onResume() {
        super.onResume()
        mViewModel.load()
    }

    private fun setupRecycler() {
        //****************
        //mAdapter.attachListener(context as OnAdapterListener.OnItemClickListener)
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_month.layoutManager = linearLayoutManager
        mAdapter.attachListener(this)
        mViewRoot.rv_month.adapter = mAdapter
        mViewRoot.rv_month.setHasFixedSize(true)
    }

    private fun setListeners() {
        mViewRoot.fab_month.setOnClickListener(this)
        mViewRoot.back_year.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.monthlist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
        })
        mViewModel.monthsave.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load()
            } else {
                Toast.makeText(context, "Houve algum erro ao inserir o ano", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        //Toast.makeText(context, "Modulo em construção", Toast.LENGTH_SHORT).show()
        mViewModel.save(month)
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


    override fun onItemClick(item: String) {
        val monthFragment = MonthFragment.newInstance(item)
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.container_root, monthFragment, ViewConstants.TAGS.MONTH)
            .addToBackStack(null)
            .commit()
    }


}
