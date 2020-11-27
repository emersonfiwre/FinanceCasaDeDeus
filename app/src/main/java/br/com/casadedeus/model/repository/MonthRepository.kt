package br.com.casadedeus.model.repository

import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class MonthRepository : ViewModel() {
    companion object {
        private var mList = mutableListOf(

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

    private fun orderby(list: List<String>): List<String> {
        val local = Locale("pt", "BR")
        return list.sortedBy {
            SimpleDateFormat("MMMM", local)
                .parse(it)
        }
    }

    fun save(month: String): Boolean {
        if (!mList.contains(month)) {
            mList.add(month)
            return true
        }
        return false
    }

    fun delete(month: String): Boolean {
        mList.remove(month)
        return true
    }

    fun getMonths(): List<String> {
        return orderby(mList)
    }
}