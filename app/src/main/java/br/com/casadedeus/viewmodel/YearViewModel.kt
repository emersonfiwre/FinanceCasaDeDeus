package br.com.casadedeus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.beans.Year
import br.com.casadedeus.model.YearModel
import br.com.casadedeus.model.repository.YearRepository
import br.com.casadedeus.view.listener.OnCallbackListener
import br.com.casadedeus.view.listener.ValidationListener
import java.lang.Exception
import java.util.*

class YearViewModel : ViewModel() {
    private var mModel = YearModel()
    private val mRepository = YearRepository()

    private var mYearList = MutableLiveData<List<Year>>()
    val yearlist: LiveData<List<Year>> = mYearList

    private var mYearSave = MutableLiveData<ValidationListener>()
    val yearsave: LiveData<ValidationListener> = mYearSave

    private var mYearDelete = MutableLiveData<ValidationListener>()
    val yeardelete: LiveData<ValidationListener> = mYearDelete

    fun save(strYear: String?) {
        try {
            val year = strYear?.let { Year(yearTitle = it) }
            mModel.save(year, object : OnCallbackListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    mYearSave.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    val s = message
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            mYearSave.value = ValidationListener(e.message.toString())
        }

    }

    fun delete(year: Year?) {
        try {
            mModel.delete(year)
            mYearDelete.value = ValidationListener()
        } catch (e: Exception) {
            e.printStackTrace()
            mYearDelete.value = ValidationListener(e.message.toString())
        }

    }

    fun load() {
        mRepository.getYears(object : OnCallbackListener<List<Year>> {
            override fun onSuccess(result: List<Year>) {
                mYearList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
            }
        })
    }


}