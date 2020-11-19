package br.com.casadedeus.model

import br.com.casadedeus.model.repository.MonthRepository
import java.lang.Exception
import java.util.*

class MonthModel {
    private var monthRepository = MonthRepository()

    fun insert(month:Calendar?){
        if (month != null){
            monthRepository.insert(month)
        }else{
            throw Exception("O mês está nulo")
        }
    }

    fun delete(month: String){
        if(month.isNotEmpty()){
            monthRepository.delete(month)
        }else{
            throw Exception("O mês está nulo")
        }
    }

    fun getMonths(): Array<String> {
        return arrayOf(
            "Janeiro",
            "Fevereiro",
            "Março",
            "Abril",
            "Maio",
            "Junho",
            "Julho",
            "Agosto",
            "Setembro",
            "Outubro",
            "Novembro",
            "Dezembro"
        )
    }
}