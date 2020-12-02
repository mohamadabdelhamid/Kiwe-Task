package com.mabdelhamid.kiwetask.utils

import java.util.regex.Pattern

object Utils {
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-zA-Z])" +
                    "(?=.*[@#$%^&+=])" +
                    ".{8,}" +
                    "$"
        );
        return passwordPattern.matcher(password).matches()
    }
}