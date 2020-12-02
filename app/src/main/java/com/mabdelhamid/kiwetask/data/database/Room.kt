package com.mabdelhamid.kiwetask.data.database

import android.content.Context
import androidx.room.*
import com.mabdelhamid.kiwetask.data.models.User
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UsersDao {
    @Query("select * from users_table WHERE email = :email and password = :password")
    fun getUserByEmailAndPassword(email: String, password: String): Single<User>

    @Query("select * from users_table WHERE email = :email")
    fun getUserByEmail(email: String): Single<User>

    @Insert()
    fun insertUser(user: User): Completable
}

@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}

private lateinit var INSTANCE: UsersDatabase

fun getDatabase(context: Context): UsersDatabase {
    synchronized(UsersDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                UsersDatabase::class.java,
                "users"
            ).build()
        }
    }
    return INSTANCE
}
