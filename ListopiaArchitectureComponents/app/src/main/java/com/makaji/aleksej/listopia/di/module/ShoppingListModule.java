package com.makaji.aleksej.listopia.di.module;

import android.app.Activity;
import android.content.Context;

import com.makaji.aleksej.listopia.ui.login.LoginFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListAddFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListRenameFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 1/25/2018.
 */

@Module
public abstract class ShoppingListModule {

    @ContributesAndroidInjector
    abstract ShoppingListAddFragment contributeShoppingListAddFragment();

    @ContributesAndroidInjector
    abstract ShoppingListFragment contributeShoppingListFragment();

    @ContributesAndroidInjector
    abstract ShoppingListRenameFragment contributeShoppingListRenameFragment();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();

}
