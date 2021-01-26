package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.constants.TransactionConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GoalRepository(private val context: Context) {
    private val mDatabase = FirebaseFirestore.getInstance()

    //Recuperando o uid do usuário.
    private val mSecurityPreferences = SecurityPreferences(context)
    val userKey = mSecurityPreferences.get(TransactionConstants.SHARED.USER_KEY)

    fun getGoals(
        listener: OnCallbackListener<List<GoalModel>>
    ) {
        val goalModels: MutableList<GoalModel> = arrayListOf()
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/goals/")
            .orderBy("startday", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val startday = document.data["startday"] as Timestamp
                        val finishday = document.data["finishday"] as Timestamp
                        val finish = document.data["finish"] as Boolean
                        val description = document.data["description"] as String
                        val amount = document.data["amount"] as Double
                        val g = GoalModel(
                            key,
                            startday.toDate(),
                            finishday.toDate(),
                            finish,
                            description,
                            amount
                        )
                        goalModels.add(g)
                    }
                }
                listener.onSuccess(goalModels)

            }.addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun save(goalModel: GoalModel, listener: OnCallbackListener<Boolean>) {
        //mDatabase.collection("users/$userKey/transactions/")
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/goals/")
            .add(goalModel)
            .addOnSuccessListener { documentReference ->
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun update(goalModel: GoalModel, listener: OnCallbackListener<Boolean>) {
        goalModel.key?.let {
            mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/goals/")
                .document(it)
                .set(goalModel)
                .addOnSuccessListener {
                    listener.onSuccess(true)
                }
                .addOnFailureListener {
                    val message = it.message.toString()
                    Log.e(TransactionConstants.ERRORS.GOAL_REPOSITORY, message)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                }
        }
    }

    fun delete(goalKey: String, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/goals").document(goalKey)
            .delete()
            .addOnSuccessListener { listener.onSuccess(true) }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(TransactionConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }
}