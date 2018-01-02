package ru.kalugin19.fridge.android.pub.v1.data.model;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import ru.kalugin19.fridge.android.pub.v1.data.local.db.UserSharedPreferences;
import ru.kalugin19.fridge.android.pub.v1.data.remote.FirebaseService;


/**
 * Модель для авторизации
 *
 * @author Kalugin Valerij
 */
public class AuthorizationModel {

    private final FirebaseService firebaseService;
    private final UserSharedPreferences userSharedPreferences;

    @Inject
    AuthorizationModel(FirebaseService firebaseService, UserSharedPreferences userSharedPreferences) {
        this.firebaseService = firebaseService;
        this.userSharedPreferences = userSharedPreferences;
    }


    public Observable<Boolean> authorizationWithGoogle(final GoogleSignInAccount acct) {
       return Observable.create((ObservableOnSubscribe<Boolean>) emitter -> {
           final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
           firebaseService.getmAuth().signInWithCredential(credential)
                   .addOnCompleteListener(task -> {
                       if (!task.isSuccessful()) {
                           emitter.onNext(false);
                       } else {
                           userSharedPreferences.setEmail(acct.getEmail());
                           if (acct.getPhotoUrl()!=null){
                               userSharedPreferences.setPhotoUrl(acct.getPhotoUrl().toString());
                           }
                           emitter.onNext(true);
                       }
                   }).addOnFailureListener(emitter::onError);
       }).subscribeOn(Schedulers.io());
    }
}
