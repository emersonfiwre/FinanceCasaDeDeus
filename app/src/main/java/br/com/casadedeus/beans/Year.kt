package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Year(
    @get:Exclude
    var key: String = "",
    val yearTitle: String
) : Serializable

