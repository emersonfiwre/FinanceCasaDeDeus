package br.com.casadedeus.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.listener.OnCallbackListener
import br.com.casadedeus.service.listener.ValidationListener
import br.com.casadedeus.service.repository.GoalRepository
import java.util.*

class GoalViewModel(application: Application) : AndroidViewModel(application) {

    // Contexto e acesso a dados
    private val mContext = application.applicationContext//quando precisa do contexto
    private val mRepository: GoalRepository = GoalRepository(mContext)

    private var mGoalList = MutableLiveData<List<GoalModel>>()
    val goallist: LiveData<List<GoalModel>> = mGoalList

    private var mValidation = MutableLiveData<ValidationListener>()
    val validation: LiveData<ValidationListener> = mValidation

    private var mDelete = MutableLiveData<ValidationListener>()
    val delete: LiveData<ValidationListener> = mDelete


    fun save(goal: GoalModel) {
        if (goal.amount == 0.0) {
            mValidation.value =
                ValidationListener("É necessário um valor válido para o seu objetivo")
            return
        }
        if (goal.finishday == null) {
            mValidation.value =
                ValidationListener("É necessário uma data para o seu objetivo")
            return
        }
        if (goal.key == "") {
            goal.startday = Calendar.getInstance().time
            mRepository.save(goal, object : OnCallbackListener<Boolean> {
                override fun onSuccess(result: Boolean) {
                    mValidation.value = ValidationListener()
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
        } else {
            update(goal)
        }
    }

    private fun update(goal: GoalModel) {
        mRepository.update(goal, object : OnCallbackListener<Boolean> {
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

    fun load() {
        mRepository.getGoals(
            object : OnCallbackListener<List<GoalModel>> {
                override fun onSuccess(result: List<GoalModel>) {
                    mGoalList.value = result
                }

                override fun onFailure(message: String) {
                    mValidation.value = ValidationListener(message)
                }
            })
    }

    fun complete(id: String) {
        updateStatus(id, true)
    }

    fun undo(id: String) {
        updateStatus(id, false)
    }

    private fun updateStatus(id: String, complete: Boolean) {
        mRepository.updateStatus(id, complete, object : OnCallbackListener<Boolean> {
            override fun onSuccess(result: Boolean) {
                load()
            }

            override fun onFailure(message: String) {
            }

        })
    }


}