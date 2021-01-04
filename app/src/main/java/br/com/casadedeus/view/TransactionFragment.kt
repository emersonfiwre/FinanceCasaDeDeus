package br.com.casadedeus.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnAdapterListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.TransactionAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*


class TransactionFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: TransactionViewModel
    private val mAdapter: TransactionAdapter = TransactionAdapter()
    private lateinit var mViewRoot: View

    private lateinit var mBottomDialog: BottomSheetDialog
    private lateinit var mDialogInflater: View

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

        //Carregar a lista com todos
        mViewModel.load()

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

    override fun onResume() {
        super.onResume()
        mViewModel.load()
    }

    private fun setupBottomDialog() {
        mBottomDialog = BottomSheetDialog(activity!!)
        mDialogInflater = layoutInflater.inflate(R.layout.dialog_single_input,null)
        mBottomDialog.setContentView(mDialogInflater)
    }


    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_expenditure.layoutManager = linearLayoutManager
        mAdapter.attachListener(object : OnAdapterListener.OnItemClickListener<TransactionModel> {
            override fun onItemClick(item: TransactionModel) {
                val listCategories = context?.resources?.getStringArray(R.array.categories)
                val index: Int = getIndex(listCategories, item.category)

                mDialogInflater.radio_entrada.isChecked = item.isEntry
                mDialogInflater.edit_descricao.setText(item.description)
                mDialogInflater.spinner_categoria.setSelection(index)
                mDialogInflater.edit_razao_social.setText(item.companyName)
                mDialogInflater.edit_nota_fiscal.setText(item.notaFiscal)
                mDialogInflater.edit_valor.setText(Utils.doubleToReal(item.amount))
                onClickSave()
            }
        })
        mViewRoot.rv_expenditure.adapter = mAdapter
        mViewRoot.rv_expenditure.setHasFixedSize(true)
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
    }

    private fun observe() {
        mViewModel.transactionlist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
        })
        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(activity,"Transação adicionada com sucesso!",Toast.LENGTH_SHORT).show()
                mBottomDialog.dismiss()
            }else{
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        val id = v?.id

        if (id == R.id.add_lancamento) {
            hide(mViewRoot.edtSearch, activity)
            /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
            mBehavior.setPeekHeight(600)*/
            onClickSave()
            //get spinner selected
        } else if (id == R.id.back_month) {
            activity?.onBackPressed()
        }
    }
    private fun onClickSave(){
        mBottomDialog.show()
        mDialogInflater.add_expenditure.setOnClickListener {
            mViewModel.save(
                TransactionModel(
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
            return false
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