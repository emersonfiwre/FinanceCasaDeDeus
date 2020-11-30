package br.com.casadedeus.beans

data class Expenditure(
    var key: String = "",
    val day: String,
    val isEntry: Boolean,
    val desc: String = "",
    val category: String,
    val companyName: String = "",
    val notaFiscal: String = "",
    val amount: Double
)