package br.com.casadedeus.service.repository

import android.content.Context
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TransactionRepository(private val mContext: Context) {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getExpenditures(listener: OnCallbackListener<List<TransactionModel>>) {
        val transactionModels: MutableList<TransactionModel> = arrayListOf()
        //mDatabase.collection("months").document(path).collection("expenditures")
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/expenditures/")
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
                    listener.onSuccess(transactionModels)
                }

            }.addOnFailureListener {
                val message = it.message.toString()
                listener.onFailure(message)
            }
        val s = ""
    }

    fun save(transactionModel: TransactionModel, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/expenditures/")
            .add(transactionModel)
            .addOnSuccessListener { documentReference ->
                //println("DocumentSnapshot added with ID: ${documentReference.id}")
                listener.onSuccess(true)
            }
            .addOnFailureListener { e ->
                println("Error adding document $e")
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED))

            }
    }

    fun update(transactionModel: TransactionModel): Boolean {
        return true
    }

    fun delete(transactionModel: TransactionModel): Boolean {
        return true
    }

    fun getExpenditure(transactionModel: TransactionModel): TransactionModel? {
        return null
    }

}