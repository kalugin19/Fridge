package ru.kalugin19.fridge.android.pub.v2.ui.auth.presenter

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import ru.kalugin19.fridge.android.pub.v2.ui.auth.IAuthorizationView
import ru.kalugin19.fridge.android.pub.v2.ui.base.Presenter


/**
 * Презентер для экрана авторизации
 */
internal interface IAuthorizationPresenter : Presenter<IAuthorizationView> {
    /**
     * Авторизация
     */
    fun authorization(acct: GoogleSignInAccount)
}
