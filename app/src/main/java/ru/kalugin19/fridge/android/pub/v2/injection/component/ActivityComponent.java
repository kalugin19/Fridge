package ru.kalugin19.fridge.android.pub.v2.injection.component;

import dagger.Component;
import ru.kalugin19.fridge.android.pub.v2.injection.module.ActivityModule;
import ru.kalugin19.fridge.android.pub.v2.injection.scope.PerActivity;
import ru.kalugin19.fridge.android.pub.v2.ui.add_edit_product.view.activity.AddEditProductActivity;
import ru.kalugin19.fridge.android.pub.v2.ui.auth.AuthorizationActivity;
import ru.kalugin19.fridge.android.pub.v2.ui.products.activity.ProductsActivity;


/**
 * Component : Activity
 *
 * @author Furmanov Sergey
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(AuthorizationActivity authorizationActivity);
    void inject(ProductsActivity productsActivity);
    void inject(AddEditProductActivity addEditProductActivity);
}
