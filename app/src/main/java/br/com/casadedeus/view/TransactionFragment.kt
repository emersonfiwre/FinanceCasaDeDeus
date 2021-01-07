package br.com.casadedeus.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.CategoryAdapter
import br.com.casadedeus.view.adapter.TransactionAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.grpc.okhttp.internal.Util
import kotlinx.android.synthetic.main.bottom_dialog_categories.view.*
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*


class TransactionFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TransactionViewModel
    private val mAdapter: TransactionAdapter = TransactionAdapter()
    private lateinit var mViewRoot: View

    private lateinit var mBottomDialog: BottomSheetDialog
    private lateinit var mDialogInflater: View
    private var mTransactionKey: String = ""
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
        setupBottomDialog()

        // Cria observadores
        observer()

        setListeners()

        return mViewRoot
    }

    private fun setupCurrentDate() {
        mViewRoot.txt_current_date.text = Utils.getCurrentDate()
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

    private fun setupBottomDialog() {
        mBottomDialog = BottomSheetDialog(activity!!)
        mDialogInflater = layoutInflater.inflate(R.layout.dialog_single_input, null)
        mBottomDialog.setContentView(mDialogInflater)
        mDialogInflater.txt_categoria.setOnClickListener {
            val bottomDialogCategory = BottomSheetDialog(activity!!)
            val inflater = layoutInflater.inflate(R.layout.bottom_dialog_categories, null)
            bottomDialogCategory.setContentView(inflater)
            val linearLayoutManager = LinearLayoutManager(activity)
            inflater.rv_categories.layoutManager = linearLayoutManager
            val adapter = CategoryAdapter()
            val listCategories: List<String> =
                context?.resources?.getStringArray(R.array.categories)!!
                    .toList()
            adapter.notifyChanged(listCategories)
            adapter.attachListener(object : OnItemClickListener<String> {
                override fun onItemClick(item: String) {
                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                }

                override fun onDeleteClick(id: String) {
                    TODO("Not yet implemented")
                }
            })
            inflater.rv_categories.adapter = adapter
            bottomDialogCategory.show()
        }
    }


    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_transaction.layoutManager = linearLayoutManager
        mAdapter.attachListener(object : OnItemClickListener<TransactionModel> {
            override fun onItemClick(item: TransactionModel) {
                val listCategories = context?.resources?.getStringArray(R.array.categories)
                val index: Int = getIndex(listCategories, item.category)

                mTransactionKey = item.key!!
                mDialogInflater.radio_entrada.isChecked = item.isEntry
                mDialogInflater.edit_descricao.setText(item.description)
                mDialogInflater.spinner_categoria.setSelection(index)
                mDialogInflater.edit_razao_social.setText(item.companyName)
                mDialogInflater.edit_nota_fiscal.setText(item.notaFiscal)
                mDialogInflater.edit_valor.setText(Utils.doubleToRealNotCurrency(item.amount))
                onClickSave()
            }

            override fun onDeleteClick(id: String) {
                mViewModel.delete(id)
            }
        })
        mViewRoot.rv_transaction.adapter = mAdapter
        mViewRoot.rv_transaction.setHasFixedSize(true)
    }

    private fun getIndex(listCategories: Array<String>?, category: String): Int {
        var index = 0
        if (listCategories != null) {
            for (i in 0 until listCategories.count()) {
                if (listCategories[i] == category) {
                    index = i
                    break
                }
            }
        }
        return index
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
                if (mTransactionKey == "") {
                    Toast.makeText(
                        context,
                        getString(R.string.success_save_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.success_update_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                loadTransactions()
                mBottomDialog.dismiss()
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
                clearForm()
                onClickSave()
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

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Utils.stringToDate("$p2-$p1").let {
            mViewRoot.txt_current_date.text = mDateFormat.format(it)
        }
        loadTransactions()
        //Toast.makeText(context, "$p2 / $p1", Toast.LENGTH_SHORT).show()
    }

    private fun onClickSave() {
        mBottomDialog.show()
        mDialogInflater.add_expenditure.setOnClickListener {
            mViewModel.save(
                TransactionModel(
                    key = mTransactionKey,
                    isEntry = mDialogInflater.radio_entrada.isChecked,
                    description = mDialogInflater.edit_descricao.text.toString(),
                    category = mDialogInflater.spinner_categoria.selectedItem.toString(),
                    companyName = mDialogInflater.edit_razao_social.text.toString(),
                    notaFiscal = mDialogInflater.edit_nota_fiscal.text.toString(),
                    amount = Utils.realToDouble(mDialogInflater.edit_valor.text.toString())
                )
            )
        }
    }

    private fun clearForm() {
        mTransactionKey = ""
        mDialogInflater.radio_entrada.isChecked = false
        mDialogInflater.edit_descricao.setText("")
        mDialogInflater.spinner_categoria.setSelection(0)
        mDialogInflater.edit_razao_social.setText("")
        mDialogInflater.edit_nota_fiscal.setText("")
        mDialogInflater.edit_valor.setText("")
    }

}