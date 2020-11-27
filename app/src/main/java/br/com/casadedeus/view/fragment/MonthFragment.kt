package br.com.casadedeus.view.fragment

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
import br.com.casadedeus.model.constants.ViewConstants
import br.com.casadedeus.view.adapter.ExpenditureAdapter
import br.com.casadedeus.viewmodel.ExpenditureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_month.view.*
import kotlinx.android.synthetic.main.fragment_year.*


class MonthFragment private constructor() : Fragment(), View.OnClickListener {

    private lateinit var mViewModel: ExpenditureViewModel
    private val mAdapter: ExpenditureAdapter = ExpenditureAdapter()
    private lateinit var mViewRoot: View

    companion object {
        fun newInstance(month: String): MonthFragment {
            val args = Bundle()
            args.putString(ViewConstants.KEYS.TITLEMONTH, month);
            val fragment = MonthFragment()
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
    ): View? {
        mViewModel = ViewModelProvider(this).get(ExpenditureViewModel::class.java)
        mViewRoot = inflater.inflate(R.layout.fragment_month, container, false)

        //****************** NÃO É NECESSÁRIO FINDVIEWBYID
        val addLancamento = mViewRoot.findViewById<Button>(R.id.add_lancamento)
        val revenueMonth = mViewRoot.findViewById<TextView>(R.id.revenue_month)
        val profitMonth = mViewRoot.findViewById<TextView>(R.id.profit_month)
        val expenditureMonth = mViewRoot.findViewById<TextView>(R.id.expenditure_month)
        val rvExpenditure = mViewRoot.findViewById<RecyclerView>(R.id.rv_expenditure)
        //view.back_month.setOnClickListener { getActivity()?.onBackPressed() }
        //************

        setupRecycler()


        val month = arguments?.getString(ViewConstants.KEYS.TITLEMONTH) as String
        mViewRoot.month.text = month

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
        hide(mViewRoot.edtSearch, context)
        // Cria observadores
        observe()

        setListeners()

        //Carregar a lista com todos
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
        mViewModel.expendituresave.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(context, "Adicionado com sucesso", Toast.LENGTH_SHORT).show()
                mViewModel.load()
            } else {
                Toast.makeText(context, "Houve algum erro ao inserir os dados", Toast.LENGTH_SHORT)
                    .show()
            }
        })
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
        when (mViewRoot.edtSearch.isFocusable) {
            true -> {
                hide(mViewRoot.edtSearch, context)
                false
            }
            else -> true
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
            bottomSheet.save_expenditure.setOnClickListener {
                Toast.makeText(activity, "Módulo em construção", Toast.LENGTH_SHORT).show()

                val isEntry = bottomSheet.radio_entrada.isChecked
                val descricao = bottomSheet.edit_descricao.text.toString()
                val categoria = bottomSheet.spinner_categoria.selectedItem.toString()
                val razaoSocial = bottomSheet.edit_razao_social.text.toString()
                val notaFiscal = bottomSheet.edit_nota_fiscal.text.toString()
                val valor = bottomSheet.edit_valor.text.toString()
                val dvalor: Double = 0.0
                if (valor.isNotEmpty()) {
                    valor.toDouble()
                }
                mViewModel.save(
                    isEntry,
                    descricao,
                    categoria,
                    razaoSocial,
                    notaFiscal,
                    dvalor
                )
            }
            //get spinner selected
        } else if (id == R.id.back_month) {
            activity?.onBackPressed()
        }

    }


}