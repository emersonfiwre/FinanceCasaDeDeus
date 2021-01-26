package br.com.casadedeus.service.utils

import android.util.Log
import br.com.casadedeus.service.constants.ViewConstants
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
            val s = value.replace(".", "").replace(",", ".").replace("R$", "").trim()
            return try {
                s.toDouble()
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                0.0
            }
        }

        fun doubleToReal(value: Double?): String {
            if (value == null || value == 0.0) {
                return "R$0,00"
            }
            val ptBr = Locale("pt", "BR")
            return try {
                NumberFormat.getCurrencyInstance(ptBr).format(value)
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                "R$0,00"
            }
        }

        fun doubleToRealNotCurrency(value: Double?): String {
            if (value == null || value == 0.0) {
                return "0,00"
            }
            val ptBr = Locale("pt", "BR")
            return try {
                NumberFormat.getCurrencyInstance(ptBr).format(value).replace("R$", "")
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                "0,00"
            }
        }

        fun getCurrentMonth(): String {
            val sdf = SimpleDateFormat("MMM, yyyy", Locale("pt", "BR"))
            return sdf.format(Date())
        }

        fun getToday(): String {
            val sdf = SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))
            return sdf.format(Date())
        }

        fun stringToMonth(str: String): Date? {
            return try {
                SimpleDateFormat("MM-yyyy", Locale("pt", "BR")).parse(str)
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                null
            }
        }

        fun stringToDate(str: String): Date? {
            return try {
                SimpleDateFormat("dd-MM-yyyy", Locale("pt", "BR")).parse(str)
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                null
            }
        }

        fun todayToDate(str: String): Date? {
            return try {
                SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR")).parse(str)
            } catch (e: Exception) {
                Log.e(ViewConstants.LOG.CONVERSION_ERROR, e.message.toString())
                null
            }
        }
    }
}