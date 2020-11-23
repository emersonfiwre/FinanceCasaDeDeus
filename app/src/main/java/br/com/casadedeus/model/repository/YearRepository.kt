package br.com.casadedeus.model.repository

import androidx.lifecycle.ViewModel
import java.util.*

class YearRepository: ViewModel() {

    fun insert(year: Calendar):Boolean{
        return true
    }

    fun delete(year: String):Boolean{
        return true
    }

    fun getYears(): List<String> {
        return listOf(
            "2020",
            "2019",
            "2018",
            "2017",
            "2016",
            "2015")
    }
}