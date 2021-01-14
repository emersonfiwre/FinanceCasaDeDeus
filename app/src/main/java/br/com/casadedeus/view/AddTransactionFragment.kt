package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.CategoryAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog_categories.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import java.text.NumberFormat.getCurrencyInstance
import java.text.SimpleDateFormat
import java.util.*


class AddTransactionFragment private constructor() : DialogFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    private lateinit var mViewRoot: View
    private lateinit var mViewModel: TransactionViewModel
    private var mTransactionKey: String = ""
    private val mDateFormat =
        SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

    companion object {
        fun newInstance(transaction: TransactionModel? = null): AddTransactionFragment {
            if (transaction != null) {
                val args = Bundle()
                args.putSerializable(ViewConstants.KEYS.TRANSACTION, transaction)
                val fragment = AddTransactionFragment()
                fragment.arguments = args
                return fragment
            }
            return AddTransactionFragment()
        }
    }

    /*Design
    * radio button https://dribbble.com/shots/9890260-Card-Theme-Switch-Light-Theme
    * btnSave top rigth https://stackoverflow.com/questions/58651661/how-to-set-max-height-in-bottomsheetdialogfragment
    * ?android:textColorHint*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewRoot = inflater.inflate(R.layout.fragment_add_transaction, container, false)
        mViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        /*mViewModel = ViewModelProvider(
            this,
            ExpenditureViewModelFactory(this.requireActivity().application, mMonth.key)
        ).get(ExpenditureViewModel::class.java)*/
        loadTransaction()

        // Cria observadores
        observer()

        setListeners()

        //mViewRoot.edit_valor.setCurrency()

        return mViewRoot
    }

    //Carregar a lista com todos
    // tem que fazer um xml selector para no status enable trocar a cor
    private fun loadTransaction() {
        if (arguments != null) {
            val mTransaction =
                arguments!!.getSerializable(ViewConstants.KEYS.TRANSACTION) as TransactionModel?
            if (mTransaction != null) {
                mTransactionKey = mTransaction.key!!
                mViewRoot.radio_entrada.isChecked = mTransaction.isEntry
                mViewRoot.edit_descricao.setText(mTransaction.description)
                //mDialogInflater.spinner_categoria.setSelection(index)
                mViewRoot.edit_category.setText(mTransaction.category)
                mViewRoot.edit_razao_social.setText(mTransaction.companyName)
                mViewRoot.edit_nota_fiscal.setText(mTransaction.notaFiscal)
                mViewRoot.edit_valor.setText(Utils.doubleToRealNotCurrency(mTransaction.amount))
                mViewRoot.edit_duedate.setText(mDateFormat.format(mTransaction.day))

                whatTypeTransaction(mTransaction.isEntry)
            }
        } else {
            setupCurrentDate()
        }
    }

    private fun setupCurrentDate() {
        mViewRoot.edit_duedate.setText(Utils.getToday())
    }

    private fun setListeners() {
        mViewRoot.edit_duedate.setOnClickListener(this)
        mViewRoot.edit_category.setOnClickListener(this)
        mViewRoot.fab_save.setOnClickListener(this)
        mViewRoot.img_back_transactions.setOnClickListener(this)
        mViewRoot.radio_entrada.setOnCheckedChangeListener(this)
        mViewRoot.radio_saida.setOnCheckedChangeListener(this)
    }

    private fun observer() {
        mViewModel.validation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.success()) {
                if (mTransactionKey == "") {
                    Toast.makeText(
                        context,
                        getString(R.string.success_save_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                    clearForm()
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.success_update_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                activity?.supportFragmentManager?.popBackStackImmediate()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit_category -> {
                showCategories()
                //get spinner selected
            }
            R.id.edit_duedate -> {
                datePicker()
            }
            R.id.fab_save -> {
                onClickSave()
            }
            R.id.img_back_transactions -> {
                //activity?.onBackPressed()
                activity?.supportFragmentManager?.popBackStackImmediate()
            }
        }
    }


    private fun datePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val thisAYear = calendar.get(Calendar.YEAR)
        val thisAMonth = calendar.get(Calendar.MONTH)
        val thisADay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context!!,
            { view2, year, month, day ->
                val mDate = Utils.stringToDate("$day-${month + 1}-$year")
                mViewRoot.edit_duedate.setText(mDateFormat.format(mDate))
            },
            thisAYear,
            thisAMonth,
            thisADay
        ).show()
    }

    private fun showCategories() {
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
                mViewRoot.edit_category.setText(item)
                //Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                bottomDialogCategory.dismiss()
            }

            override fun onLongClick(id: String) {
                TODO("Not yet implemented")
            }
        })
        inflater.rv_categories.adapter = adapter
        bottomDialogCategory.show()
    }

    private fun onClickSave() {
        mViewModel.save(
            TransactionModel(
                key = mTransactionKey,
                day = Utils.todayToDate(mViewRoot.edit_duedate.text.toString()),
                isEntry = mViewRoot.radio_entrada.isChecked,
                description = mViewRoot.edit_descricao.text.toString(),
                category = mViewRoot.edit_category.text.toString(),
                companyName = mViewRoot.edit_razao_social.text.toString(),
                notaFiscal = mViewRoot.edit_nota_fiscal.text.toString(),
                amount = Utils.realToDouble(mViewRoot.edit_valor.text.toString())
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

    }


    private fun clearForm() {
        mTransactionKey = ""
        //mViewRoot.radio_entrada.isChecked = false
        mViewRoot.edit_duedate.setText(Utils.getToday())
        mViewRoot.edit_descricao.setText("")
        mViewRoot.edit_category.setText("")
        mViewRoot.edit_razao_social.setText("")
        mViewRoot.edit_nota_fiscal.setText("")
        mViewRoot.edit_valor.setText("")
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.radio_entrada -> {
                mViewRoot.llm_header.isEnabled = true
                whatTypeTransaction(false)
            }
            R.id.radio_saida -> {
                mViewRoot.llm_header.isEnabled = false
                whatTypeTransaction(true)
            }

        }
    }

    private fun whatTypeTransaction(isEntry: Boolean) {
        if (isEntry) {
            mViewRoot.txt_title.text = getString(R.string.update_profit)
            mViewRoot.txt_whatvalue.text = getString(R.string.value_profit)
            //mViewRoot.llm_header.background.setTint(resources.getColor(R.color.light_blue))
            mViewRoot.llm_header.isEnabled = false
        } else {
            mViewRoot.txt_title.text = getString(R.string.update_expenditure)
            mViewRoot.txt_whatvalue.text = getString(R.string.value_expenditure)
        }
    }
}


