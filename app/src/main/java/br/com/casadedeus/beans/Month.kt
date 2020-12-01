package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude

data class Month(
    @get:Exclude
    var key: String = "",
    val montTitle: String
) {


}