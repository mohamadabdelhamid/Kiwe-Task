package com.mabdelhamid.kiwetask.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.utils.Constants.KEY_EMAIL
import com.mabdelhamid.kiwetask.utils.Constants.PREFERENCES
import com.mabdelhamid.kiwetask.data.models.User
import com.mabdelhamid.kiwetask.databinding.FragmentProfileBinding

interface ProfileView {
    fun getAppContext(): Context
    fun onSuccess(user: User)
}

class ProfileFragment : Fragment(), ProfileView {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppBar()
        createPresenter()
        getUserEmail()?.let { presenter.getUserDetails(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        presenter.clear()
    }

    override fun getAppContext(): Context = requireActivity().applicationContext

    override fun onSuccess(user: User) {
        with(binding) {
            etFirstName.setText(user.firstName)
            etLastName.setText(user.lastName)
            etAge.setText(user.age.toString())
            etEmail.setText(user.email)
        }
    }

    private fun setAppBar() {
        with(binding.appBar){
            ivBack.setOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            tvTitle.text = getString(R.string.profile)
        }
    }

    private fun createPresenter() {
        presenter = ProfilePresenter(this)
    }


    private fun getUserEmail(): String? =
        requireActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).getString(
            KEY_EMAIL, null
        )
}