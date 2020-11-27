package br.com.casadedeus.model.repository

import br.com.casadedeus.beans.Expenditure

class ExpenditureRepository {

    companion object {
        private var mList = mutableListOf(
            Expenditure(
                "-Mfgsdjng",
                "qui, 29 out 2020",
                false,
                "Compra de vinhos e pães lorem ipsum, equipamento de som de radio",
                "Gasto Fixo",
                "Armarinho Fernandes",
                "675s",
                235.96
            ),
            Expenditure(
                "-Grjssnds",
                "sáb, 21 set 2020",
                false,
                "Compra de materias de construção ",
                "Serviços",
                "Leroy Merlim LTDA",
                "1234",
                5599.11
            ),
            Expenditure(
                "-Ropksdbs",
                "seg, 16 mar 2020",
                true,
                "Recebimento de pagamento",
                "Salário",
                "Empresa fulano de tal",
                "",
                969.96
            )
        )
    }

    fun save(expenditure: Expenditure): Boolean {
        mList.add(expenditure)
        return true
    }

    fun update(expenditure: Expenditure): Boolean {
        return true
    }

    fun delete(expenditure: Expenditure): Boolean {
        mList.remove(expenditure)
        return true
    }

    fun getExpenditure(expenditure: Expenditure): Expenditure? {
        return expenditure
    }

    fun orderby(list: List<Expenditure>) {}

    fun getExpenditures(): List<Expenditure> {
        return mList
    }
}