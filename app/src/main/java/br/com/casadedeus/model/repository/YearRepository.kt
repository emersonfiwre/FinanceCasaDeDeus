package br.com.casadedeus.model.repository

import androidx.lifecycle.ViewModel
import br.com.casadedeus.view.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore

class YearRepository {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getYears(listener: OnCallbackListener<List<String>>) {
        val years: MutableList<String> = arrayListOf()
        mDatabase.collection("users/2D6MxXyqAA2gaDM3Ya9y/years/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        years.add(key)
                        listener.callback(orderby(years))
                        //println("${document.id} => ${document.data}")
                    }
                }
            }.addOnFailureListener {
                val message = it.message.toString()
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
    }

    private fun orderby(list: List<String>): List<String> {
        return list.sortedByDescending { it }
    }

    fun save(year: String): Boolean {
        mList.add(year)
        return true
    }

    fun delete(year: String): Boolean {
        mList.remove(year)
        return true
    }

}