package br.com.casadedeus.service.utils

import kotlinx.android.synthetic.main.dialog_single_input.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
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

        fun getCurrentMonth(): String {
            val sdf = SimpleDateFormat("MMM, yyyy", Locale("pt", "BR"))
            return sdf.format(Date())
        }
        fun getToday(): String {
            val sdf = SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))
            return sdf.format(Date())
        }
        fun stringToMonth(str:String):Date?{
            return SimpleDateFormat("MM-yyyy",Locale("pt", "BR")).parse(str)
        }
        fun stringToDate(str:String):Date?{
            return SimpleDateFormat("dd-MM-yyyy",Locale("pt", "BR")).parse(str)
        }
    }
}