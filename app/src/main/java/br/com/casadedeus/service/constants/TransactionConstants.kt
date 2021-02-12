package br.com.casadedeus.service.constants

class TransactionConstants private constructor() {

    companion object {
        const val AMOUNT = "amount"
        const val CATEGORY = "category"
        const val PLACE = "place"
        const val DAY = "day"
        const val DESCRIPTION = "description"
        const val ENTRY = "entry"
        const val TRANSACTIONS = "transactions"

    }

    object ERRORS {
        const val TRANSACTION_REPOSITORY = "TransactionRepoError"
    }

}