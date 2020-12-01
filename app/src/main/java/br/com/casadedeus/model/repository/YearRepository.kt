package br.com.casadedeus.model.repository

import android.util.Log
import br.com.casadedeus.beans.Year
import br.com.casadedeus.view.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore

class YearRepository {
    //****FAZER VALIDAÇÃO PARA VER SE NÃO JA EXISTE ESTE ANO*******/
    private val mDatabase = FirebaseFirestore.getInstance()

    fun getYears(listener: OnCallbackListener<List<Year>>) {
        val years: MutableList<Year> = arrayListOf()
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/years/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val yearTitle = document.data["yearTitle"].toString()
                        years.add(Year(key,yearTitle))
                        listener.onSuccess(orderby(years))
                        //println("${document.id} => ${document.data}")
                    }
                }
            }.addOnFailureListener {
                val message = it.message.toString()
                listener.onFailure(message)
            }
    }

    companion object {
        private var mList = mutableListOf(
            "2019",
            "2018",
            "2017",
            "2016",
            "2015"
        )
        private const val TAG: String = "YearRepository"
    }

    private fun orderby(list: List<Year>): List<Year> {
        return list.sortedByDescending { it.yearTitle }
    }

    fun save(year: Year, listener: OnCallbackListener<Boolean>) {
        /*val hashYear = hashMapOf(
            "yearTitle" to year
        )*/
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/years/")
            .add(year)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                listener.onSuccess(true)
            }
            .addOnFailureListener { e ->
                println("Error adding document $e")
                listener.onFailure(e.message.toString())
            }
    }

    fun delete(year: Year) {
        //mList.remove(year)
        mDatabase.collection("cities").document(year.key)
            .delete()
            .addOnSuccessListener { Log.i(Companion.TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(Companion.TAG, "Error deleting document", e) }
    }

}