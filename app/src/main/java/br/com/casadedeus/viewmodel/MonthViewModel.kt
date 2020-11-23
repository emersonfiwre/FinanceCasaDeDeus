package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.model.MonthModel
import br.com.casadedeus.model.repository.MonthRepository
import java.util.*

class MonthViewModel : ViewModel() {
    private var mModel = MonthModel()
    private val mRepository = MonthRepository()

    private var mMonthList = MutableLiveData<List<String>>()
    var monthlist: LiveData<List<String>> = mMonthList

    fun insert(month: Calendar) {
        mModel.insert(month)
    }

    fun delete(month: String) {
        mModel.delete(month)
    }

    fun load() {
        mMonthList.value = mRepository.getMonths()
    }
}