package br.com.casadedeus.model.repository

import androidx.lifecycle.ViewModel

class YearRepository : ViewModel() {
    companion object {
        private var mList = mutableListOf(
            "2019",
            "2018",
            "2017",
            "2016",
            "2015"
        )
    }

    private fun orderby(list: List<String>): List<String> {
        return list.sortedByDescending { it }
    }

    fun save(year: String): Boolean {
        mList.add(year)
        return true
    }

    fun delete(year: String): Boolean {
        mList.remove(year)
        return true
    }

    fun getYears(): List<String> {
        return orderby(mList)
    }
}