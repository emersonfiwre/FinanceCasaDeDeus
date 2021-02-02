package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude

data class UserModel(
    @get:Exclude val key: String = "",
    var name: String = "",
    var email: String = "",
    @get:Exclude var password: String = ""
)