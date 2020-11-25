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
import br.com.casadedeus.view.adapter.ExpenditureAdapter
import br.com.casadedeus.viewmodel.ExpenditureViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_month.view.*


class MonthFragment : Fragment() {

    private lateinit var mViewModel: ExpenditureViewModel
    private val mAdapter: ExpenditureAdapter = ExpenditureAdapter()

    companion object {
        fun newInstance(month: String): MonthFragment {
            val args = Bundle()
            args.putString("month", month);
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
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        val activity: Context = activity as Context
        val addLancamento = view.findViewById<Button>(R.id.add_lancamento)
        //****************** NÃO É NECESSÁRIO FINDVIEWBYID
        val revenueMonth = view.findViewById<TextView>(R.id.revenue_month)
        val profitMonth = view.findViewById<TextView>(R.id.profit_month)
        val expenditureMonth = view.findViewById<TextView>(R.id.expenditure_month)
        //************
        // Cria observadores
        observe()

        val rvExpenditure = view.findViewById<RecyclerView>(R.id.rv_expenditure)
        rvExpenditure.adapter = mAdapter
        val linearLayoutManager = LinearLayoutManager(activity)
        rvExpenditure.layoutManager = linearLayoutManager

        //Carregar a lista com todos
        mViewModel.load()


        view.back_month.setOnClickListener { getActivity()?.onBackPressed() }
        val month = arguments?.getString("month") as String
        view.month.text = month

        view.edtSearch.setOnEditorActionListener { textView, i, keyEvent ->
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
        view.edtSearch.setOnTouchListener { view, motionEvent ->
            view.edtSearch.isFocusableInTouchMode = true
            val icSearch = ContextCompat.getDrawable(
                activity,
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


        view.add_lancamento.setOnClickListener {
            hide(view.edtSearch, activity)
            val dialog = BottomSheetDialog(activity)
            val bottomSheet = layoutInflater.inflate(R.layout.dialog_single_input, null)
            dialog.setContentView(bottomSheet)
            /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
            mBehavior.setPeekHeight(600)*/
            dialog.show()
            bottomSheet.addExpenditure.setOnClickListener {
                Toast.makeText(activity, "Módulo em construção", Toast.LENGTH_SHORT).show()
            }
            //get spinner selected
        }
        hide(view.edtSearch!!, context)
        return view
    }

    private fun observe() {
        mViewModel.expenditurelist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
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
        when (view?.edtSearch?.isFocusable) {
            true -> {
                hide(view?.edtSearch!!, context)
                false
            }
            else -> true
        }


}