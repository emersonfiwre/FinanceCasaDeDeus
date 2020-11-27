package br.com.casadedeus.model

import br.com.casadedeus.model.repository.YearRepository
import java.util.*

class YearModel {
    private var yearRepository = YearRepository()

    fun save(year: String?): Boolean {
        return if (year != null) {
            yearRepository.save(year)
        } else {
            throw Exception("Selecione um ano")
        }
    }

    fun delete(year: String?) {
        if (year != null) {
            yearRepository.delete(year)
        } else {
            throw Exception("O ano é nulo")
        }
    }
}