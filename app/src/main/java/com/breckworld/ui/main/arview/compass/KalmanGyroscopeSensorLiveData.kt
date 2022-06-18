package com.breckworld.ui.main.arview.compass

import android.content.Context
import androidx.lifecycle.LiveData
import com.kircherelectronics.fsensor.sensor.gyroscope.KalmanGyroscopeSensor
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class KalmanGyroscopeSensorLiveData(context: Context) : LiveData<FloatArray>() {

    private val sensor: KalmanGyroscopeSensor = KalmanGyroscopeSensor(context)
    private var compositeDisposable: CompositeDisposable? = null

    override fun onActive() {
        this.compositeDisposable = CompositeDisposable()
        this.sensor.publishSubject.subscribe(object : Observer<FloatArray> {
            override fun onSubscribe(d: Disposable) {
                compositeDisposable?.add(d)
            }

            override fun onNext(values: FloatArray) {
                value = values
            }

            override fun onError(e: Throwable) {}

            override fun onComplete() {}
        })
        this.sensor.onStart()
    }

    override fun onInactive() {
        this.compositeDisposable?.dispose()
        this.sensor.onStop()
    }
}
