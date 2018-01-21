package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.product.ProductAddFragment;
import com.makaji.aleksej.listopia.ui.product.ProductFragment;
import com.makaji.aleksej.listopia.ui.product.ProductRenameFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 1/14/2018.
 */

@Module
public abstract class FragmentBuildersProductModule {

    @ContributesAndroidInjector
    abstract ProductFragment productFragment();

    @ContributesAndroidInjector
    abstract ProductAddFragment productAddFragment();

    @ContributesAndroidInjector
    abstract ProductRenameFragment productRenameFragment();

}
