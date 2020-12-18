package br.com.casadedeus.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.ExpenditureModel
import br.com.casadedeus.beans.MonthModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.view.adapter.ExpenditureAdapter
import br.com.casadedeus.viewmodel.ExpenditureViewModel
import br.com.casadedeus.viewmodel.ExpenditureViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_expenditure.view.*


class ExpenditureFragment private constructor() : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: ExpenditureViewModel
    private val mAdapter: ExpenditureAdapter = ExpenditureAdapter()
    private lateinit var mViewRoot: View

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


    companion object {
        fun newInstance(monthModel: MonthModel): ExpenditureFragment {
            val args = Bundle()
            args.putSerializable(ViewConstants.KEYS.EXTRAS_MONTH, monthModel);
            val fragment = ExpenditureFragment()
            fragment.arguments = args
            return fragment
        }
    }

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
        mViewRoot = inflater.inflate(R.layout.fragment_expenditure, container, false)
        //mViewModel = ViewModelProvider(this).get(ExpenditureViewModel::class.java)


        //****************** NÃO É NECESSÁRIO FINDVIEWBYID
        val addLancamento = mViewRoot.findViewById<Button>(R.id.add_lancamento)
        val revenueMonth = mViewRoot.findViewById<TextView>(R.id.revenue_month)
        val profitMonth = mViewRoot.findViewById<TextView>(R.id.profit_month)
        val expenditureMonth = mViewRoot.findViewById<TextView>(R.id.expenditure_month)
        val rvExpenditure = mViewRoot.findViewById<RecyclerView>(R.id.rv_expenditure)

        val mMonth = arguments?.getSerializable(ViewConstants.KEYS.EXTRAS_MONTH) as MonthModel
        mViewRoot.month.text = mMonth.monthTitle

        mViewModel = ViewModelProvider(
            this,
            ExpenditureViewModelFactory(this.requireActivity().application, mMonth.key)
        ).get(ExpenditureViewModel::class.java)


        //************
        setupRecycler()

        // Cria observadores
        observe()

        setListeners()

        //Carregar a lista com todos
        mViewModel.load(mMonth.key)

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


        hide(mViewRoot.edtSearch!!, context)
        return mViewRoot
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(activity)
        mViewRoot.rv_expenditure.layoutManager = linearLayoutManager
        mViewRoot.rv_expenditure.adapter = mAdapter
        mViewRoot.rv_expenditure.setHasFixedSize(true)
    }

    private fun setListeners() {
        mViewRoot.add_lancamento.setOnClickListener(this)
        mViewRoot.back_month.setOnClickListener(this)
    }

    private fun observe() {
        mViewModel.expenditurelist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
        })
        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (!it.success()) {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        val id = v?.id

        if (id == R.id.add_lancamento) {
            hide(mViewRoot.edtSearch, activity)
            val dialog = BottomSheetDialog(activity!!)
            val bottomSheet = layoutInflater.inflate(R.layout.dialog_single_input, null)
            dialog.setContentView(bottomSheet)
            /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
            mBehavior.setPeekHeight(600)*/
            dialog.show()
            bottomSheet.add_expenditure.setOnClickListener {

                mViewModel.save(
                    ExpenditureModel(
                        isEntry = bottomSheet.radio_entrada.isChecked,
                        desc = bottomSheet.edit_descricao.text.toString(),
                        category = bottomSheet.spinner_categoria.selectedItem.toString(),
                        companyName = bottomSheet.edit_razao_social.text.toString(),
                        notaFiscal = bottomSheet.edit_nota_fiscal.text.toString(),
                        amount = bottomSheet.edit_valor.text.toString().toDouble()
                    )
                )
            }
            //get spinner selected
        } else if (id == R.id.back_month) {
            activity?.onBackPressed()
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