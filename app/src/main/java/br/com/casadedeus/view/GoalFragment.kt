package br.com.casadedeus.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.service.listener.GoalDialogListener
import br.com.casadedeus.service.listener.GoalListener
import br.com.casadedeus.service.utils.Utils
import br.com.casadedeus.view.adapter.GoalAdapter
import br.com.casadedeus.viewmodel.GoalViewModel
import kotlinx.android.synthetic.main.dialog_goal_form.view.*
import kotlinx.android.synthetic.main.fragment_goal.view.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import kotlinx.android.synthetic.main.fragment_transaction.view.*


class GoalFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewRoot: View
    private lateinit var mViewModel: GoalViewModel
    private val mAdapter: GoalAdapter = GoalAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewRoot = inflater.inflate(R.layout.fragment_goal, container, false)
        mViewModel = ViewModelProvider(this).get(GoalViewModel::class.java)

        //************
        setupRecycler()

        // Criar observadores
        observer()

        setListeners()


        //for crate home button
        //val activity = activity as AppCompatActivity?
        //activity?.setSupportActionBar(mViewRoot.toolbar)
        //activity?.supportActionBar?.title = "My Title"
        //activity?.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_add)// set drawable icon
        //activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //mViewRoot.toolbar.inflateMenu(R.menu.menu_add_planning)
        load()

        return mViewRoot
    }

    override fun onResume() {
        super.onResume()
        load()
    }

    private fun load(){
        mViewRoot.rv_planning.visibility = View.GONE
        mViewRoot.pg_await_load_goal.visibility = View.VISIBLE
        mViewModel.load()
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        mViewRoot.rv_planning.layoutManager = linearLayoutManager
        mAdapter.attachListener(object : GoalListener {
            override fun onItemClick(item: GoalModel) {
                showGoal(item)
            }

            override fun onDeleteClick(id: String) {
                mViewModel.delete(id)
            }

            override fun onCompleteClick(id: String) {
                mViewModel.complete(id)
            }

            override fun onUndoClick(id: String) {
                mViewModel.undo(id)
            }

        })
        mViewRoot.rv_planning.adapter = mAdapter
    }

    private fun setListeners() {
        mViewRoot.add_goal.setOnClickListener(this)
    }

    private fun observer() {
        mViewModel.goallist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
            mViewRoot.rv_planning.visibility = View.VISIBLE
            mViewRoot.pg_await_load_goal.visibility = View.GONE
        })
        mViewModel.delete.observe(viewLifecycleOwner, Observer {
            if (!it.success()) {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
            load()
        })
        mViewModel.validation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.success()) {
                Toast.makeText(
                    context,
                    getString(R.string.success_save_goal),
                    Toast.LENGTH_SHORT
                ).show()
                //dismiss()
                //activity?.supportFragmentManager?.popBackStackImmediate()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
            load()
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_goal -> {
                val picker = GoalDialog.newInstance()
                activity?.supportFragmentManager?.let {
                    picker.show(
                        it,
                        ViewConstants.TAGS.GOAL_DIALOG
                    )
                }
            }
        }
    }

    private fun showGoal(goal: GoalModel? = null) {
        val dialog = GoalDialog.newInstance(goal)
        dialog.attachListener(object : GoalDialogListener {
            override fun onSaveClick(goal: GoalModel) {
                mViewModel.save(goal)
            }

        })
        activity?.supportFragmentManager?.let {
            dialog.show(
                it,
                ViewConstants.KEYS.GOAL
            )
        }
    }


}