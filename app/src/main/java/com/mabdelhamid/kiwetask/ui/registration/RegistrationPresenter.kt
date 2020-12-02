package com.mabdelhamid.kiwetask.ui.registration

import com.mabdelhamid.kiwetask.data.database.getDatabase
import com.mabdelhamid.kiwetask.data.models.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RegistrationPresenter(private val view: RegistrationView) {
    private val compositeDisposable = CompositeDisposable()
    private val database = getDatabase(view.getAppContext())

    fun checkUser(user: User) {
        database.usersDao.getUserByEmail(user.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onUserExisted()
            }, {
                register(user)
            })
            .let { compositeDisposable.add(it) }
    }

    private fun register(user: User) {
        database.usersDao.insertUser(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view.onSuccess(user.email)
            }
            .let { compositeDisposable.add(it) }
    }

    fun clear() = compositeDisposable.clear()
}