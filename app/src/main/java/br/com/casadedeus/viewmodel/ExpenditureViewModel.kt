package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.model.ExpenditureModel
import br.com.casadedeus.model.repository.ExpenditureRepository

class ExpenditureViewModel(application: Application) : AndroidViewModel(application) {
    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mModel: ExpenditureModel = ExpenditureModel()
    private val mRepository: ExpenditureRepository = ExpenditureRepository()

    private var mExpenditureList = MutableLiveData<List<Expenditure>>()
    val expenditurelist: LiveData<List<Expenditure>> = mExpenditureList

    /*private var mGuest = MutableLiveData<ExpenditureModel>()
    val guest: LiveData<ExpenditureModel> = mGuest*/

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

    fun load() {
        mExpenditureList.value = mRepository.getExpenditures()
    }
}