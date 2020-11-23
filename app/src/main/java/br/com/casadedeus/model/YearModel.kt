package br.com.casadedeus.model

import br.com.casadedeus.model.repository.YearRepository
import java.util.*

class YearModel {
    private var yearRepository = YearRepository()

    fun insert(year:Calendar?){
        if(year != null){
            yearRepository.insert(year)
        }else{
            throw Exception("O ano é nulo")
        }
    }

    fun delete(year:String?){
        if(year != null){
            yearRepository.delete(year)
        }else{
            throw Exception("O ano é nulo")
        }
    }
}