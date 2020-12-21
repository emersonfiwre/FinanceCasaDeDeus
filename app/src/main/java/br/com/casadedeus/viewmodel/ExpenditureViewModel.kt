package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.TransactionRepository
import java.util.*

/*class ExpenditureViewModelFactory(
    private val application: Application,
    private val monthKey: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenditureViewModel(application, monthKey) as T
    }
}*/

class ExpenditureViewModel(application: Application) : AndroidViewModel(application) {

    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mRepository: TransactionRepository = TransactionRepository(mContext)

    private var mExpenditureList = MutableLiveData<List<TransactionModel>>()
    val expenditurelist: LiveData<List<TransactionModel>> = mExpenditureList

    /*private var mGuest = MutableLiveData<ExpenditureModel>()
    val guest: LiveData<ExpenditureModel> = mGuest*/

    private var mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation


    fun save(transactionModel: TransactionModel) {
        transactionModel.day = Calendar.getInstance().time
        /*if (expenditureModel.day == null) {

            //mValidation.value = ValidationListener("O dia está vazio")
            //return
        }*/
        if (transactionModel.description.isEmpty()) {
            mValidation.value = ValidationListener("A descrição está vazio")
            return
        }

        mRepository.save(transactionModel, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun update(transactionModel: TransactionModel) {
        //TODO
    }

    fun delete(transactionModel: TransactionModel) {
        mRepository.delete(transactionModel)
    }

    fun get(transactionModel: TransactionModel) {
        mRepository.getExpenditure(transactionModel)
    }

    fun load() {
        mRepository.getExpenditures(object : OnCallbackListener<List<TransactionModel>> {
            override fun onSuccess(result: List<TransactionModel>) {
                mExpenditureList.value = result
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                val s = message
            }
        })
    }

}