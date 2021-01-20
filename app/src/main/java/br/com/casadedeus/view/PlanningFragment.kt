package br.com.casadedeus.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.TransactionModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.view.adapter.TransactionAdapter
import br.com.casadedeus.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.fragment_planning.view.*


class PlanningFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewRoot: View
    private lateinit var mViewModel: TransactionViewModel
    private val mAdapter: TransactionAdapter = TransactionAdapter()


    /*Design
    * radio button https://dribbble.com/shots/9890260-Card-Theme-Switch-Light-Theme
    * btnSave top rigth https://stackoverflow.com/questions/58651661/how-to-set-max-height-in-bottomsheetdialogfragment
    * ?android:textColorHint*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewRoot = inflater.inflate(R.layout.fragment_planning, container, false)
        mViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        //************
        setupRecycler()

        // Criar observadores
        observer()

        setListeners()

        //for crate home button
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(mViewRoot.toolbar)
        //activity?.supportActionBar?.title = "My Title"
        //activity?.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_add)// set drawable icon
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return mViewRoot
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        mViewRoot.rv_planning.layoutManager = linearLayoutManager
        //mAdapter.attachListener()
        mViewRoot.rv_planning.adapter = mAdapter
    }

    private fun setListeners() {

    }

    private fun observer() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
        }
    }

    private fun showPlanning(transaction: TransactionModel? = null) {
        val intent = Intent(context, TransactionFormActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(ViewConstants.KEYS.TRANSACTION, transaction)
        intent.putExtras(bundle)
        startActivity(intent)
        activity?.overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        print("call frag")
        Toast.makeText(context, "Clickme", Toast.LENGTH_SHORT).show()
        return when (item.itemId) {
            R.id.nav_add_planning -> {
                Toast.makeText(context, "Clickme", Toast.LENGTH_SHORT).show()
                true
            }

            else -> {
                Toast.makeText(context, "clickNo", Toast.LENGTH_SHORT).show()
                return super.onOptionsItemSelected(item)
            }
        }
    }


}