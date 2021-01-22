package br.com.casadedeus.beans

import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

data class GoalModel(
    @get:Exclude val key: String? = "",
    var startday: Date? = null,
    var finishday: Date? = null,
    var finish: Boolean? = false,
    var description: String? = "",
    var amount: Double
) : Serializable