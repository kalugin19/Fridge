package ru.kalugin19.fridge.android.pub.v2.injection.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.kalugin19.fridge.android.pub.v2.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v2.data.remote.ConnectionService;
import ru.kalugin19.fridge.android.pub.v2.injection.module.ApplicationModule;
import ru.kalugin19.fridge.android.pub.v2.injection.qualifiers.ApplicationContext;


/**
 * Component : Application
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @SuppressWarnings("unused")
    @ApplicationContext
    Context context();

    @SuppressWarnings("unused")
    FridgeApplication application();

    ConnectionService connectionService();

}
