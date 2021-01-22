package br.com.casadedeus.service.constants

class ViewConstants private constructor() {

    object KEYS {
        const val WHAT_PICKER = "isMonth"
        const val TRANSACTION = "transaction"
        const val GOAL = "goal"
    }

    object TAGS {
        const val YEAR_FRAG = "yearFragment"
        const val ADD_TRANSACTION = "addTransactionFragment"
        const val TRANSACTION_FRAG = "monthFragment"
        const val YEAR_PICKER = "yearPickerDialog"
        const val MONTH_PICKER = "monthPickerDialog"
        const val GOAL_DIALOG = "GoalDialog"
        const val FILTERS_DIALOG = "filtersBottomSheetDialog"
    }
}