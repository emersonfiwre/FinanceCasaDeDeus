package br.com.casadedeus.viewmodel

import androidx.lifecycle.ViewModel
import br.com.casadedeus.model.YearModel
import java.util.*

class YearViewModel: ViewModel() {
    private var yearModel = YearModel()

    fun insert(year:Calendar?){
        yearModel.insert(year)
    }

    fun delete(year: String?){
        yearModel.delete(year)
    }
}