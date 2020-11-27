package br.com.casadedeus.model

import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.model.repository.ExpenditureRepository
import java.lang.Exception

class ExpenditureModel {
    //inserir a regra de negocio nas models, campos nao nulos, verificar valores para chamar o repository
    private val expenditureRepository = ExpenditureRepository()

    fun save(expenditure: Expenditure?): Boolean {
        if (expenditure != null) {
            if (expenditure.dia.isEmpty()) {
                throw Exception("O dia está vazio")
                return false
            }
            if (expenditure.desc.isEmpty()) {
                throw Exception("A descrição está vázia")
                return false
            }
            if (expenditure.category.isEmpty()) {
                throw Exception("Categoria não encontrada")
                return false
            }
            if (expenditure.valor == 0.0) {
                throw Exception("Valor não encontrada")
                return false
            }

            return expenditureRepository.save(expenditure)
        }
        return false
    }

    fun update(expenditure: Expenditure) {
        if (expenditure.dia.isNotEmpty()) {
            throw Exception("O dia está vazio")
            return
        }
        if (expenditure.desc.isNotEmpty()) {
            throw Exception("O desc está vazio")
            return
        }

        expenditureRepository.update(expenditure)
    }

    fun delete(expenditure: Expenditure) {
        expenditureRepository.delete(expenditure)
    }

    fun getExpenditure(expenditure: Expenditure) {
        expenditureRepository.getExpenditure(expenditure)
    }

    /*fun getAll(): List<Expenditure>{
        val expenditures = expenditureRepository.getExpenditures()
        if(expenditures.isEmpty()){
            //TODO
        }
        return expenditures
    }*/

}