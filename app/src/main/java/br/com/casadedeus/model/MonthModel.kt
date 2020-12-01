package br.com.casadedeus.model

import android.content.Context
import br.com.casadedeus.R
import br.com.casadedeus.beans.Month
import br.com.casadedeus.model.repository.MonthRepository
import br.com.casadedeus.view.listener.OnCallbackListener
import java.text.SimpleDateFormat
import java.util.*

class MonthModel {
    private var monthRepository = MonthRepository()

    fun save(month: Int?, context: Context, listener: OnCallbackListener<Boolean>) {
        if (month != null) {
            //transforma o int em extenso //SimpleDateFormat não funcionou
            val monthName = context.resources.getStringArray(R.array.month_names)[month - 1]
            monthRepository.save(Month(montTitle = monthName), listener)
        } else {
            throw Exception("O mês está nulo")
        }
    }

    fun delete(month: Month?) {
        if (month != null) {
            monthRepository.delete(month)
        } else {
            throw Exception("O mês está nulo")
        }
    }

}