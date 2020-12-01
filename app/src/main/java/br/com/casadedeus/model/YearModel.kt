package br.com.casadedeus.model

import br.com.casadedeus.beans.Year
import br.com.casadedeus.model.repository.YearRepository
import br.com.casadedeus.view.listener.OnCallbackListener
import java.util.*

class YearModel {
    private var yearRepository = YearRepository()

    fun save(year: Year?, listener: OnCallbackListener<Boolean>) {
        return if (year != null) {
            yearRepository.save(year, listener)
        } else {
            throw Exception("Selecione um ano")
        }
    }

    fun delete(year: Year?) {
        if (year != null) {
            yearRepository.delete(year)
        } else {
            throw Exception("O ano é nulo")
        }
    }
}