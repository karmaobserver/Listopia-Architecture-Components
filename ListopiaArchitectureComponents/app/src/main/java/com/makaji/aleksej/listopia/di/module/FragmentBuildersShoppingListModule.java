package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.product.ProductFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListAddFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListRenameFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 12/18/2017.
 */

@Module
public abstract class FragmentBuildersShoppingListModule {

    @ContributesAndroidInjector
    abstract ShoppingListAddFragment shoppingListAddFragment();

    @ContributesAndroidInjector
    abstract ShoppingListFragment shoppingListFragment();

    @ContributesAndroidInjector
    abstract ShoppingListRenameFragment shoppingListRenameFragment();

}
