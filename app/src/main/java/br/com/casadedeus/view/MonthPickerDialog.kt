package br.com.casadedeus.view

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import br.com.casadedeus.R
import kotlinx.android.synthetic.main.dialog_month_picker.*
import kotlinx.android.synthetic.main.dialog_month_picker.view.*
import java.util.*


class MonthPickerDialog(val currentMonth: Int, val currentYear: Int) : DialogFragment(),
    View.OnClickListener {
    var listener: OnDateSetListener? = null
    private lateinit var mDialogRoot: View
    private lateinit var mTextMonths: List<TextView>
    private var mMonthSelection: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        mDialogRoot = inflater.inflate(R.layout.dialog_month_picker, null)

        //Recuperar todas as TextViews dos Months
        getViewMonths()

        //Pegar as datas atuais e setar nos textViews
        setupDateDialog()

        //Setar os clicks das views
        setListeners()

        builder.setView(mDialogRoot).setPositiveButton(getString(R.string.select),
            DialogInterface.OnClickListener { dialog, id ->
                listener?.onDateSet(
                    null,
                    mDialogRoot.txt_year.text.toString().toInt(),
                    mMonthSelection,
                    0
                )

            }).setNegativeButton((getString(R.string.cancel)),
            DialogInterface.OnClickListener { dialog, id -> this@MonthPickerDialog.dialog!!.dismiss() })
        return builder.create()
    }

    private fun getViewMonths() {
        mTextMonths = listOf(
            mDialogRoot.txt_month1,
            mDialogRoot.txt_month2,
            mDialogRoot.txt_month3,
            mDialogRoot.txt_month4,
            mDialogRoot.txt_month5,
            mDialogRoot.txt_month6,
            mDialogRoot.txt_month7,
            mDialogRoot.txt_month8,
            mDialogRoot.txt_month9,
            mDialogRoot.txt_month10,
            mDialogRoot.txt_month11,
            mDialogRoot.txt_month12
        )

    }

    private fun setupDateDialog() {
        /*val cal: Calendar = Calendar.getInstance()
        val year = cal[Calendar.YEAR]// quando abrir a dialog
        val month = cal[Calendar.MONTH]// quando abrir a dialog*/

        mMonthSelection = currentMonth
        mDialogRoot.txt_year.text = currentYear.toString()
        mTextMonths[currentMonth].setTextColor(resources.getColor(R.color.colorAccent))
    }


    private fun setListeners() {
        mDialogRoot.txt_next_year.setOnClickListener(this)
        mDialogRoot.txt_previous_year.setOnClickListener(this)
        //Months click
        for (i in 0 until mTextMonths.count()) {
            mTextMonths[i].setOnClickListener(this)
        }
    }


    override fun onClick(v: View?) {
        //val lengthYear = listOf(1900..3500)
        val currentYear = mDialogRoot.txt_year.text.toString().toInt()
        when (v?.id) {
            R.id.txt_next_year -> {
                mDialogRoot.txt_year.text = (currentYear + 1).toString()
            }
            R.id.txt_previous_year -> {
                mDialogRoot.txt_year.text = (currentYear - 1).toString()
            }
            else -> {
                setSelectionMonth(v?.id)
            }
        }
    }


    private fun setSelectionMonth(id: Int?) {
        for (i in 0 until mTextMonths.count()) {
            if (id == mTextMonths[i].id) {
                mTextMonths[i].setTextColor(resources.getColor(R.color.colorAccent))
                mMonthSelection = i + 1
            } else {
                mTextMonths[i].setTextColor(resources.getColor(R.color.textColor))
            }
        }
        // mTextMonths[index].setTextColor(resources.getColor(R.color.colorAccent))
    }

}