package com.mabdelhamid.kiwetask.ui.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mabdelhamid.kiwetask.utils.Constants
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.data.models.User
import com.mabdelhamid.kiwetask.databinding.FragmentRegistrationBinding
import com.mabdelhamid.kiwetask.ui.activities.MainActivity
import com.mabdelhamid.kiwetask.ui.login.LoginFragment
import com.mabdelhamid.kiwetask.utils.*
import com.mabdelhamid.kiwetask.utils.Constants.KEY_EMAIL
import com.mabdelhamid.kiwetask.utils.Constants.KEY_IS_LOGGED_IN

interface RegistrationView {
    fun getAppContext(): Context
    fun onUserExisted()
    fun onSuccess(email: String)
}

class RegistrationFragment : Fragment(), RegistrationView {

    companion object {
        const val INPUT_FIRST_NAME = "tilFirstName"
        const val INPUT_LAST_NAME = "tilLastName"
        const val INPUT_AGE = "tilAge"
        const val INPUT_EMAIL = "tilEmail"
        const val INPUT_PASSWORD = "tilPassword"
    }

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var presenter: RegistrationPresenter

    private var focusFlag = ""
    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            when (focusFlag) {
                INPUT_FIRST_NAME -> {
                    binding.tilFirstName.focusOff()
                    focusFlag = ""
                }
                INPUT_LAST_NAME -> {
                    binding.tilLastName.focusOff()
                    focusFlag = ""
                }
                INPUT_AGE -> {
                    binding.tilAge.focusOff()
                    focusFlag = ""
                }
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
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
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

    override fun onUserExisted() = Toast.makeText(requireContext(), getString(R.string.email_already_exists), Toast.LENGTH_SHORT).show()

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

    private fun setListeners() {
        with(binding) {
            etFirstName.addTextChangedListener(watcher)
            etLastName.addTextChangedListener(watcher)
            etAge.addTextChangedListener(watcher)
            etEmail.addTextChangedListener(watcher)
            etPassword.addTextChangedListener(watcher)
            etPassword.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateInputs()
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }
            btnRegister.setOnClickListener { validateInputs() }
            tvLogin.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun createPresenter() {
        presenter = RegistrationPresenter(this)
    }

    private fun validateInputs() {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val age = binding.etAge.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (firstName.isBlank()) {
            binding.tilFirstName.focusOn(getString(R.string.validation_field_required))
            focusFlag = INPUT_FIRST_NAME
        } else if (lastName.isBlank()) {
            binding.tilLastName.focusOn(getString(R.string.validation_field_required))
            focusFlag = INPUT_LAST_NAME
        } else if (age.isBlank()) {
            binding.tilAge.focusOn(getString(R.string.validation_field_required))
            focusFlag = INPUT_AGE
        } else if (age.toInt() < 18) {
            binding.tilAge.focusOn(getString(R.string.validation_valid_age))
            focusFlag = INPUT_AGE
        } else if (email.isBlank()) {
            binding.tilEmail.focusOn(getString(R.string.validation_field_required))
            focusFlag = LoginFragment.INPUT_EMAIL
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.focusOn(getString(R.string.validation_valid_email))
            focusFlag = LoginFragment.INPUT_EMAIL
        } else if (password.isBlank()) {
            binding.tilPassword.focusOn(getString(R.string.validation_field_required))
            focusFlag = LoginFragment.INPUT_PASSWORD
        } else if (!Utils.isValidPassword(password)) {
            binding.tilPassword.focusOn(getString(R.string.validation_valid_password))
            focusFlag = LoginFragment.INPUT_PASSWORD
        } else {
            KeyboardUtils.hide(requireActivity())
            presenter.checkUser(User(firstName, lastName, age.toInt(), email, password))
        }
    }
}