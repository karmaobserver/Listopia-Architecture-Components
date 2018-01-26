package com.makaji.aleksej.listopia.di.module;

import android.support.v4.app.Fragment;

import com.makaji.aleksej.listopia.ui.login.LoginFragment;
import com.makaji.aleksej.listopia.ui.product.ProductAddFragment;
import com.makaji.aleksej.listopia.ui.product.ProductFragment;
import com.makaji.aleksej.listopia.ui.product.ProductRenameFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by Aleksej on 1/25/2018.
 */

/*@Module(subcomponents = LoginFragmentModule.LoginFragmentSubcomponent.class)
abstract class LoginFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindLoginFragmentInjectorFactory(LoginFragmentSubcomponent.Builder builder);

    @Subcomponent(modules = ProductAdapterModule.class)
    public interface LoginFragmentSubcomponent extends AndroidInjector<LoginFragment> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<LoginFragment> {}
    }
}*/


