package ru.kalugin19.fridge.android.pub.v1.injection.component;

import dagger.Component;
import ru.kalugin19.fridge.android.pub.v1.injection.module.FragmentModule;
import ru.kalugin19.fridge.android.pub.v1.injection.scope.PerFragment;
import ru.kalugin19.fridge.android.pub.v1.ui.products.fragment.ProductsFragment;


/**
 * Component : Fragment
 *
 * @author Kalugin Valerij
 */
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(ProductsFragment productsFragment);
}
