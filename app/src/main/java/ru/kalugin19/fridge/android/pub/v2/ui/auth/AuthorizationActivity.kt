package ru.kalugin19.fridge.android.pub.v2.ui.auth

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import com.crashlytics.android.Crashlytics

import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_authorization.*
import ru.kalugin19.fridge.android.pub.v2.R
import ru.kalugin19.fridge.android.pub.v2.data.remote.FirebaseService
import ru.kalugin19.fridge.android.pub.v2.ui.auth.presenter.AuthorizationPresenter
import ru.kalugin19.fridge.android.pub.v2.ui.base.util.ProgressDialogCustom
import ru.kalugin19.fridge.android.pub.v2.ui.base.view.activity.BaseActivity
import ru.kalugin19.fridge.android.pub.v2.ui.products.activity.ProductsActivity


/**
 * Authorization Screen
 */
class AuthorizationActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener, IAuthorizationView {

    var firebaseService: FirebaseService? = null
        @Inject set

    var authorizationPresenter: AuthorizationPresenter? = null
        @Inject set

    private lateinit var progressDialogAuthorization: ProgressDialogCustom
    private var googleApiClient: GoogleApiClient? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)
        authorizationPresenter?.attachView(this)
        progressDialogAuthorization = ProgressDialogCustom(this, getString(R.string.screen_auth_dialog_authorization))
        signInBtn.setOnClickListener {
            signIn()
        }
    }


    private fun signIn() {
        progressDialogAuthorization.showProgress(true)
        if (googleApiClient == null) {
            googleApiClient = buildApiClient()
        }
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        this.startActivityForResult(signInIntent, SignIn.RC_SIGN_IN)
    }

    override fun onResume() {
        super.onResume()
        authorizationPresenter?.onResume()
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Crashlytics.log(33, "onConnectionFailed", connectionResult.errorMessage)
        showAuthorizationError()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SignIn.RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (!result.isSuccess) {
                Crashlytics.log(444, "activityResult", result.toString())
                showAuthorizationError()
            } else {
                result?.signInAccount?.let { authorizationPresenter?.authorization(it) }
            }
            progressDialogAuthorization.showProgress(false)
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseService?.getmAuth()?.currentUser?.getIdToken(false)?.apply {
            progressDialogAuthorization.showProgress(true)
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMainActivity()
                }
                progressDialogAuthorization.showProgress(false)
            }.addOnFailureListener { _ ->
                progressDialogAuthorization.showProgress(false)
            }
        }
    }


    /**
     * Configure Google Sign In
     */
    private fun buildApiClient(): GoogleApiClient {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.resources.getString(R.string.server_client_id))
                .requestEmail()
                .build()
        return GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }


    override fun goToMainActivity() {
        val intent = Intent(this, ProductsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun showAuthorizationError() {
        Snackbar.make(globalView, R.string.screen_auth_authorization_error, Snackbar.LENGTH_LONG).show()
    }

    override fun startAuthorizationProgressDialog() {
        progressDialogAuthorization.showProgress(true)
    }

    override fun stopAuthorizationProgressDialog() {
        progressDialogAuthorization.showProgress(false)
    }

    object SignIn {
        internal val RC_SIGN_IN = 100
    }
}
