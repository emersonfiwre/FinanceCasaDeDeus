package br.com.casadedeus.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.casadedeus.R
import br.com.casadedeus.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

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
        mViewRoot.btn_logout.setOnClickListener(this)
        mViewRoot.img_edit_name.setOnClickListener(this)
        mViewRoot.img_edit_email.setOnClickListener(this)
        mViewRoot.switch_protection.setOnCheckedChangeListener(this)
    }

    private fun observer() {
        mViewModel.currentUser.observe(viewLifecycleOwner, Observer {
            if (it.profilePhotoUrl != null) {
                context?.let { it1 ->
                    Glide.with(it1).load(it.profilePhotoUrl).into(mViewRoot.img_account)
                }
            }
            mViewRoot.edit_name_profile.setText(it.name)
            mViewRoot.edit_email_profile.setText(it.email)
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
        mViewModel.resetName.observe(viewLifecycleOwner, Observer {
            if (!it.success()) {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
            resetingName(true)
        })
        mViewModel.changeEmail.observe(viewLifecycleOwner, Observer {
            if (it.success()) {
                Toast.makeText(context, "Email alterado com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.failure(), Toast.LENGTH_SHORT).show()
            }
            updateEmail(true)
        })
        mViewModel.protection.observe(viewLifecycleOwner, Observer {
            mViewRoot.switch_protection.isChecked = it
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_logout -> {
                exit()
            }
            R.id.img_edit_name -> {
                mViewRoot.edit_name_profile.isEnabled = true
                mViewRoot.edit_name_profile.setSelection(edit_name_profile.text.length)
                mViewRoot.edit_name_profile.requestFocus()
                showKeyboard()

                mViewRoot.edit_name_profile.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        resetingName()
                        mViewModel.resetName(mViewRoot.edit_name_profile.text.toString())
                        showKeyboard(true)
                        //Toast.makeText(context, "Click done", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                })
            }
            R.id.img_edit_email -> {
                mViewRoot.edit_email_profile.isEnabled = true
                mViewRoot.edit_email_profile.setSelection(edit_email_profile.text.length)
                mViewRoot.edit_email_profile.requestFocus()
                showKeyboard()

                mViewRoot.edit_email_profile.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        updateEmail()
                        mViewModel.updateEmail(mViewRoot.edit_email_profile.text.toString())
                        showKeyboard(true)
                        true
                    } else false
                })
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        when (p0?.id) {
            R.id.switch_protection -> {
                mViewModel.setProtection(switch_protection.isChecked)
                //Toast.makeText(context,"Switch: ${if (switch_protection.isChecked) "true" else "false"}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showKeyboard(hide: Boolean = false) {
        val imgr =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (hide) imgr.hideSoftInputFromWindow(view?.windowToken, 0)
        else {
            imgr.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }

    }

    private fun resetingName(finish: Boolean = false) {
        if (finish) {
            mViewRoot.edit_name_profile.visibility = View.VISIBLE
            mViewRoot.img_edit_name.visibility = View.VISIBLE
            mViewRoot.edit_name_profile.isEnabled = false
            mViewRoot.pg_reset_name.visibility = View.GONE
        } else {
            mViewRoot.edit_name_profile.visibility = View.GONE
            mViewRoot.img_edit_name.visibility = View.GONE
            mViewRoot.pg_reset_name.visibility = View.VISIBLE
        }
    }

    private fun updateEmail(finish: Boolean = false) {
        if (finish) {
            mViewRoot.edit_email_profile.visibility = View.VISIBLE
            mViewRoot.img_edit_email.visibility = View.VISIBLE
            mViewRoot.edit_email_profile.isEnabled = false
            mViewRoot.pg_update_email.visibility = View.GONE
        } else {
            mViewRoot.edit_email_profile.visibility = View.GONE
            mViewRoot.img_edit_email.visibility = View.GONE
            mViewRoot.pg_update_email.visibility = View.VISIBLE
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