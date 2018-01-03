package ru.kalugin19.fridge.android.pub.v2.ui.base.view.activity;

import android.support.v7.app.AppCompatActivity;

import ru.kalugin19.fridge.android.pub.v2.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v2.injection.component.ActivityComponent;
import ru.kalugin19.fridge.android.pub.v2.injection.component.DaggerActivityComponent;
import ru.kalugin19.fridge.android.pub.v2.injection.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    protected ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            //noinspection deprecation
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(FridgeApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }
}
