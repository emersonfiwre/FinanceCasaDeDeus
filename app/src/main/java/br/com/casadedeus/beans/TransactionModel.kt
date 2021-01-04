package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.util.*

data class TransactionModel(
    @get:Exclude val key: String? = "",
    var day: Date? = null,
    val isEntry: Boolean,
    val description: String? = "",
    val category: String,
    val companyName: String? = "",
    val notaFiscal: String? = "",
    val amount: Double
)