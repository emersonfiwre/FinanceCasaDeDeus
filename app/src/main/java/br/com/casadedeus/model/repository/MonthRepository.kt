package br.com.casadedeus.model.repository

import br.com.casadedeus.view.listener.OnCallbackListener
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class MonthRepository {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getMonthss(path: String, listener: OnCallbackListener<List<String>>) {
        val months: MutableList<String> = arrayListOf()
        mDatabase.collection("$path/months/")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        months.add(key)
                        listener.onSuccess(orderby(months))
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

    private fun orderby(list: List<String>): List<String> {
        val local = Locale("pt", "BR")
        return list.sortedBy {
            SimpleDateFormat("MMMM", local)
                .parse(it)
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

    fun getMonths(): List<String> {
        return orderby(mList)
    }
}