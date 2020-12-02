package com.mabdelhamid.kiwetask.ui.login

import com.mabdelhamid.kiwetask.data.database.getDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: LoginView) {

    private val compositeDisposable = CompositeDisposable()
    private val database = getDatabase(view.getAppContext())

    fun login(email: String, password: String) {
        database.usersDao.getUserByEmailAndPassword(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                view.onSuccess(it.email)
            }, {
                view.onError()
            })
            .let { compositeDisposable.add(it) }
    }

    fun clear() = compositeDisposable.clear()
}