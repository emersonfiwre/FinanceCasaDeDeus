package br.com.casadedeus.model

import br.com.casadedeus.beans.Expenditure
import br.com.casadedeus.model.repository.ExpenditureRepository
import java.lang.Exception

class ExpenditureModel {
    //inserir a regra de negocio nas models, campos nao nulos, verificar valores para chamar o repository
    private val expenditureRepository = ExpenditureRepository()

    fun insert(expenditure: Expenditure) {
        if (expenditure.dia.isNotEmpty()) {
            throw Exception("O dia está vazio")
            return
        }
        if (expenditure.desc.isNotEmpty()){
            throw Exception("O desc está vazio")
            return
        }
        if (expenditure.valor.isNotEmpty()){
            throw Exception("O valor está vazio")
            return
        }

        expenditureRepository.insert(expenditure)
    }

    fun update(expenditure: Expenditure) {
        if (expenditure.dia.isNotEmpty()) {
            throw Exception("O dia está vazio")
            return
        }
        if (expenditure.desc.isNotEmpty()){
            throw Exception("O desc está vazio")
            return
        }
        if (expenditure.valor.isNotEmpty()){
            throw Exception("O valor está vazio")
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

    fun getAll() {
        expenditureRepository.getExpenditures()
    }

    fun getExpenditures(): List<Expenditure> {
        return listOf(
            Expenditure("sex, 23 out 2020", "Compra de vinhos e pães", "R$235,96"),
            Expenditure("qua, 23 jan 2020", "Compra de vinhos e pães", "R$465.599,11"),
            Expenditure("qui, 13 nov 2020", "Compra de vinhos e pães", "R$3.456.789,96"),
            Expenditure("sáb, 21 set 2020", "Compra de vinhos e pães", "R$235,96"),
            Expenditure("seg, 16 mar 2020", "Compra de vinhos e pães", "R$235,96"),
            Expenditure("qua, 01 jan 2020", "Compra de vinhos e pães", "R$235,96"),
            Expenditure(
                "qui, 29 out 2020",
                "Compra de vinhos e pães lorem ipsum, equipamento de som de radio",
                "R$235,96"
            )
        )
    }
}