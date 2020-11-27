package br.com.casadedeus.beans

data class Expenditure(
    var key: String = "",
    val dia: String,
    val isEntry: Boolean,
    val desc: String = "",
    val category: String,
    val razaoSocial: String = "",
    val notaFiscal: String = "",
    val valor: Double
)