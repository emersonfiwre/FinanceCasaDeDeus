package br.com.casadedeus.viewmodel

import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.model.ExpenditureModel

class ExpenditureViewModel : ViewModel() {
    private val expenditureModel = ExpenditureModel()

    fun insert(expenditure: Expenditure) {
        expenditureModel.insert(expenditure)
    }

    fun update(expenditure: Expenditure) {
        expenditureModel.update(expenditure)
    }

    fun delete(expenditure: Expenditure) {
        expenditureModel.delete(expenditure)
    }

    fun get(expenditure: Expenditure) {
        expenditureModel.getExpenditure(expenditure)
    }

    fun getAll() {
        expenditureModel.getAll()
    }
}