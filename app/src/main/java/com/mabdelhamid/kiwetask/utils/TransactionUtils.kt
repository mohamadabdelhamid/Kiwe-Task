package com.mabdelhamid.kiwetask.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mabdelhamid.kiwetask.R

object TransactionUtils {
    fun noStackFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            replace(containerId, fragment)
            commit()
        }
    }

    fun replaceFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            replace(containerId, fragment)
            addToBackStack(null)
            commit()
        }
    }

    fun addFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            add(containerId, fragment)
            addToBackStack(null)
            commit()
        }
    }
}