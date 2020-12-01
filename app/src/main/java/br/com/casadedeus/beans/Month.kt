package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Month(
    @get:Exclude
    var key: String = "",
    val montTitle: String
) : Serializable