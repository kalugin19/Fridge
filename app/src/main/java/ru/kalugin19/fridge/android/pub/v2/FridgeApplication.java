package ru.kalugin19.fridge.android.pub.v2;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import ru.kalugin19.fridge.android.pub.v2.injection.component.ApplicationComponent;
import ru.kalugin19.fridge.android.pub.v2.injection.component.DaggerApplicationComponent;
import ru.kalugin19.fridge.android.pub.v2.injection.module.ApplicationModule;

/**
 * Application class
 *
 * @author Furmanov Sergey
 */
public class FridgeApplication extends Application {

    private ApplicationComponent mApplicationComponent;

    public static FridgeApplication get(Context context) {
        return (FridgeApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
