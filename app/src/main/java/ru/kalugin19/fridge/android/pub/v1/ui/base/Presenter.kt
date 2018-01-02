package ru.kalugin19.fridge.android.pub.v1.ui.base


/**
 * Main Presenter contract
 */
interface Presenter<in V : ViewContract> {

    fun attachView(view: V)

    fun detachView()

    fun onStart()

    fun onStop()

    fun onResume()

    fun onPause()
}
