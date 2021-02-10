package br.com.casadedeus.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewRoot: View
    private lateinit var mViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mViewRoot = inflater.inflate(R.layout.fragment_profile, container, false)
        mViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        // Criar observadores
        observer()

        setListeners()

        mViewModel.getCurrentUser()

        return mViewRoot
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getCurrentUser()
    }

    private fun setListeners() {
        mViewRoot.img_logout.setOnClickListener(this)
    }

    private fun observer() {
        mViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if (it.profilePhotoUrl != null) {
                context?.let { it1 ->
                    Glide.with(it1).load(it.profilePhotoUrl).into(mViewRoot.img_account)
                }
            }
            mViewRoot.txt_name.text = it.name
            mViewRoot.txt_email.text = it.email
        })
        mViewModel.validation.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_logout -> {
                exit()
            }
        }
    }

    private fun exit() {
        AlertDialog.Builder(context)
            .setTitle(R.string.title_logout)
            .setMessage(R.string.desc_logout)
            .setPositiveButton(R.string.yes) { dialog, which ->
                mViewModel.doLogout()
            }
            .setNeutralButton(R.string.cancel, null)
            .show()
    }


}