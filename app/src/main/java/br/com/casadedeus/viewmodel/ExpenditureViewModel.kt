package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.service.ExpenditureModel
import br.com.casadedeus.service.repository.ExpenditureRepository
import br.com.casadedeus.service.listener.OnCallbackListener
import java.lang.Exception

class ExpenditureViewModel(application: Application) : AndroidViewModel(application) {
    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mRepository: ExpenditureRepository = ExpenditureRepository()

    private var mExpenditureList = MutableLiveData<List<Expenditure>>()
    val expenditurelist: LiveData<List<Expenditure>> = mExpenditureList

    /*private var mGuest = MutableLiveData<ExpenditureModel>()
    val guest: LiveData<ExpenditureModel> = mGuest*/

    private val expenditureModel = ExpenditureModel()

    fun insert(expenditure: Expenditure) {
        if (expenditure.day.isNotEmpty()) {
            throw Exception("O dia está vazio")
            return
        }
        if (expenditure.desc.isNotEmpty()) {
            throw Exception("O desc está vazio")
            return
        }
        if (expenditure.amount != 0.0) {
            throw Exception("O valor está vazio")
            return
        }
        mRepository.insert(expenditure)
    }

    fun update(expenditure: Expenditure) {
        expenditureModel.update(expenditure)
    }

    fun delete(expenditure: Expenditure) {
        mRepository.delete(expenditure)
    }

    fun get(expenditure: Expenditure) {
        mRepository.getExpenditure(expenditure)
    }

    fun load(path: String) {
        mExpenditureList.value = mRepository.getExpenditures()
        mRepository.getExpendituress(path, object : OnCallbackListener<List<Expenditure>> {
            override fun onSuccess(result: List<Expenditure>) {
                mExpenditureList.value = result
            }

            override fun onFailure(message: String) {
                val s = message
            }

        })
    }

}