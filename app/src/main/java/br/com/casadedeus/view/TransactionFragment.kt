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
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.TransactionAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mViewRoot: View
    private lateinit var mViewModel: TransactionViewModel
    private val mAdapter: TransactionAdapter = TransactionAdapter()

    private val mDateFormat = SimpleDateFormat("MMM, yyyy", Locale("pt", "BR"))// mes por extenso

    /*Design
    * radio button https://dribbble.com/shots/9890260-Card-Theme-Switch-Light-Theme
    * btnSave top rigth https://stackoverflow.com/questions/58651661/how-to-set-max-height-in-bottomsheetdialogfragment
    * ?android:textColorHint*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewRoot = inflater.inflate(R.layout.fragment_transaction, container, false)
        mViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        /*mViewModel = ViewModelProvider(
            this,
            ExpenditureViewModelFactory(this.requireActivity().application, mMonth.key)
        ).get(ExpenditureViewModel::class.java)*/


        //************
        setupCurrentDate()
        setupRecycler()

        // Cria observadores
        observer()

        setListeners()

        return mViewRoot
    }

    private fun setupCurrentDate() {
        mViewRoot.txt_current_date.text = Utils.getCurrentMonth()
    }

    //Carregar a lista com todos
    private fun loadTransactions() {
        rv_transaction.visibility = View.GONE
        pg_await_load.visibility = View.VISIBLE
        mViewModel.load(txt_current_date.text.toString())
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_transaction.layoutManager = linearLayoutManager
        mAdapter.attachListener(object : OnItemClickListener<TransactionModel> {
            override fun onItemClick(item: TransactionModel) {
                showAddTransaction(item)
            }

            override fun onLongClick(id: String) {
                mViewModel.delete(id)
            }
        })
        mViewRoot.rv_transaction.adapter = mAdapter
        mViewRoot.rv_transaction.setHasFixedSize(true)
    }

    private fun setListeners() {
        mViewRoot.add_lancamento.setOnClickListener(this)
        mViewRoot.txt_current_date.setOnClickListener(this)
    }

    private fun observer() {
        mViewModel.transactionlist.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                mViewRoot.pg_await_load.visibility = View.GONE
                mViewRoot.rv_transaction.visibility = View.GONE
                mViewRoot.txt_empty_transactions.visibility = View.VISIBLE
            } else {
                mAdapter.notifyChanged(it)
                mViewRoot.txt_empty_transactions.visibility = View.GONE
                mViewRoot.pg_await_load.visibility = View.GONE
                mViewRoot.rv_transaction.visibility = View.VISIBLE
            }

        })
        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.success()) {

            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.delete.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(
                    context,
                    getString(R.string.success_delete_transaction),
                    Toast.LENGTH_SHORT
                ).show()
                loadTransactions()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.balance.observe(viewLifecycleOwner, Observer {
            mViewRoot.balance_month.text = it
        })
        mViewModel.expenditure.observe(viewLifecycleOwner, Observer {
            mViewRoot.expenditure_month.text = it
        })
        mViewModel.profit.observe(viewLifecycleOwner, Observer {
            mViewRoot.profit_month.text = it
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_lancamento -> {
                /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
                mBehavior.setPeekHeight(600)*/
                /*clearForm()
                onClickSave()*/
                showAddTransaction()
                //get spinner selected
            }
            R.id.txt_current_date -> {
                val picker = MonthPickerDialog(
                    mViewModel.getYearMonthFromString(txt_current_date.text.toString(), true),
                    mViewModel.getYearMonthFromString(txt_current_date.text.toString())
                )
                picker.listener = this
                picker.show(activity!!.supportFragmentManager, ViewConstants.TAGS.MONTH_PICKER)
            }
        }
    }

    private fun showAddTransaction(transaction: TransactionModel? = null) {
        /*val monthFragment = AddTransactionFragment.newInstance(transaction)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.setCustomAnimations(
                R.anim.slide_in_up,
                R.anim.slide_out_up,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            ?.replace(R.id.container_root, monthFragment, ViewConstants.TAGS.ADD_TRANSACTION)
            ?.addToBackStack(null)
            ?.commit()*/
        val intent = Intent(context, TransactionFormActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(ViewConstants.KEYS.TRANSACTION, transaction)
        intent.putExtras(bundle)
        startActivity(intent)
        activity?.overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Utils.stringToMonth("$p2-$p1").let {
            mViewRoot.txt_current_date.text = mDateFormat.format(it)
        }
        loadTransactions()
        //Toast.makeText(context, "$p2 / $p1", Toast.LENGTH_SHORT).show()
    }


}