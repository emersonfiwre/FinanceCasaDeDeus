package br.com.casadedeus.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnItemClickListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.CategoryAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog_categories.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.edit_descricao
import kotlinx.android.synthetic.main.fragment_add_transaction.view.edit_nota_fiscal
import kotlinx.android.synthetic.main.fragment_add_transaction.view.edit_razao_social
import kotlinx.android.synthetic.main.fragment_add_transaction.view.edit_valor
import kotlinx.android.synthetic.main.fragment_add_transaction.view.radio_entrada
import java.text.SimpleDateFormat
import java.util.*


class AddTransactionFragment : Fragment(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TransactionViewModel
    private lateinit var mViewRoot: View

    private var mTransactionKey: String = ""
    private val mDateFormat =
        SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

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


        //************
        setupCurrentDate()

        // Cria observadores
        observer()

        setListeners()

        return mViewRoot
    }

    private fun setupCurrentDate() {
        mViewRoot.edit_duedate.setText(Utils.getCurrentDate())
    }

    //Carregar a lista com todos
    private fun loadTransaction(item: TransactionModel) {
        mTransactionKey = item.key!!
        mViewRoot.radio_entrada.isChecked = item.isEntry
        mViewRoot.edit_descricao.setText(item.description)
        //mDialogInflater.spinner_categoria.setSelection(index)
        mViewRoot.edit_razao_social.setText(item.companyName)
        mViewRoot.edit_nota_fiscal.setText(item.notaFiscal)
        mViewRoot.edit_valor.setText(Utils.doubleToRealNotCurrency(item.amount))
    }

    private fun setListeners() {
        mViewRoot.edit_duedate.setOnClickListener(this)
        mViewRoot.edit_category.setOnClickListener(this)
    }

    private fun observer() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit_category -> {
                clearForm()
                onClickSave()
                //get spinner selected
            }
            R.id.edit_duedate -> {
                showCategories()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        Utils.stringToDate("$p2-$p1").let {
            mViewRoot.edit_duedate.setText("")//mDateFormat.format(it)
        }
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
                Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteClick(id: String) {
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
                isEntry = mViewRoot.radio_entrada.isChecked,
                description = mViewRoot.edit_descricao.text.toString(),
                category = mViewRoot.edit_category.text.toString(),
                companyName = mViewRoot.edit_razao_social.text.toString(),
                notaFiscal = mViewRoot.edit_nota_fiscal.text.toString(),
                amount = Utils.realToDouble(mViewRoot.edit_valor.text.toString())
            )
        )
    }

    private fun clearForm() {
        mTransactionKey = ""
        mViewRoot.radio_entrada.isChecked = false
        mViewRoot.edit_descricao.setText("")
        mViewRoot.edit_category.setText("")
        mViewRoot.edit_razao_social.setText("")
        mViewRoot.edit_nota_fiscal.setText("")
        mViewRoot.edit_valor.setText("")
    }
}


