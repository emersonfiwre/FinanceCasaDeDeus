package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude

data class Year(
    @get:Exclude
    var key: String = "",
    val yearTitle: String
)

