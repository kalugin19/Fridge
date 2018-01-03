package ru.kalugin19.fridge.android.pub.v2.ui.common_fridge

import javax.inject.Inject


class CommonFridgePresenter @Inject constructor(): ICommonFridgeContract.ICommonFridgePresenter {

    var mView: ICommonFridgeContract.View? = null

    override fun attachView(view: ICommonFridgeContract.View) {
        mView = view
    }

    override fun detachView() {
        mView = null
    }

    override fun onStart() {
    }

    override fun onStop() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun getMembers() {
    }
}