package br.com.casadedeus.view.adapter

import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.listener.GoalListener
import br.com.casadedeus.service.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class GoalAdapter : RecyclerView.Adapter<MyViewHolder>() {

    private var mList: List<GoalModel> = arrayListOf()
    private var mListener: GoalListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val item =
            LayoutInflater.from(parent.context).inflate(R.layout.card_goal, parent, false)
        return MyViewHolder(item, mListener)
    }

    override fun getItemCount(): Int {
        return mList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(mList[position])
    }

    fun attachListener(listener: GoalListener) {
        mListener = listener
    }

    fun notifyChanged(list: List<GoalModel>) {
        mList = list
        notifyDataSetChanged()
    }

}

class MyViewHolder(itemView: View, val listener: GoalListener?) :
    RecyclerView.ViewHolder(itemView) {

    private val mDateShortFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private val mDateFullFormat: SimpleDateFormat = SimpleDateFormat("EEE, d MMM 'de' yyyy", Locale("pt", "BR"))

    private var mCard: View = itemView.findViewById(R.id.card_goal)
    private var mTextDescription: TextView = itemView.findViewById(R.id.txt_description_goal)
    private var mTextAmount: TextView = itemView.findViewById(R.id.txt_amount_goal)
    private var mTextDueDate: TextView = itemView.findViewById(R.id.txt_duedate_goal)
    private var mTextStartDate: TextView = itemView.findViewById(R.id.txt_startdate_goal)
    private var mImage: ImageView = itemView.findViewById(R.id.image_goal)

    /**
     * Atribui valores aos elementos de interface e também eventos
     */
    fun bindData(goal: GoalModel) {

        this.mTextAmount.text = Utils.doubleToReal(goal.amount)
        this.mTextDescription.text = goal.description
        this.mTextStartDate.text = mDateShortFormat.format(goal.startday)
        this.mTextDueDate.text = mDateFullFormat.format(goal.finishday)

        if (goal.finish == true) {
            this.mTextDescription.setTextColor(Color.GRAY)
            this.mTextDueDate.setTextColor(Color.GRAY)
            mImage.setImageResource(R.drawable.ic_done)
        } else {
            this.mTextDescription.setTextColor(Color.BLACK)
            this.mTextDueDate.setTextColor(Color.BLACK)
            mImage.setImageResource(R.drawable.ic_todo)
        }

        if(goal.description.isNullOrEmpty()){
            mTextDescription.visibility = View.GONE
        }else{
            mTextDescription.visibility = View.VISIBLE
        }

        // Eventos
        mCard.setOnClickListener { listener?.onItemClick(goal) }
        mImage.setOnClickListener {
            if (goal.finish == true) {
                listener?.onUndoClick(goal.key!!)
            } else {
                listener?.onCompleteClick(goal.key!!)
            }
        }

        mCard.setOnLongClickListener {
            AlertDialog.Builder(itemView.context)
                .setTitle(R.string.title_want_remove_goal)
                .setMessage(R.string.desc_want_remove_goal)
                .setPositiveButton(R.string.remove) { dialog, which ->
                    listener?.onDeleteClick(goal.key!!)
                }
                .setNeutralButton(R.string.cancel, null)
                .show()
            true
        }

    }

}