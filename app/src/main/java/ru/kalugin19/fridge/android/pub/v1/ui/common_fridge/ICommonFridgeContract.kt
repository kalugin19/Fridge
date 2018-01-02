package ru.kalugin19.fridge.android.pub.v1.ui.common_fridge

import ru.kalugin19.fridge.android.pub.v1.ui.base.Presenter
import ru.kalugin19.fridge.android.pub.v1.ui.base.ViewContract

interface ICommonFridgeContract {
    interface View :ViewContract {

    }

    interface ICommonFridgePresenter: Presenter<View> {
        fun getMembers()
    }
}