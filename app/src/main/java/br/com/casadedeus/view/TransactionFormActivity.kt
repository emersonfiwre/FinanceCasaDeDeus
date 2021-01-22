package br.com.casadedeus.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.CategoryModel
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.CategoryConstansts
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.CategoryAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_transaction_form.*
import kotlinx.android.synthetic.main.bottom_dialog_categories.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionFormActivity : AppCompatActivity(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {
    private lateinit var mViewModel: TransactionViewModel
    private var mTransactionKey: String = ""
    val mCategoryAdapter = CategoryAdapter()

    private val mDateFormat =
        SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_form)
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
        if (radio_entrada.isChecked) {
            mCategoryAdapter.notifyChanged(CategoryConstansts.getCategoriesProfit(this))
        } else {
            mCategoryAdapter.notifyChanged(CategoryConstansts.getCategoriesExpenditure(this))
        }

    }


    //Carregar a lista com todos
    // tem que fazer um xml selector para no status enable trocar a cor
    private fun loadTransaction() {
        if (intent.extras != null) {
            val mTransaction =
                intent.extras!!.getSerializable(ViewConstants.KEYS.TRANSACTION) as TransactionModel?
            if (mTransaction != null) {
                mTransactionKey = mTransaction.key!!
                radio_entrada.isChecked = mTransaction.isEntry
                edit_descricao.setText(mTransaction.description)
                //mDialogInflater.spinner_categoria.setSelection(index)
                edit_category.setText(mTransaction.category)
                edit_razao_social.setText(mTransaction.companyName)
                edit_nota_fiscal.setText(mTransaction.notaFiscal)
                edit_valor.setText(Utils.doubleToRealNotCurrency(mTransaction.amount))
                edit_duedate.setText(mDateFormat.format(mTransaction.day))

                whatTypeTransaction(mTransaction.isEntry)
            } else {
                setupCurrentDate()
            }
        }
    }

    private fun setupCurrentDate() {
        edit_duedate.setText(Utils.getToday())
    }

    private fun setListeners() {
        edit_duedate.setOnClickListener(this)
        edit_category.setOnClickListener(this)
        fab_save.setOnClickListener(this)
        img_back_transactions.setOnClickListener(this)
        radio_entrada.setOnCheckedChangeListener(this)
        radio_saida.setOnCheckedChangeListener(this)
    }

    private fun observer() {
        mViewModel.validation.observe(this, androidx.lifecycle.Observer {
            if (it.success()) {
                if (mTransactionKey == "") {
                    Toast.makeText(
                        this,
                        getString(R.string.success_save_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                    clearForm()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.success_update_transaction),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                onBackPressed()
                //activity?.supportFragmentManager?.popBackStackImmediate()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_SHORT).show()
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
                onBackPressed()
                //activity?.supportFragmentManager?.popBackStackImmediate()
            }
        }
    }


    private fun datePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val thisAYear = calendar.get(Calendar.YEAR)
        val thisAMonth = calendar.get(Calendar.MONTH)
        val thisADay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            this,
            { view2, year, month, day ->
                val mDate = Utils.stringToDate("$day-${month + 1}-$year")
                edit_duedate.setText(mDateFormat.format(mDate))
            },
            thisAYear,
            thisAMonth,
            thisADay
        ).show()
    }

    private fun showCategories() {
        val bottomDialogCategory = BottomSheetDialog(this)
        val inflater = layoutInflater.inflate(R.layout.bottom_dialog_categories, null)
        bottomDialogCategory.setContentView(inflater)
        val linearLayoutManager = LinearLayoutManager(this)
        inflater.rv_categories.layoutManager = linearLayoutManager
        mCategoryAdapter.attachListener(object : OnItemClickListener<CategoryModel> {
            override fun onItemClick(item: CategoryModel) {
                edit_category.setText(item.title)

                edit_category.setCompoundDrawablesWithIntrinsicBounds(
                    item.image,
                    null,
                    null,
                    null
                );
                //Toast.makeText(this, item, Toast.LENGTH_SHORT).show()
                bottomDialogCategory.dismiss()
            }

            override fun onLongClick(id: String) {
                TODO("Not yet implemented")
            }
        })
        inflater.rv_categories.adapter = mCategoryAdapter
        bottomDialogCategory.show()
    }

    private fun onClickSave() {
        mViewModel.save(
            TransactionModel(
                key = mTransactionKey,
                day = Utils.todayToDate(edit_duedate.text.toString()),
                isEntry = radio_entrada.isChecked,
                description = edit_descricao.text.toString(),
                category = edit_category.text.toString(),
                companyName = edit_razao_social.text.toString(),
                notaFiscal = edit_nota_fiscal.text.toString(),
                amount = Utils.realToDouble(edit_valor.text.toString())
            )
        )
    }

    private fun clearForm() {
        mTransactionKey = ""
        //radio_entrada.isChecked = false
        edit_duedate.setText(Utils.getToday())
        edit_descricao.setText("")
        edit_category.setText("")
        edit_razao_social.setText("")
        edit_nota_fiscal.setText("")
        edit_valor.setText(R.string.zeroed_value)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.radio_entrada -> {
                llm_header.isEnabled = true
                whatTypeTransaction(false)
            }
            R.id.radio_saida -> {
                llm_header.isEnabled = false
                whatTypeTransaction(true)
            }

        }
    }

    private fun whatTypeTransaction(isEntry: Boolean) {
        if (isEntry) {
            mCategoryAdapter.notifyChanged(CategoryConstansts.getCategoriesProfit(this))
            txt_title.text = getString(R.string.update_profit)
            txt_whatvalue.text = getString(R.string.value_profit)
            //llm_header.background.setTint(resources.getColor(R.color.light_blue))
            llm_header.isEnabled = false
        } else {
            // mListCategories = this?.resources?.getStringArray(R.array.categories)?.toList() ?: arrayListOf()
            mCategoryAdapter.notifyChanged(CategoryConstansts.getCategoriesExpenditure(this))
            txt_title.text = getString(R.string.update_expenditure)
            txt_whatvalue.text = getString(R.string.value_expenditure)
        }
    }

}