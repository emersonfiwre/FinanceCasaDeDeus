package br.com.casadedeus.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.viewmodel.GoalViewModel
import kotlinx.android.synthetic.main.activity_transaction_form.*
import kotlinx.android.synthetic.main.dialog_goal_form.view.*
import kotlinx.android.synthetic.main.dialog_month_picker.*
import kotlinx.android.synthetic.main.dialog_month_picker.view.*
import kotlinx.android.synthetic.main.fragment_add_transaction.view.*
import java.text.SimpleDateFormat
import java.util.*


class GoalDialog private constructor() : DialogFragment(),
    View.OnClickListener {
    private lateinit var mDialogRoot: View
    private lateinit var mViewModel: GoalViewModel
    private var auxGoal: GoalModel? = null

    private val mDateFormat =
        SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))// dia por extenso

    companion object {
        fun newInstance(goal: GoalModel? = null): GoalDialog {
            if (goal != null) {
                val args = Bundle()
                args.putSerializable(ViewConstants.KEYS.GOAL, goal)
                val fragment = GoalDialog()
                fragment.arguments = args
                return fragment
            }
            return GoalDialog()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater = requireActivity().layoutInflater
        mDialogRoot = inflater.inflate(R.layout.dialog_goal_form, null)
        mViewModel = ViewModelProvider(this).get(GoalViewModel::class.java)

        //Setar os clicks das views
        setListeners()

        loadGoal()

        // Criar observadores
        observer()

        builder.setView(mDialogRoot).setPositiveButton(getString(R.string.save),
            DialogInterface.OnClickListener { dialog, id ->
                save(auxGoal)
            }).setNegativeButton((getString(R.string.cancel)),
            DialogInterface.OnClickListener { dialog, id -> this@GoalDialog.dialog!!.dismiss() })
        return builder.create()
    }

    private fun loadGoal() {
        if (arguments != null) {
            val goal =
                requireArguments().getSerializable(ViewConstants.KEYS.GOAL) as GoalModel?
            if (goal != null) {
                auxGoal = goal
                mDialogRoot.edit_description_goal.setText(goal.description)
                mDialogRoot.edit_value_goal.setText(Utils.doubleToRealNotCurrency(goal.amount))
                mDialogRoot.edit_duedate.setText(mDateFormat.format(goal.finishday))
                mDialogRoot.txt_title_goal.text = getString(R.string.update_goal)
            }
        }
    }


    private fun datePicker() {
        val calendar: Calendar = Calendar.getInstance()
        val thisAYear = calendar.get(Calendar.YEAR)
        val thisAMonth = calendar.get(Calendar.MONTH)
        val thisADay = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { view2, year, month, day ->
                val mDate = Utils.stringToDate("$day-${month + 1}-$year")
                mDialogRoot.edit_duedate_goal.setText(mDateFormat.format(mDate))
            },
            thisAYear,
            thisAMonth,
            thisADay
        ).show()
    }


    private fun setListeners() {
        mDialogRoot.edit_duedate_goal.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.edit_duedate_goal -> {
                datePicker()
            }
        }
    }

    private fun observer() {
        mViewModel.validation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.success()) {
                if (auxGoal == null) {
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
                dismiss()
                //activity?.supportFragmentManager?.popBackStackImmediate()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save(goal: GoalModel? = null) {
        if (goal != null) {
            goal.finishday = Utils.todayToDate(mDialogRoot.edit_duedate_goal.text.toString())
            goal.description = mDialogRoot.edit_description_goal.text.toString()
            goal.amount = Utils.realToDouble(mDialogRoot.edit_value_goal.text.toString())
            mViewModel.save(goal)
        } else {
            mViewModel.save(
                GoalModel(
                    finishday = Utils.todayToDate(mDialogRoot.edit_duedate_goal.text.toString()),
                    description = mDialogRoot.edit_description_goal.text.toString(),
                    amount = Utils.realToDouble(mDialogRoot.edit_value_goal.text.toString())
                )
            )
        }
    }

    private fun clearForm() {
        mDialogRoot.edit_duedate_goal.setText("")
        mDialogRoot.edit_description_goal.setText("")
        mDialogRoot.edit_value_goal.setText(R.string.zeroed_value)
    }
}