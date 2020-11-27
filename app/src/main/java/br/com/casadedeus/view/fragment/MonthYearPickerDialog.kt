package br.com.casadedeus.view.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import br.com.casadedeus.R
import br.com.casadedeus.model.constants.ViewConstants
import kotlinx.android.synthetic.main.month_year_picker_dialog.view.*
import java.util.*


class MonthYearPickerDialog private constructor() : DialogFragment() {
    private val maxYear = 2100
    var listener: OnDateSetListener? = null

    companion object {
        fun newInstance(isMonth: Boolean?): MonthYearPickerDialog {
            val args = Bundle()
            if (isMonth != null)
                args.putBoolean(ViewConstants.KEYS.WHATPICKER, isMonth)
            val fragment = MonthYearPickerDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity, R.style.AlertDialogStyle)
        val inflater = activity!!.layoutInflater
        val cal: Calendar = Calendar.getInstance()
        val dialog: View = inflater.inflate(R.layout.month_year_picker_dialog, null)
        val monthPicker = dialog.picker_month
        val yearPicker = dialog.picker_year

        val isMonth = arguments?.getBoolean(ViewConstants.KEYS.WHATPICKER) as Boolean
        if (isMonth) {
            yearPicker.visibility = View.GONE
            monthPicker.visibility = View.VISIBLE
            dialog.title_picker.text = getString(R.string.months)
            //println("isMonth: ${title_picker.text}")
        }


        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal[Calendar.MONTH] + 1

        val year = cal[Calendar.YEAR]
        yearPicker.minValue = 1900
        yearPicker.maxValue = 3500
        yearPicker.value = year

        builder.setView(dialog).setPositiveButton("Adicionar",
            DialogInterface.OnClickListener { dialog, id ->
                listener!!.onDateSet(
                    null,
                    yearPicker.value,
                    monthPicker.value,
                    0
                )
            }).setNegativeButton(("Cancelar"),
            DialogInterface.OnClickListener { dialog, id -> this@MonthYearPickerDialog.dialog!!.cancel() })
        return builder.create()
    }

}