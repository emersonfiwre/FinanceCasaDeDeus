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
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.TransactionAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*
import java.util.*


class TransactionFragment : Fragment(), View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TransactionViewModel
    private val mAdapter: TransactionAdapter = TransactionAdapter()
    private lateinit var mViewRoot: View

    private lateinit var mBottomDialog: BottomSheetDialog
    private lateinit var mDialogInflater: View
    private var mTransactionKey: String = ""

    /*does not work without inserting dependencies in the gradle
    dependencies{
        implementation 'androidx.core:core-ktx:1.3.2'
        implementation 'androidx.fragment:fragment-ktx:1.2.5'
    }
    compileOptions
    {

        sourceCompatibility = 1.8
        targetCompatibility = 1.8

        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    private val myViewModel: ExpenditureViewModel by viewModels {
        ExpenditureViewModelFactory(this.requireActivity().application, "some string value")
    }
     */


    /*Design
    * radio button https://dribbble.com/shots/9890260-Card-Theme-Switch-Light-Theme
    * btnSave top rigth https://stackoverflow.com/questions/58651661/how-to-set-max-height-in-bottomsheetdialogfragment
    * ?android:textColorHint*/
    @SuppressLint("ClickableViewAccessibility")
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
        setupRecycler()
        setupBottomDialog()

        // Cria observadores
        observe()

        setListeners()



        mViewRoot.edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
            when (i) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    Toast.makeText(activity, "Modulo em contrução", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> { // Note the block
                    println("default case")
                    false
                }
            }
        }
        mViewRoot.edtSearch.setOnTouchListener { view, motionEvent ->
            view.edtSearch.isFocusableInTouchMode = true
            val icSearch = ContextCompat.getDrawable(
                activity!!,
                R.drawable.ic_search
            )
            /*Tint drawable
            DrawableCompat.setTint(
            icSearch as Drawable,
            ContextCompat.getColor(activity, R.color.hintColor)
            )*/
            view.edtSearch.setCompoundDrawablesWithIntrinsicBounds(
                icSearch, null, null, null
            )
            view.edtSearch.hint = getString(R.string.hint_search)
            /*Open Keyboard
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED, 0
            )*/

            false
        }
        /*val etInput = mViewRoot.findViewById<CurrencyEditText>(R.id.edit_valor)
        error, is needed be in the bottomDialog view
        etInput.setCurrency("R$");
        etInput.setDelimiter(false)
        etInput.setSpacing(false)
        etInput.setDecimals(true)
        //Make sure that Decimals is set as false if a custom Separator is used
        etInput.setSeparator(".")*/



        hide(mViewRoot.edtSearch!!, context)
        return mViewRoot
    }

    //Carregar a lista com todos
    private fun loadTransactions() {
        rv_transaction.visibility = View.GONE
        pg_await_load.visibility = View.VISIBLE
        mViewModel.load()
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun setupBottomDialog() {
        mBottomDialog = BottomSheetDialog(activity!!)
        mDialogInflater = layoutInflater.inflate(R.layout.dialog_single_input, null)
        mBottomDialog.setContentView(mDialogInflater)
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
        mViewRoot.back_month.setOnClickListener(this)
        mViewRoot.month.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.transactionlist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
            pg_await_load.visibility = View.GONE
            rv_transaction.visibility = View.VISIBLE
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
        val id = v?.id
        when (id) {
            R.id.add_lancamento -> {
                hide(mViewRoot.edtSearch, activity)
                /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
                mBehavior.setPeekHeight(600)*/
                mTransactionKey = ""
                mDialogInflater.radio_entrada.isChecked = false
                mDialogInflater.edit_descricao.setText("")
                mDialogInflater.spinner_categoria.setSelection(0)
                mDialogInflater.edit_razao_social.setText("")
                mDialogInflater.edit_nota_fiscal.setText("")
                mDialogInflater.edit_valor.setText("")
                onClickSave()
                //get spinner selected
            }
            R.id.back_month -> {
                activity?.onBackPressed()
            }
            R.id.month -> {
                val picker = MonthPickerDialog()
                picker.listener = this
                picker.show(activity!!.supportFragmentManager, "TAG")
                /*val mLocale = Locale("pt", "BR")
                RackMonthPicker(activity)
                    .setLocale(mLocale)
                    .setColorTheme(R.color.colorAccent)
                    .setPositiveButton { month, startDate, endDate, year, monthLabel -> }
                    .setNegativeButton { }.show()*/
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Toast.makeText(context, "$p2 / $p1", Toast.LENGTH_SHORT).show()
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

    private fun hide(edit: EditText, context: Context?) {
        edit.clearFocus()
        edit.isFocusable = false

        edit.setText("")
        val icSearch = context?.let {
            ContextCompat.getDrawable(
                it,
                R.drawable.ic_search
            )
        }
        edit.setCompoundDrawablesWithIntrinsicBounds(
            null, null, icSearch, null
        )
        edit.hint = ""

    }

    fun onBackPressed(): Boolean =
        /*if (view?.edtSearch?.isFocusable == true) {
            hide(view?.edtSearch!!, context as Context)
         s   return false
        }
        return true*/
        when (view?.edtSearch?.isFocusable) {
            true -> {
                hide(view?.edtSearch!!, context)
                false
            }
            else -> true
        }


}