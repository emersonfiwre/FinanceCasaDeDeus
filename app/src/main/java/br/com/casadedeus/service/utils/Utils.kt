package br.com.casadedeus.service.utils

import kotlinx.android.synthetic.main.dialog_single_input.view.*
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun realToDouble(value: String): Double {
            //val s = value.replace("[.,]".toRegex(), "")
            val s = value.replace(".", "").replace(",", ".").replace("R$","")
            return s.toDouble()
        }
        fun doubleToReal(value: Double): String {
            val ptBr = Locale("pt", "BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(value)
        }
    }
}