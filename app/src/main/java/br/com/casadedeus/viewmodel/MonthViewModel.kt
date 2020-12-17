package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.R
import br.com.casadedeus.beans.MonthModel
import br.com.casadedeus.service.repository.MonthRepository
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener

class MonthViewModel(application: Application) : AndroidViewModel(application) {

    private val mContext = application.applicationContext
    private lateinit var mRepository: MonthRepository
    private val mListMonthName = mContext.resources.getStringArray(R.array.month_names)


    private var mMonthList = MutableLiveData<List<MonthModel>>()
    var monthlist: LiveData<List<MonthModel>> = mMonthList

    private var mMonthSave = MutableLiveData<ValidationListener>()
    var monthsave: LiveData<ValidationListener> = mMonthSave

    private var mMonthDelete = MutableLiveData<ValidationListener>()
    var monthdelete: LiveData<ValidationListener> = mMonthDelete

    fun save(strMonth: Int?) {
        //transforma o int em extenso //SimpleDateFormat não funcionou
        val month = strMonth?.let { MonthModel(monthTitle = mListMonthName[it - 1]) }
        mRepository.save(month!!, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mMonthSave.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mMonthSave.value = ValidationListener(message)
            }

        })
    }


    fun load(yearKey: String) {
        mRepository = MonthRepository.newInstance(yearKey)
        //mMonthList.value = mRepository.getMonths()
        mRepository.getMonths(object : OnCallbackListener<List<MonthModel>> {
            override fun onSuccess(result: List<MonthModel>) {
                mMonthList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
            }

        })
    }

    fun delete(monthModel: MonthModel?) {
        monthModel.let {
            if (it != null) {
                mRepository.delete(it)
            }
        }
        mMonthDelete.value = ValidationListener()

    }

}