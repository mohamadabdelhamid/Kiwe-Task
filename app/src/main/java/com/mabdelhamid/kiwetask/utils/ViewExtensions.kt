package com.mabdelhamid.kiwetask.utils

import android.view.View
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.focusOn(error: String) {
    this.error = error
    editText!!.requestFocus()
}

fun TextInputLayout.focusOff() {
    error = ""
    isErrorEnabled = false
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}
