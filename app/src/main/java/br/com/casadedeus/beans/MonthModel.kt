package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class MonthModel(@get:Exclude val key: String = "", val monthTitle: String) : Serializable