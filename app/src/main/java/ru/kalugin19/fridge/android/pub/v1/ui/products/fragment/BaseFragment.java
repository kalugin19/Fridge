package ru.kalugin19.fridge.android.pub.v1.ui.products.fragment;

import android.support.v4.app.Fragment;

import ru.kalugin19.fridge.android.pub.v1.FridgeApplication;
import ru.kalugin19.fridge.android.pub.v1.injection.component.DaggerFragmentComponent;
import ru.kalugin19.fridge.android.pub.v1.injection.component.FragmentComponent;
import ru.kalugin19.fridge.android.pub.v1.injection.module.FragmentModule;


/**
 * Fragment : Base
 *
 * @author Kalugin Valerij
 */
public class BaseFragment extends Fragment{
    private FragmentComponent fragmentComponent;

    @SuppressWarnings("WeakerAccess")
    protected FragmentComponent getFragmentComponent() {
        if (fragmentComponent == null) {
            //noinspection deprecation
            fragmentComponent = DaggerFragmentComponent.builder()
                    .fragmentModule(new FragmentModule(this))
                    .applicationComponent(FridgeApplication.get(this.getActivity()).getComponent())
                    .build();
        }
        return fragmentComponent;
    }
}
