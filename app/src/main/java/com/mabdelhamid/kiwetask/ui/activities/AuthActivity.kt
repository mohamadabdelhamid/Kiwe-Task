package com.mabdelhamid.kiwetask.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.databinding.ActivityAuthBinding
import com.mabdelhamid.kiwetask.ui.login.LoginFragment
import com.mabdelhamid.kiwetask.utils.Constants
import com.mabdelhamid.kiwetask.utils.Constants.KEY_IS_LOGGED_IN
import com.mabdelhamid.kiwetask.utils.TransactionUtils

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
        val isLoggedIn = preferences.getBoolean(KEY_IS_LOGGED_IN, false)
        if (isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            TransactionUtils.noStackFragment(
                supportFragmentManager,
                R.id.containerAuth,
                LoginFragment()
            )
        }
    }
}