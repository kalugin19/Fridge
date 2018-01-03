package ru.kalugin19.fridge.android.pub.v2.injection.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.kalugin19.fridge.android.pub.v2.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v2.data.remote.ConnectionService;
import ru.kalugin19.fridge.android.pub.v2.injection.qualifiers.ApplicationContext;


/**
 * Module : Application
 *
 * @author Furmanov Sergey
 */
@Module
public class ApplicationModule {
    @SuppressWarnings("WeakerAccess")
    protected final FridgeApplication mApplication;

    public ApplicationModule(FridgeApplication application) {
        mApplication = application;
    }

    @Provides
    FridgeApplication provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    ConnectionService provideConnectionService() {
        return new ConnectionService(mApplication);
    }
}
