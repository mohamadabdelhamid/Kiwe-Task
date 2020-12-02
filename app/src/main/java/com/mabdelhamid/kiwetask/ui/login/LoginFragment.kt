package com.mabdelhamid.kiwetask.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.mabdelhamid.kiwetask.utils.Constants
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.databinding.FragmentLoginBinding
import com.mabdelhamid.kiwetask.ui.activities.MainActivity
import com.mabdelhamid.kiwetask.ui.registration.RegistrationFragment
import com.mabdelhamid.kiwetask.utils.*
import com.mabdelhamid.kiwetask.utils.Constants.KEY_EMAIL
import com.mabdelhamid.kiwetask.utils.Constants.KEY_IS_LOGGED_IN

interface LoginView {
    fun getAppContext(): Context
    fun onSuccess(email: String)
    fun onError()
}

class LoginFragment : Fragment(), LoginView {

    companion object {
        const val INPUT_EMAIL = "tilEmail"
        const val INPUT_PASSWORD = "tilPassword"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: LoginPresenter

    private var focusFlag = ""
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            when (focusFlag) {
                INPUT_EMAIL -> {
                    binding.tilEmail.focusOff()
                    focusFlag = ""
                }
                INPUT_PASSWORD -> {
                    binding.tilPassword.focusOff()
                    focusFlag = ""
                }
            }
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        createPresenter()
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

    override fun onSuccess(email: String) {
        val preferences =
            requireActivity().getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().apply {
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onError() =
        Toast.makeText(requireContext(), getString(R.string.check_credentials), Toast.LENGTH_SHORT)
            .show()

    private fun setListeners() {
        with(binding) {
            etEmail.addTextChangedListener(watcher)
            etPassword.addTextChangedListener(watcher)
            etPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateInputs()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            btnLogin.setOnClickListener { validateInputs() }
            tvRegister.setOnClickListener {
                TransactionUtils.replaceFragment(
                    requireActivity().supportFragmentManager,
                    R.id.containerAuth,
                    RegistrationFragment()
                )
            }
        }
    }

    private fun createPresenter() {
        presenter = LoginPresenter(this)
    }

    private fun validateInputs() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if (email.isBlank()) {
            binding.tilEmail.focusOn(getString(R.string.validation_field_required))
            focusFlag = INPUT_EMAIL
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.focusOn(getString(R.string.validation_valid_email))
            focusFlag = INPUT_EMAIL
        } else if (password.isBlank()) {
            binding.tilPassword.focusOn(getString(R.string.validation_field_required))
            focusFlag = INPUT_PASSWORD
        } else if (!Utils.isValidPassword(password)) {
            binding.tilPassword.focusOn(getString(R.string.validation_valid_password))
            focusFlag = INPUT_PASSWORD
        } else {
            KeyboardUtils.hide(requireActivity())
            presenter.login(email, password)
        }
    }
}