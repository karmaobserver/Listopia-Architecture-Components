package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.shoppinglist.MyFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListAddFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 12/18/2017.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract MyFragment contributeMyFragment();

    @ContributesAndroidInjector
    abstract ShoppingListAddFragment shoppingListAddFragment();

    @ContributesAndroidInjector
    abstract ShoppingListFragment shoppingListFragment();

}
