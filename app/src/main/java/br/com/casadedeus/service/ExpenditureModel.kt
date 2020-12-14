package br.com.casadedeus.service

import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.service.repository.ExpenditureRepository
import java.lang.Exception

class ExpenditureModel {
    //inserir a regra de negocio nas models, campos nao nulos, verificar valores para chamar o repository
    private val expenditureRepository = ExpenditureRepository()

    fun update(expenditure: Expenditure) {
        if (expenditure.day.isNotEmpty()) {
            throw Exception("O dia está vazio")
            return
        }
        if (expenditure.desc.isNotEmpty()) {
            throw Exception("O desc está vazio")
            return
        }
        if (expenditure.amount != 0.0) {
            throw Exception("O valor está vazio")
            return
        }

        expenditureRepository.update(expenditure)
    }

}