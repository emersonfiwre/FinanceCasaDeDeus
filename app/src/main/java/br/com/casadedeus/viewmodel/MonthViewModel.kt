package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.casadedeus.model.MonthModel
import br.com.casadedeus.model.repository.MonthRepository
import br.com.casadedeus.view.listener.OnCallbackListener
import java.util.*

class MonthViewModel(application: Application) : AndroidViewModel(application){

    private val mContext = application.applicationContext
    private var mModel = MonthModel()
    private val mRepository = MonthRepository()

    private var mMonthList = MutableLiveData<List<String>>()
    var monthlist: LiveData<List<String>> = mMonthList

    private var mMonthSave = MutableLiveData<Boolean>()
    var monthsave: LiveData<Boolean> = mMonthSave

    fun save(month: Int) {
        mMonthSave.value = mModel.save(month, mContext)
    }

    fun delete(month: String) {
        mModel.delete(month)
    }

    fun load(path: String) {
        //mMonthList.value = mRepository.getMonths()
        mRepository.getMonthss(path, object:   OnCallbackListener<List<String>>{
            override fun onSuccess(result: List<String>) {
                mMonthList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
            }

        })
    }

}