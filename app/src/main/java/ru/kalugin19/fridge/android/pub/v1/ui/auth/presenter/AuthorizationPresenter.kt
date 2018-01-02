package ru.kalugin19.fridge.android.pub.v1.ui.auth.presenter

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

import java.lang.ref.WeakReference

import javax.inject.Inject

import ru.kalugin19.fridge.android.pub.v1.data.model.AuthorizationModel
import ru.kalugin19.fridge.android.pub.v1.ui.auth.IAuthorizationView


/**
 * реализация презентера для экрана авторизации
 */
class AuthorizationPresenter @Inject constructor(val authorizationModel: AuthorizationModel) : IAuthorizationPresenter {

    private var authorizationView: WeakReference<IAuthorizationView>? = null


    override fun attachView(view: IAuthorizationView) {
        authorizationView = WeakReference(view)
    }

    override fun detachView() {
        authorizationView = null
    }

    override fun onStart() {

    }

    override fun onStop() {

    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun authorization(acct: GoogleSignInAccount) {
        authorizationModel.authorizationWithGoogle(acct).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Boolean>() {
                    override fun onComplete() {
                        this.dispose()
                    }

                    override fun onError(e: Throwable) {
                        authorizationView?.get()?.showAuthorizationError()
                    }

                    override fun onNext(aBoolean: Boolean) {
                        if (aBoolean) {
                            authorizationView?.get()?.goToMainActivity()
                        } else {
                            authorizationView?.get()?.showAuthorizationError()
                        }
                    }
                })
    }
}
