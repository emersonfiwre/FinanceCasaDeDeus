package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.constants.UserConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.*

class TransactionRepository(private val context: Context) {

    private val mDatabase = FirebaseFirestore.getInstance()

    //Recuperando o uid do usuário.
    private val mSecurityPreferences = SecurityPreferences(context)
    val userKey = mSecurityPreferences.get(UserConstants.SHARED.USER_KEY)

    fun getTransactions(
        startDateTime: Timestamp,
        endDateTime: Timestamp,
        query: Query.Direction,
        listener: OnCallbackListener<List<TransactionModel>>
    ) {
        val transactionModels: MutableList<TransactionModel> = arrayListOf()
        //mDatabase.collection("users/$userKey/expenditures/")
        /*val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())*/
        /*val dateS = SimpleDateFormat("dd-MM-yyyy").parse("01-01-2021")
        val dateE = SimpleDateFormat("dd-MM-yyyy").parse("30-01-2021")*/

        mDatabase.collection("users/$userKey/transactions/")
            .whereGreaterThanOrEqualTo(TransactionConstants.DAY, startDateTime)
            .whereLessThanOrEqualTo(TransactionConstants.DAY, endDateTime)
            //.whereGreaterThan("day", startDate)
            //.whereLessThan("day",startDate)//funcionou
            .orderBy(TransactionConstants.DAY, query)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val timestamp = document.data[TransactionConstants.DAY] as Timestamp
                        val isEntry = document.data[TransactionConstants.ENTRY] as Boolean
                        val desc = document.data[TransactionConstants.DESCRIPTION] as String
                        val category = document.data[TransactionConstants.CATEGORY] as String
                        val companyName = document.data[TransactionConstants.COMPANY_NAME] as String
                        val notaFiscal = document.data[TransactionConstants.NOTA_FISCAL] as String
                        val amount = document.data[TransactionConstants.AMOUNT] as Double
                        val ex = TransactionModel(
                            key,
                            timestamp.toDate(),
                            isEntry,
                            desc,
                            category,
                            companyName,
                            notaFiscal,
                            amount
                        )
                        transactionModels.add(ex)
                        //println("${document.id} => ${document.data}")
                    }
//                    listener.onSuccess(transactionModels)
                }
                listener.onSuccess(transactionModels)

            }.addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.TRANSACTION_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun save(transactionModel: TransactionModel, listener: OnCallbackListener<Boolean>) {
        //mDatabase.collection("users/$userKey/transactions/")
        mDatabase.collection("users/$userKey/transactions/")
            .add(transactionModel)
            .addOnSuccessListener { documentReference ->
                //println("DocumentSnapshot added with ID: ${documentReference.id}")
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.TRANSACTION_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun update(transactionModel: TransactionModel, listener: OnCallbackListener<Boolean>) {
        transactionModel.key?.let {
            mDatabase.collection("users/$userKey/transactions")
                .document(it)
                .set(transactionModel)
                .addOnSuccessListener {
                    listener.onSuccess(true)
                }
                .addOnFailureListener {
                    val message = it.message.toString()
                    Log.e(TransactionConstants.ERRORS.TRANSACTION_REPOSITORY, message)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                }
        }
    }

    fun delete(transactionKey: String, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/$userKey/transactions").document(transactionKey)
            .delete()
            .addOnSuccessListener { listener.onSuccess(true) }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.TRANSACTION_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


    }

    fun getTransaction(transactionKey: String, listener: OnCallbackListener<TransactionModel?>) {
        mDatabase.collection("users/transactions").document(transactionKey)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val key = document.id
                    val timestamp = document.data?.get(TransactionConstants.DAY) as Timestamp
                    val isEntry = document.data?.get(TransactionConstants.ENTRY) as Boolean
                    val desc = document.data?.get(TransactionConstants.DESCRIPTION) as String
                    val category = document.data?.get(TransactionConstants.CATEGORY) as String
                    val companyName = document.data?.get(TransactionConstants.COMPANY_NAME) as String
                    val notaFiscal = document.data?.get(TransactionConstants.NOTA_FISCAL) as String
                    val amount = document.data?.get(TransactionConstants.AMOUNT) as Double
                    val ex = TransactionModel(
                        key,
                        timestamp.toDate(),
                        isEntry,
                        desc,
                        category,
                        companyName,
                        notaFiscal,
                        amount
                    )
                    listener.onSuccess(ex)

                } else {
                    listener.onFailure(context.getString(R.string.transaction_not_found))
                }
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.TRANSACTION_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

}