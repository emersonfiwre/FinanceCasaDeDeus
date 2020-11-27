package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.model.ExpenditureModel
import br.com.casadedeus.model.repository.ExpenditureRepository
import java.lang.Exception

class ExpenditureViewModel(application: Application) : AndroidViewModel(application) {
    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mModel: ExpenditureModel = ExpenditureModel()
    private val mRepository: ExpenditureRepository = ExpenditureRepository()

    private var mExpenditureList = MutableLiveData<List<Expenditure>>()
    val expenditurelist: LiveData<List<Expenditure>> = mExpenditureList

    private var mExpenditureSave = MutableLiveData<Boolean>()
    val expendituresave: LiveData<Boolean> = mExpenditureSave

    private val expenditureModel = ExpenditureModel()

    fun save(
        isEntry: Boolean,
        desc: String,
        category: String,
        razaoSocial: String,
        notaFiscal: String,
        valor: Double
    ) {
        val expenditure = Expenditure(
            dia = "sab, 21 set 2020",
            isEntry = false,
            desc = desc,
            category = category,
            razaoSocial = razaoSocial,
            notaFiscal = notaFiscal,
            valor = valor
        )
        try {
            mExpenditureSave.value = expenditureModel.save(expenditure)
        } catch (e: Exception) {
            e.printStackTrace()
            mExpenditureSave.value = false
        }

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