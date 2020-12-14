package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class YearModel(@get:Exclude val key: String = "", val yearTitle: String) : Serializable