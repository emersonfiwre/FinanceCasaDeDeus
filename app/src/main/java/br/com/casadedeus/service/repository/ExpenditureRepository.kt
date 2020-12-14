package br.com.casadedeus.service.repository

import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ExpenditureRepository {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getExpendituress(path: String, listener: OnCallbackListener<List<Expenditure>>) {
        val expenditures: MutableList<Expenditure> = arrayListOf()
        mDatabase.collection("months/$path/expenditures")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    if (document.exists()) {
                        val key = document.id
                        val timestamp = document.data["day"] as Timestamp
                        val isEntry = document.data["isEntry"] as Boolean
                        val desc = document.data["description"] as String
                        val category = document.data["category"] as String
                        val companyName = document.data["companyName"] as String
                        val notaFiscal = document.data["notaFiscal"] as String
                        val amount = document.data["amount"] as Double
                        val local = Locale("pt", "BR")
                        val format =
                            SimpleDateFormat("EEE, d MMM 'de' yyyy", local)// dia por extenso
                        val day = format.format(timestamp.toDate())
                        val ex = Expenditure(
                            key,
                            day,
                            isEntry,
                            desc,
                            category,
                            companyName,
                            notaFiscal,
                            amount
                        )
                        expenditures.add(ex)
                        listener.onSuccess(expenditures)
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
            Expenditure(
                "-Mfgsdjng",
                "qui, 29 out 2020",
                false,
                "Compra de vinhos e pães lorem ipsum, equipamento de som de radio",
                "Gasto Fixo",
                "Armarinho Fernandes",
                "675s",
                235.96
            ),
            Expenditure(
                "-Grjssnds",
                "sáb, 21 set 2020",
                false,
                "Compra de materias de construção ",
                "Serviços",
                "Leroy Merlim LTDA",
                "1234",
                5599.11
            ),
            Expenditure(
                "-Ropksdbs",
                "seg, 16 mar 2020",
                true,
                "Recebimento de pagamento",
                "Salário",
                "Empresa fulano de tal",
                "",
                969.96
            )
        )
    }

    fun insert(expenditure: Expenditure): Boolean {
        return true
    }

    fun update(expenditure: Expenditure): Boolean {
        return true
    }

    fun delete(expenditure: Expenditure): Boolean {
        return true
    }

    fun getExpenditure(expenditure: Expenditure): Expenditure? {
        return null
    }

    fun getExpenditures(): List<Expenditure> {
        return mList
    }
}