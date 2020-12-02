package com.mabdelhamid.kiwetask.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    val firstName: String,
    val lastName: String,
    val age: Int,
    @PrimaryKey val email: String,
    val password: String
)