package com.mabdelhamid.kiwetask.ui.home

import android.location.Location
import android.util.Log
import com.mabdelhamid.kiwetask.data.network.NetworkManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomePresenter(private val view: HomeView) {

    private val compositeDisposable = CompositeDisposable()

    fun getVenues(location: Location) {
        NetworkManager.invoke()
            .getVenues("${location.latitude},${location.longitude}")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { view.showLoading(false) }
            .doOnSubscribe { view.showLoading(true) }
            .subscribe({ view.onSuccess(it?.response?.venues) },
                {view.onError()})
            .let {
                compositeDisposable.add(it)
            }
    }

    fun clear() = compositeDisposable.clear()
}