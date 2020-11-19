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
}