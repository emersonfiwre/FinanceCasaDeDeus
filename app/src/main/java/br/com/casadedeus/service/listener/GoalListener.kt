package br.com.casadedeus.service.listener

import br.com.casadedeus.beans.GoalModel

interface GoalListener {
    /**
     * Click para edição
     */
    fun onItemClick(item: GoalModel)

    /**
     * Remoção
     */
    fun onDeleteClick(id: String)

    /**
     * Completa tarefa
     */
    fun onCompleteClick(id: String)

    /**
     * Descompleta tarefa
     */
    fun onUndoClick(id: String)


}