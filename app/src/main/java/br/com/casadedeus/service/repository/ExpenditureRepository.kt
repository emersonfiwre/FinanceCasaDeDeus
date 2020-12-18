package br.com.casadedeus.service.repository

import android.content.Context
import br.com.casadedeus.R
import br.com.casadedeus.beans.ExpenditureModel
import br.com.casadedeus.service.listener.OnCallbackListener
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ExpenditureRepository(private val mContext: Context, private val mMonthKey: String) {

    private val mDatabase = FirebaseFirestore.getInstance()

    fun getExpenditures(path: String, listener: OnCallbackListener<List<ExpenditureModel>>) {
        val expenditureModels: MutableList<ExpenditureModel> = arrayListOf()
        //mDatabase.collection("months").document(path).collection("expenditures")
        mDatabase.collection("users/WqVSBEFTfLTRSPLNV52k/years/dJ0VRxauGWo7akOBpadH/months/$path/expenditures")
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
                        val ex = ExpenditureModel(
                            key,
                            day,
                            isEntry,
                            desc,
                            category,
                            companyName,
                            notaFiscal,
                            amount
                        )
                        expenditureModels.add(ex)
                        //println("${document.id} => ${document.data}")
                    }
                    listener.onSuccess(expenditureModels)
                }

            }.addOnFailureListener {
                val message = it.message.toString()
                listener.onFailure(message)
            }
        val s = ""
    }

    companion object {
        private var mList = mutableListOf(
            ExpenditureModel(
                "-Mfgsdjng",
                "qui, 29 out 2020",
                false,
                "Compra de vinhos e pães lorem ipsum, equipamento de som de radio",
                "Gasto Fixo",
                "Armarinho Fernandes",
                "675s",
                235.96
            ),
            ExpenditureModel(
                "-Grjssnds",
                "sáb, 21 set 2020",
                false,
                "Compra de materias de construção ",
                "Serviços",
                "Leroy Merlim LTDA",
                "1234",
                5599.11
            ),
            ExpenditureModel(
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

    fun save(expenditureModel: ExpenditureModel, listener: OnCallbackListener<Boolean>) {
        mDatabase
            .document(mMonthKey)
            .collection("months")
            .add(expenditureModel)
            .addOnSuccessListener { documentReference ->
                //println("DocumentSnapshot added with ID: ${documentReference.id}")
                listener.onSuccess(true)
            }
            .addOnFailureListener { e ->
                println("Error adding document $e")
                listener.onFailure(mContext.getString(R.string.ERROR_UNEXPECTED))

            }
    }

    fun update(expenditureModel: ExpenditureModel): Boolean {
        return true
    }

    fun delete(expenditureModel: ExpenditureModel): Boolean {
        return true
    }

    fun getExpenditure(expenditureModel: ExpenditureModel): ExpenditureModel? {
        return null
    }

}