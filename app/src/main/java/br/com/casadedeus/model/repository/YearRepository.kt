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
}