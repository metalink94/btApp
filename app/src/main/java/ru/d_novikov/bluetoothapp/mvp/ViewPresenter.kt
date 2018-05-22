package ru.d_novikov.bluetoothapp.mvp

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.experimental.Job
import java.lang.ref.WeakReference

abstract class ViewPresenter<V : IView> {

    protected val disposables = CompositeDisposable()
    protected val jobs = CompositeJob()

    private var mViewRef: WeakReference<V>? = null

    fun setView(view: V) {
        mViewRef = WeakReference<V>(view)
    }

    protected fun getView(): V? {
        return if (mViewRef == null) null else mViewRef!!.get()
    }

    fun onAttachView() {

    }

    fun onDetachView() {
        disposables.clear()
        jobs.cancel()
        if (mViewRef != null) {
            mViewRef!!.clear()
        }
    }

    protected fun unsubscribeOnDestroy(subscription: Disposable) {
        disposables.add(subscription)
    }

    protected fun unsubscribeOnDestroy(job: Job) {
        jobs.add(job)
    }

    protected fun getCompositeDisposable(): CompositeDisposable {
        return disposables
    }

    class CompositeJob {

        private val jobs = mutableListOf<Job>()

        fun add(job: Job) {
            jobs.add(job)
        }

        fun cancel() {
            jobs.forEach { it.cancel() }
        }

        operator fun plusAssign(job: Job) {
            add(job)
        }
    }
}