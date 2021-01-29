package br.com.casadedeus.service.constants

class TransactionConstants private constructor() {

    object SHARED {
        const val USER_SHARED = "userShared"
        const val USER_KEY = "userKey"
        const val USER_NAME = "userName"
    }

    object ERRORS{
        const val USER_REPOSITORY = "UserRepoError"
        const val TRANSACTION_REPOSITORY = "TransactionRepoError"
    }

}