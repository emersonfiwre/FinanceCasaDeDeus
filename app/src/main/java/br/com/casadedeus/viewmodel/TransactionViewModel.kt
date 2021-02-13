package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.*
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.TransactionRepository
import br.com.casadedeus.service.utils.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

/*class ExpenditureViewModelFactory(
    private val application: Application,
    private val monthKey: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ExpenditureViewModel(application, monthKey) as T
    }
}*/

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    // Contexto e acesso a dados
    private val context = application.applicationContext//quando precisa do contexto
    private val mRepository: TransactionRepository = TransactionRepository(context)

    private var mTransactionList = MutableLiveData<List<TransactionModel>>()
    val transactionlist: LiveData<List<TransactionModel>> = mTransactionList

    private var mTransaction = MutableLiveData<TransactionModel>()
    val transaction: LiveData<TransactionModel> = mTransaction

    private var mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private var mDelete = MutableLiveData<ValidationListener>()
    val delete: LiveData<ValidationListener> = mDelete

    //Labels mostrando os balanços
    private var mProfit = MutableLiveData<String>()
    val profit: LiveData<String> = mProfit

    private var mExpenditure = MutableLiveData<String>()
    val expenditure: LiveData<String> = mExpenditure

    private var mBalance = MutableLiveData<String>()
    val balance: LiveData<String> = mBalance

    fun save(transactionModel: TransactionModel) {
        //transactionModel.day = Calendar.getInstance().time
        /*if (expenditureModel.day == null) {

            //mValidation.value = ValidationListener("O dia está vazio")
            //return
        }
        if (transactionModel.description.isEmpty()) {
            mValidation.value = ValidationListener("A descrição está vazio")
            return
        }*/

        if (transactionModel.amount == 0.0) {
            mValidation.value =
                ValidationListener(context.getString(R.string.please_insert_amount_valid))
            return
        }
        if(transactionModel.category.isEmpty()){
            mValidation.value =
                ValidationListener(context.getString(R.string.select_category_transaction))
            return
        }
        if (!transactionModel.isEntry) {
            transactionModel.amount = transactionModel.amount * -1
        }
        if (transactionModel.key == "") {
            mRepository.save(transactionModel, object : OnCallbackListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
        } else {
            update(transactionModel)
        }
    }

    private fun update(transactionModel: TransactionModel) {
        mRepository.update(transactionModel, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mValidation.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun delete(transactionKey: String) {
        mRepository.delete(transactionKey, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                mDelete.value = ValidationListener()
            }

            override fun onFailure(message: String) {
                mDelete.value = ValidationListener(message)
            }

        })
    }

    fun get(key: String) {
        mRepository.getTransaction(key, object : OnCallbackListener<TransactionModel?> {
            override fun onSuccess(result: TransactionModel?) {
                mTransaction.value = result
            }

            override fun onFailure(message: String) {
                mValidation.value = ValidationListener(message)
            }
        })
    }

    fun load(currentDate: String, filter: Int) {
        val format = SimpleDateFormat("MMM, yyyy", Locale("pt", "BR"))// mes por extenso
        val dateFormat = format.parse(currentDate)

        val startDateTime = Timestamp(getDaysOfMonth(dateFormat))
        val endDateTime = Timestamp(getDaysOfMonth(dateFormat, true))

        var query = Query.Direction.DESCENDING
        if (filter == 0) {
            query = Query.Direction.ASCENDING
        }
        mRepository.getTransactions(
            startDateTime,
            endDateTime,
            query,
            object : OnCallbackListener<List<TransactionModel>> {
                override fun onSuccess(result: List<TransactionModel>) {
                    mTransactionList.value = orderBy(filter, result)
                    mBalance.value =
                        Utils.doubleToRealNotCurrency(result.sumByDouble { it.amount })
                        //result.sumByDouble { it.amount }.toString()
                    mExpenditure.value =
                        Utils.doubleToReal(result.sumByDouble { if (!it.isEntry) it.amount else 0.0 })
                    mProfit.value =
                        Utils.doubleToReal(result.sumByDouble { if (it.isEntry) it.amount else 0.0 })
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
    }

    private fun orderBy(filter: Int, list: List<TransactionModel>): List<TransactionModel> {
        when (filter) {
            1 -> {
                return list.sortedWith(compareByDescending<TransactionModel> { it.amount }
                    .thenByDescending { it.amount })
            }
            2 -> {
                return list.sortedWith(compareBy<TransactionModel> { it.amount }
                    .thenBy { it.amount })

            }
            3 -> {
                return list.sortedBy { it.category }
            }
            else -> {
                return list
            }
        }
    }

    fun getYearMonthFromString(str: String, isMonth: Boolean = false): Int {
        SimpleDateFormat("MMM, yyyy", Locale("pt", "BR")).parse(str).let {
            return getYearMonthFromDate(it, isMonth)
        }
    }

    private fun getYearMonthFromDate(date: Date, isMonth: Boolean = false): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        if (isMonth) {
            return calendar.get(Calendar.MONTH)
        }
        return calendar.get(Calendar.YEAR)
    }

    private fun getDaysOfMonth(date: Date, isEnd: Boolean = false): Date {
        //Lógica para pegar todos os dias do mês
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.MONTH, getYearMonthFromDate(date, true))
        calendar.set(Calendar.YEAR, getYearMonthFromDate(date))
        if (isEnd) {
            //end date
            val monthMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.DAY_OF_MONTH, monthMaxDays)
            return calendar.time
        }
        return calendar.time
    }


}