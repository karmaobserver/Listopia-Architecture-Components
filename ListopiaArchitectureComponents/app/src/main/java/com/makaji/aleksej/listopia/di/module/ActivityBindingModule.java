package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.product.ProductActivity;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 1/14/2018.
 */


@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = ShoppingListModule.class)
    abstract ShoppingListActivity contributeShoppingListActivity();

    @ContributesAndroidInjector(modules = ProductModule.class)
    abstract ProductActivity contributeProductActivity();
}
