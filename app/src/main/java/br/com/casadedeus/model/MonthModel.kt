package br.com.casadedeus.model

import android.content.Context
import br.com.casadedeus.R
import br.com.casadedeus.model.repository.MonthRepository
import java.text.SimpleDateFormat
import java.util.*

class MonthModel {
    private var monthRepository = MonthRepository()

    fun save(month: Int?, context: Context): Boolean {
        return if (month != null) {
            //transforma o int em extenso //SimpleDateFormat não funcionou
            val monthName = context.resources.getStringArray(R.array.month_names)[month - 1]
            monthRepository.save(monthName)
        } else {
            throw Exception("O mês está nulo")
        }
    }

    fun delete(month: String) {
        if (month.isNotEmpty()) {
            monthRepository.delete(month)
        } else {
            throw Exception("O mês está nulo")
        }
    }

}