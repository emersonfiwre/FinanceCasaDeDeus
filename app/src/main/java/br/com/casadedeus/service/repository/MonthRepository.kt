package br.com.casadedeus.service.repository

import android.util.Log
import br.com.casadedeus.beans.MonthModel
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MonthRepository private constructor(private val mYearKey: String) {
    companion object {
        fun newInstance(yearKey: String): MonthRepository {
            return MonthRepository(yearKey)
        }
    }

    private val TAG: String = "MonthRepository"

    //****FAZER VALIDAÇÃO PARA VER SE NÃO JA EXISTE ESTE MÊS*******/
    private val mDatabase = FirebaseFirestore.getInstance()

    fun getMonths(listener: OnCallbackListener<List<MonthModel>>) {
        val monthModels: MutableList<MonthModel> = arrayListOf()
        mDatabase.collection("$mYearKey/months/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val monthTitle = document.data["monthTitle"].toString()
                        monthModels.add(MonthModel(key, monthTitle))
                        listener.onSuccess(orderby(monthModels))
                        //println("${document.id} => ${document.data}")
                    }
                }
            }.addOnFailureListener {
                val message = it.message.toString()
                listener.onFailure(message)
            }
    }


    private fun orderby(list: List<MonthModel>): List<MonthModel> {
        val local = Locale("pt", "BR")
        return list.sortedBy {
            SimpleDateFormat("MMMM", local)
                .parse(it.monthTitle)
        }
        return list
    }

    fun save(monthModel: MonthModel, listener: OnCallbackListener<Boolean>) {
        /*if (!mList.contains(month)) {
            mList.add(month)
        }*/
        mDatabase
//            .collection("users")
//            .document("WqVSBEFTfLTRSPLNV52k")
//            .collection("years")
            .document(mYearKey)
            .collection("months")
            .add(monthModel)
            .addOnSuccessListener { documentReference ->
                //println("DocumentSnapshot added with ID: ${documentReference.id}")
                listener.onSuccess(true)
            }
            .addOnFailureListener { e ->
                println("Error adding document $e")
                listener.onFailure(e.message.toString())

            }
    }

    fun delete(monthModel: MonthModel) {
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/years/$mYearKey/months/")
            .document(monthModel.key)
            .delete()
            .addOnSuccessListener { Log.i(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        //mList.remove(month)
    }

}