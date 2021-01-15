package br.com.casadedeus.service.constants

import android.content.Context
import br.com.casadedeus.R
import br.com.casadedeus.beans.CategoryModel

class CategoryConstansts private constructor() {

    companion object {
        fun getCategoriesExpenditure(context: Context): List<CategoryModel> {
            return arrayListOf(
                CategoryModel("Lazer",context.resources.getDrawable(R.drawable.ic_lazer)),
                CategoryModel("Alimentação",context.resources.getDrawable(R.drawable.ic_alimentacao)),
                CategoryModel("Educação",context.resources.getDrawable(R.drawable.ic_educacao)),
                CategoryModel("Moradia",context.resources.getDrawable(R.drawable.ic_moradia)),
                CategoryModel("Roupa",context.resources.getDrawable(R.drawable.ic_roupa)),
                CategoryModel("Saúde",context.resources.getDrawable(R.drawable.ic_saude)),
                CategoryModel("Transporte",context.resources.getDrawable(R.drawable.ic_transporte)),
                //CategoryModel("Gasto Fixo",context.resources.getDrawable(R.drawable.ic_trending_up)),
                CategoryModel("Outros",context.resources.getDrawable(R.drawable.ic_outros))
            )
        }

        /*
        <item>Lazer</item>
        <item>Alimentação</item>
        <item>Educação</item>
        <item>Moradia</item>
        <item>Roupa</item>
        <item>Saúde</item>
        <item>Transporte</item>
        <item>Gasto Fixo</item>
        <item>Outros</item>
         */

        fun getCategoriesProfit(context: Context): List<CategoryModel> {
            return arrayListOf(
                return arrayListOf(
                    CategoryModel("Salário",context.resources.getDrawable(R.drawable.ic_salario)),
                    CategoryModel("Investimento",context.resources.getDrawable(R.drawable.ic_investimento)),
                    CategoryModel("Presente",context.resources.getDrawable(R.drawable.ic_presente)),
                    CategoryModel("Prêmios",context.resources.getDrawable(R.drawable.ic_vendas)),
                    CategoryModel("Vendas",context.resources.getDrawable(R.drawable.ic_vendas)),
                    CategoryModel("Serviços",context.resources.getDrawable(R.drawable.ic_servico)),
                    CategoryModel("Outros",context.resources.getDrawable(R.drawable.ic_outros))
                )
            )
        }
        /*
        *

    <string-array name="categories_profit">
        <item>Salário</item>
        <item>Investimento</item>
        <item>Presente</item>
        <item>Prêmios</item>
        <item>Vendas</item>
        <item>Serviços</item>
        <item>Outros</item>
    </string-array>*/
    }

}