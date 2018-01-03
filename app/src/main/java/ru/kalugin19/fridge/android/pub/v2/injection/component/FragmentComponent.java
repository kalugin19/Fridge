package ru.kalugin19.fridge.android.pub.v2.injection.component;

import dagger.Component;
import ru.kalugin19.fridge.android.pub.v2.injection.module.FragmentModule;
import ru.kalugin19.fridge.android.pub.v2.injection.scope.PerFragment;
import ru.kalugin19.fridge.android.pub.v2.ui.products.fragment.ProductsFragment;


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
