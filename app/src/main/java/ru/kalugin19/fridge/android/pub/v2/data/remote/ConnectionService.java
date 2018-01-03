package ru.kalugin19.fridge.android.pub.v2.data.remote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Connection Service
 *
 * @author Kalugin Valery
 */

@Singleton
public class ConnectionService {

    private final Context context;

    @Inject
    public ConnectionService(Context appContext) {
        this.context = appContext;
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkInternetConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
