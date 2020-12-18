package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.casadedeus.beans.ExpenditureModel
import br.com.casadedeus.service.repository.ExpenditureRepository
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import java.lang.Exception

class ExpenditureViewModelFactory(
    private val application: Application,
    private val monthKey: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenditureViewModel(application, monthKey) as T
    }
}

class ExpenditureViewModel(application: Application, val monthKey: String) :
    AndroidViewModel(application) {

    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mRepository: ExpenditureRepository = ExpenditureRepository(mContext, monthKey)

    private var mExpenditureList = MutableLiveData<List<ExpenditureModel>>()
    val expenditurelist: LiveData<List<ExpenditureModel>> = mExpenditureList

    /*private var mGuest = MutableLiveData<ExpenditureModel>()
    val guest: LiveData<ExpenditureModel> = mGuest*/

    private var mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation


    fun save(expenditureModel: ExpenditureModel) {
        if (expenditureModel.day.isNotEmpty()) {
            mValidation.value = ValidationListener("O dia está vazio")
            return
        }
        if (expenditureModel.desc.isNotEmpty()) {
            mValidation.value = ValidationListener("A descrição está vazio")
            return
        }
        if (expenditureModel.amount != 0.0) {
            mValidation.value = ValidationListener("O valor está vazio")
            return
        }
        mRepository.save(expenditureModel, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun update(expenditureModel: ExpenditureModel) {
        //TODO
    }

    fun delete(expenditureModel: ExpenditureModel) {
        mRepository.delete(expenditureModel)
    }

    fun get(expenditureModel: ExpenditureModel) {
        mRepository.getExpenditure(expenditureModel)
    }

    fun load(path: String) {
        mRepository.getExpenditures(path, object : OnCallbackListener<List<ExpenditureModel>> {
            override fun onSuccess(result: List<ExpenditureModel>) {
                mExpenditureList.value = result
                mValidation.value = ValidationListener(monthKey)
            }

            override fun onFailure(message: String) {
                val s = message
            }
        })
    }

}