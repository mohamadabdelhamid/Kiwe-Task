package com.mabdelhamid.kiwetask.ui.profile

import com.mabdelhamid.kiwetask.data.database.getDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProfilePresenter(private val view: ProfileView) {

    private val compositeDisposable = CompositeDisposable()
    private val database = getDatabase(view.getAppContext())

    fun getUserDetails(email: String) {
        database.usersDao.getUserByEmail(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess(it)
            }, {
            })
            .let { compositeDisposable.add(it) }
    }

    fun clear() = compositeDisposable.clear()
}