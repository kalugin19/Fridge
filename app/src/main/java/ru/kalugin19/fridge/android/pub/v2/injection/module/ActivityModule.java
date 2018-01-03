package ru.kalugin19.fridge.android.pub.v2.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import ru.kalugin19.fridge.android.pub.v2.data.model.ProductModel;
import ru.kalugin19.fridge.android.pub.v2.data.remote.FirebaseService;
import ru.kalugin19.fridge.android.pub.v2.injection.qualifiers.ActivityContext;
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.presenter.AddEditProductPresenter;
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.presenter.IAddEditProductPresenter;


/**
 * Module : Activity
 *
 * @author Natochij Alexander
 */
@Module
public class ActivityModule {

    final private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

    @Provides
    IAddEditProductPresenter iAddEditProductPresenter(FirebaseService firebaseService, ProductModel productModel) {
        return new AddEditProductPresenter(firebaseService, productModel);
    }
}
