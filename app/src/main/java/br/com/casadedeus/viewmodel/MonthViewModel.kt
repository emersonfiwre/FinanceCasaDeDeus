package br.com.casadedeus.viewmodel

import androidx.lifecycle.ViewModel
import br.com.casadedeus.model.MonthModel
import java.util.*

class MonthViewModel: ViewModel() {
    private var monthModel = MonthModel()

    fun insert(month:Calendar){
        monthModel.insert(month)
    }

    fun delete(month: String){
        monthModel.delete(month)
    }
}