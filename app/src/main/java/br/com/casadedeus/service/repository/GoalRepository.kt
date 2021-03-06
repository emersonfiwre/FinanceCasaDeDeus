package br.com.casadedeus.service.repository

import android.content.Context
import android.util.Log
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.constants.GoalConstants
import br.com.casadedeus.service.constants.UserConstants
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GoalRepository(private val context: Context) {
    private val mDatabase = FirebaseFirestore.getInstance()

    //Recuperando o uid do usuário.
    private val mSecurityPreferences = SecurityPreferences(context)
    private val userKey = mSecurityPreferences.get(UserConstants.SHARED.USER_KEY)

    fun getGoals(
        listener: OnCallbackListener<List<GoalModel>>
    ) {
        val goalModels: MutableList<GoalModel> = arrayListOf()
        mDatabase.collection("users/$userKey/goals/")
            .orderBy(GoalConstants.START_DAY, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val startday = document.data[GoalConstants.START_DAY] as Timestamp
                        val finishday = document.data[GoalConstants.FINISH_DAY] as Timestamp
                        val finish = document.data[GoalConstants.FINISH] as Boolean
                        val description = document.data[GoalConstants.DESCRIPTION] as String
                        val amount = document.data[GoalConstants.AMOUNT] as Double
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
                Log.e(GoalConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun save(goalModel: GoalModel, listener: OnCallbackListener<Boolean>) {
        //mDatabase.collection("users/$userKey/transactions/")
        mDatabase.collection("users/$userKey/goals/")
            .add(goalModel)
            .addOnSuccessListener { documentReference ->
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(GoalConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }

    fun update(goalModel: GoalModel, listener: OnCallbackListener<Boolean>) {
        goalModel.key?.let {
            mDatabase.collection("users/$userKey/goals/")
                .document(it)
                .set(goalModel)
                .addOnSuccessListener {
                    listener.onSuccess(true)
                }
                .addOnFailureListener {
                    val message = it.message.toString()
                    Log.e(GoalConstants.ERRORS.GOAL_REPOSITORY, message)
                    listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
                }
        }
    }

    fun delete(goalKey: String, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/$userKey/goals").document(goalKey)
            .delete()
            .addOnSuccessListener { listener.onSuccess(true) }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(GoalConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }
    }


    fun updateStatus(key: String, complete: Boolean, listener: OnCallbackListener<Boolean>) {
        mDatabase.collection("users/$userKey/goals")
            .document(key)
            .update(GoalConstants.FINISH, complete)
            .addOnSuccessListener {
                listener.onSuccess(true)
            }
            .addOnFailureListener {
                val message = it.message.toString()
                Log.e(GoalConstants.ERRORS.GOAL_REPOSITORY, message)
                listener.onFailure(context.getString(R.string.ERROR_UNEXPECTED))
            }


    }
}