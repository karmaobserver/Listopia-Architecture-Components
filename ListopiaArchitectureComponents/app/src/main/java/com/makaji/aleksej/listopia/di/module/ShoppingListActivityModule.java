package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Module
public abstract class ShoppingListActivityModule {
    @ContributesAndroidInjector
    abstract ShoppingListActivity contributeShoppingListActivity();
}
