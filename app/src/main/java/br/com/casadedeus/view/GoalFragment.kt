package br.com.casadedeus.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.casadedeus.R
import br.com.casadedeus.beans.GoalModel
import br.com.casadedeus.service.constants.ViewConstants
import br.com.casadedeus.view.adapter.GoalAdapter
import br.com.casadedeus.viewmodel.GoalViewModel
import kotlinx.android.synthetic.main.fragment_goal.view.*


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
        mViewModel.load()

        return mViewRoot
    }

    override fun onResume() {
        super.onResume()
        mViewModel.load()
    }

    private fun setupRecycler() {
        val linearLayoutManager = LinearLayoutManager(context)
        mViewRoot.rv_planning.layoutManager = linearLayoutManager
        //mAdapter.attachListener()
        mViewRoot.rv_planning.adapter = mAdapter
    }

    private fun setListeners() {
        mViewRoot.add_goal.setOnClickListener(this)
    }

    private fun observer() {
        mViewModel.goallist.observe(viewLifecycleOwner, Observer {
            mAdapter.notifyChanged(it)
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
        val intent = Intent(context, TransactionFormActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable(ViewConstants.KEYS.GOAL, goal)
        intent.putExtras(bundle)
        startActivity(intent)
        activity?.overridePendingTransition(
            R.anim.slide_in_up,
            R.anim.slide_out_up
        )
    }

}