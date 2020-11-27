package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.model.YearModel
import br.com.casadedeus.model.repository.YearRepository
import java.util.*

class YearViewModel : ViewModel() {
    private var mModel = YearModel()
    private val mRepository = YearRepository()

    private var mYearList = MutableLiveData<List<String>>()
    val yearlist: LiveData<List<String>> = mYearList

    private var mYearSave = MutableLiveData<Boolean>()
    val yearsave: LiveData<Boolean> = mYearSave

    fun save(year: String?) {
        mYearSave.value = mModel.save(year)
    }

    fun delete(year: String?) {
        mModel.delete(year)
    }

    fun load() {
        mYearList.value = mRepository.getYears()
    }
}