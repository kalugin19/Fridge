package ru.kalugin19.fridge.android.pub.v2.injection.module;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import ru.kalugin19.fridge.android.pub.v2.injection.qualifiers.ActivityContext;

/**
 * Module : Fragment
 *
 * @author Kalugin Valerij
 */
@Module
public class FragmentModule {

    final private Activity mActivity;
    final private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        mActivity = fragment.getActivity();
        this.fragment = fragment;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    Fragment provideFragment() {
        return fragment;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
