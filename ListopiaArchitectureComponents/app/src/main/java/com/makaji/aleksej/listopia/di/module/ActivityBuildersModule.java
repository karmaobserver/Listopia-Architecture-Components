package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.product.ProductActivity;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 1/14/2018.
 */


@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = FragmentBuildersShoppingListModule.class)
    abstract ShoppingListActivity contributeShoppingListActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersProductModule.class)
    abstract ProductActivity contributeProductActivity();
}
