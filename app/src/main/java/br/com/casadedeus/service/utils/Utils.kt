package br.com.casadedeus.service.utils

import kotlinx.android.synthetic.main.dialog_single_input.view.*
import java.text.NumberFormat
import java.util.*

class Utils {
    companion object {
        fun realToDouble(value: String?): Double {
            //val s = value.replace("[.,]".toRegex(), "")
            if (value == null || value == "") {
                return 0.0
            }
            val s = value.replace(".", "").replace(",", ".").replace("R$", "")
            return s.toDouble()
        }

        fun doubleToReal(value: Double?): String {
            if (value == null || value == 0.0) {
                return "R$0,00"
            }
            val ptBr = Locale("pt", "BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(value)
        }
        fun doubleToRealNotCurrency(value: Double?): String {
            if (value == null || value == 0.0) {
                return "0,00"
            }
            val ptBr = Locale("pt", "BR")
            return NumberFormat.getCurrencyInstance(ptBr).format(value).replace("R$","")
        }
    }
}