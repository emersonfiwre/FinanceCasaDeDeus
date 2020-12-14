package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.YearModel
import br.com.casadedeus.service.repository.YearRepository
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener

class YearViewModel : ViewModel() {
    private val mRepository = YearRepository()

    private var mYearList = MutableLiveData<List<YearModel>>()
    val yearlist: LiveData<List<YearModel>> = mYearList

    private var mYearSave = MutableLiveData<ValidationListener>()
    val yearsave: LiveData<ValidationListener> = mYearSave

    private var mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private var mYearDelete = MutableLiveData<ValidationListener>()
    val yeardelete: LiveData<ValidationListener> = mYearDelete

    fun save(strYear: String?) {
        val year = strYear?.let { YearModel(yearTitle = it) }
        mRepository.save(year!!, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mYearSave.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mYearSave.value = ValidationListener(message)
            }
        })
    }

    fun delete(yearModel: YearModel?) {
        mRepository.delete(yearModel!!)
        mYearDelete.value = ValidationListener()

    }

    fun load() {
        mRepository.getYears(object : OnCallbackListener<List<YearModel>> {
            override fun onSuccess(result: List<YearModel>) {
                mYearList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
                mValidation.value = ValidationListener(s)
            }
        })
    }


}