package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class TransactionRepository(private val context: Context) {

    private val mDatabase = FirebaseFirestore.getInstance()

    //Recuperando o uid do usuário.
    private val mSecurityPreferences = SecurityPreferences(context)
    val userKey = mSecurityPreferences.get(TransactionConstants.SHARED.USER_KEY)

    fun getTransactions(listener: OnCallbackListener<List<TransactionModel>>) {
        val transactionModels: MutableList<TransactionModel> = arrayListOf()
        //mDatabase.collection("users/$userKey/expenditures/")
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/transactions/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val timestamp = document.data["day"] as Timestamp
                        val isEntry = document.data["entry"] as Boolean
                        val desc = document.data["desc"] as String
                        val category = document.data["category"] as String
                        val companyName = document.data["companyName"] as String
                        val notaFiscal = document.data["notaFiscal"] as String
                        val amount = document.data["amount"] as Double
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
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/transactions/")
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
        mDatabase.collection("users/transactions")
            .document(transactionModel.key)
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

    fun delete(transactionModel: TransactionModel, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/transactions").document(transactionModel.key)
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
                    val timestamp = document.data?.get("day") as Timestamp
                    val isEntry = document.data?.get("entry") as Boolean
                    val desc = document.data?.get("desc") as String
                    val category = document.data?.get("category") as String
                    val companyName = document.data?.get("companyName") as String
                    val notaFiscal = document.data?.get("notaFiscal") as String
                    val amount = document.data?.get("amount") as Double
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