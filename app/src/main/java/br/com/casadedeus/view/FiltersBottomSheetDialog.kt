package br.com.casadedeus.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.com.casadedeus.R
import br.com.casadedeus.service.listener.OnItemClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_dialog_filters.view.*


class FiltersBottomSheetDialog(private val selected: Int, val listener: OnItemClickListener<Int>) :
    BottomSheetDialogFragment(), View.OnClickListener {
    private lateinit var mDialogRoot: View
    private lateinit var mFilters: List<ImageView>
    private var mFilterSelection: Int = selected

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDialogRoot = inflater.inflate(R.layout.bottom_dialog_filters, container)
        getFilters()
        setListeners()
        return mDialogRoot
    }

    private fun getFilters() {
        mFilters = listOf(
            mDialogRoot.imgorder_date,
            mDialogRoot.imgorder_maxamount,
            mDialogRoot.imgorder_minamount,
            mDialogRoot.imgorder_category,
            mDialogRoot.imgorder_default
        )
        setSelection(mFilters[selected].id)
    }

    private fun setListeners() {
        for (filter in mFilters) {
            filter.setOnClickListener(this)
        }
        mDialogRoot.btn_filterresults.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_filterresults -> {
                listener.onItemClick(mFilterSelection)
                dismiss()
            }
            else -> {
                setSelection(v?.id)
            }
        }

    }

    private fun setSelection(id: Int?) {
        for (i in 0 until mFilters.count()) {
            if (id == mFilters[i].id) {
                mFilters[i].setBackgroundResource(R.drawable.shape_orderby_selected)
                context?.resources?.getColor(R.color.red)?.let { mFilters[i].setColorFilter(it) }
                mFilterSelection = i
            } else {
                mFilters[i].setBackgroundResource(R.drawable.shape_orderby)
                context?.resources?.getColor(R.color.black)?.let { mFilters[i].setColorFilter(it) }
            }
        }
    }
}