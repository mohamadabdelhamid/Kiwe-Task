package com.mabdelhamid.kiwetask.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.mabdelhamid.kiwetask.R
import com.mabdelhamid.kiwetask.databinding.ActivityMainBinding
import com.mabdelhamid.kiwetask.ui.home.HomeFragment
import com.mabdelhamid.kiwetask.ui.profile.ProfileFragment
import com.mabdelhamid.kiwetask.ui.TermsAndConditionsFragment
import com.mabdelhamid.kiwetask.utils.Constants.PREFERENCES
import com.mabdelhamid.kiwetask.utils.TransactionUtils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        replaceWith(HomeFragment())
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStackImmediate()
        else super.onBackPressed()
    }

    private fun setListeners() {
        with(binding) {
            drawerNav.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navHome -> replaceWith(HomeFragment())
                    R.id.navProfile -> navigateTo(ProfileFragment())
                    R.id.navTermsAndConditions -> navigateTo(TermsAndConditionsFragment())
                    R.id.navLogout -> logout()
                }
                closeDrawer()
                true
            }
        }
    }

    private fun logout() {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
            .clear().apply()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()

    }

    private fun replaceWith(target: Fragment) =
        TransactionUtils.noStackFragment(supportFragmentManager, R.id.containerMain, target)

    private fun navigateTo(target: Fragment) =
        TransactionUtils.addFragment(supportFragmentManager, R.id.containerMain, target)

    fun openDrawer() = binding.drawerLayout.openDrawer(GravityCompat.START)
    private fun closeDrawer() = binding.drawerLayout.closeDrawer(GravityCompat.START)
}