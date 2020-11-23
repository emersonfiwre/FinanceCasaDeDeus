package br.com.casadedeus.model.repository

import androidx.lifecycle.ViewModel
import java.util.*

class MonthRepository: ViewModel() {
    fun insert(month: Calendar):Boolean{
        return true
    }

    fun delete(month: String):Boolean{
        return true
    }

    fun getMonths(): List<String> {
        return listOf(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
    }
}