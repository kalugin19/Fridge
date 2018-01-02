package ru.kalugin19.fridge.android.pub.v1.injection.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.kalugin19.fridge.android.pub.v1.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v1.data.remote.ConnectionService;
import ru.kalugin19.fridge.android.pub.v1.injection.module.ApplicationModule;
import ru.kalugin19.fridge.android.pub.v1.injection.qualifiers.ApplicationContext;


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
