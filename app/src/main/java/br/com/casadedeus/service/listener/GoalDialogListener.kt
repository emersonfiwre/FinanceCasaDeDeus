package br.com.casadedeus.service.listener

import android.widget.DatePicker
import br.com.casadedeus.beans.GoalModel

interface GoalDialogListener {
    fun onSaveClick(goal: GoalModel)

}