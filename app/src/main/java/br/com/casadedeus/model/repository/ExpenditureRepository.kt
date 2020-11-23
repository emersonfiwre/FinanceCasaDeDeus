package br.com.casadedeus.model.repository

import br.com.casadedeus.beans.Expenditure

class ExpenditureRepository {
    fun insert(expenditure: Expenditure): Boolean {
        return true
    }

    fun update(expenditure: Expenditure): Boolean {
        return true
    }

    fun delete(expenditure: Expenditure): Boolean {
        return true
    }

    fun getExpenditure(expenditure: Expenditure): Expenditure? {
        return null
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