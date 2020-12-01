package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.Month
import br.com.casadedeus.model.MonthModel
import br.com.casadedeus.model.repository.MonthRepository
import br.com.casadedeus.view.listener.OnCallbackListener
import br.com.casadedeus.view.listener.ValidationListener
import java.lang.Exception

class MonthViewModel(application: Application) : AndroidViewModel(application) {

    private val mContext = application.applicationContext
    private var mModel = MonthModel()
    private val mRepository = MonthRepository()

    private var mMonthList = MutableLiveData<List<Month>>()
    var monthlist: LiveData<List<Month>> = mMonthList

    private var mMonthSave = MutableLiveData<ValidationListener>()
    var monthsave: LiveData<ValidationListener> = mMonthSave

    private var mMonthDelete = MutableLiveData<ValidationListener>()
    var monthdelete: LiveData<ValidationListener> = mMonthDelete

    fun save(month: Int?) {
        try {
            month?.let {
                mModel.save(it, mContext, object : OnCallbackListener<Boolean> {
                    override fun onSuccess(result: Boolean) {
                        mMonthSave.value = ValidationListener()

                    }

                    override fun onFailure(message: String) {
                        mMonthSave.value = ValidationListener(message)
                    }

                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mMonthSave.value = ValidationListener(e.message.toString())
        }

    }

    fun delete(month: Month?) {
        try {
            month.let { mModel.delete(it) }
            mMonthDelete.value = ValidationListener()
        } catch (e: Exception) {
            e.printStackTrace()
            mMonthDelete.value = ValidationListener(e.message.toString())
        }
    }

    fun load(path: String) {
        //mMonthList.value = mRepository.getMonths()
        mRepository.getMonths(path, object : OnCallbackListener<List<Month>> {
            override fun onSuccess(result: List<Month>) {
                mMonthList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
            }

        })
    }

}