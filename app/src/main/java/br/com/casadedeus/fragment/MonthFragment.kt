package br.com.casadedeus.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.adapter.ExpenditureAdapter
import br.com.casadedeus.model.ExpenditureModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_single_input.view.*
import kotlinx.android.synthetic.main.fragment_month.*
import kotlinx.android.synthetic.main.fragment_month.view.*

class MonthFragment : Fragment() {

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
    * btnSave top rigth https://stackoverflow.com/questions/58651661/how-to-set-max-height-in-bottomsheetdialogfragment*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        val activity: Context = activity as Context
        val addLancamento = view.findViewById<Button>(R.id.add_lancamento)
        //****************** NÃO É NECESSÁRIO FINDVIEWBYID
        val revenueMonth = view.findViewById<TextView>(R.id.revenue_month)
        val profitMonth = view.findViewById<TextView>(R.id.profit_month)
        val expenditureMonth = view.findViewById<TextView>(R.id.expenditure_month)
        //************
        val rvExpenditure = view.findViewById<RecyclerView>(R.id.rv_expenditure)
        val model = ExpenditureModel()
        val adapter = ExpenditureAdapter(activity, model.getExpenditures())
        rvExpenditure.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(activity)
        rvExpenditure.layoutManager = linearLayoutManager

        view.back_month.setOnClickListener { getActivity()?.onBackPressed() }

        val month = arguments?.getString("month") as String
        view.month.text = month

        view.add_lancamento.setOnClickListener {
            val dialog = BottomSheetDialog(activity)
            val bottomSheet = layoutInflater.inflate(R.layout.dialog_single_input, null)
            dialog.setContentView(bottomSheet)
            /*val mBehavior = BottomSheetBehavior.from(bottomSheet.parent as View);
            mBehavior.setPeekHeight(600)*/
            dialog.show()
            bottomSheet.addExpenditure.setOnClickListener {
                Toast.makeText(activity,"Módulo em construção", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }


}