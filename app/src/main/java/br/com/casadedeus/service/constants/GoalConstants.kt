package br.com.casadedeus.service.constants

class GoalConstants {
    companion object {
        const val AMOUNT = "amount"
        const val DESCRIPTION = "description"
        const val FINISH = "finish"
        const val FINISH_DAY = "finishday"
        const val START_DAY = "startday"
        const val GOALS = "goals"
    }

    object ERRORS {
        const val GOAL_REPOSITORY = "GoalRepoError"
    }
}