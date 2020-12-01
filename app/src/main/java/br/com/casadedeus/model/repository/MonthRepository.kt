package br.com.casadedeus.model.repository

import br.com.casadedeus.beans.Month
import br.com.casadedeus.view.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MonthRepository {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getMonths(path: String, listener: OnCallbackListener<List<Month>>) {
        val months: MutableList<Month> = arrayListOf()
        mDatabase.collection("$path/months/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val monthTitle = document.data["monthTitle"].toString()
                        months.add(Month(key, monthTitle))
                        listener.onSuccess(orderby(months))
                        println("${document.id} => ${document.data}")
                    }
                }
            }.addOnFailureListener {
                val message = it.message.toString()
                listener.onFailure(message)
            }
    }

    companion object {
        private var mList = mutableListOf(

            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
    }

    private fun orderby(list: List<Month>): List<Month> {
        val local = Locale("pt", "BR")
        return list.sortedBy {
            SimpleDateFormat("MMMM", local)
                .parse(it.montTitle)
        }
    }

    fun save(month: String): Boolean {
        if (!mList.contains(month)) {
            mList.add(month)
            return true
        }
        return false
    }

    fun delete(month: String): Boolean {
        mList.remove(month)
        return true
    }

}