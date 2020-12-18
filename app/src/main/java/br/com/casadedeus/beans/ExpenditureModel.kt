package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude

data class ExpenditureModel(
    @get:Exclude val key: String = "",
    val day: String = "",
    val isEntry: Boolean,
    val desc: String = "",
    val category: String,
    val companyName: String = "",
    val notaFiscal: String = "",
    val amount: Double
)