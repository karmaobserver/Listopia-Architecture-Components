package com.makaji.aleksej.listopia.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.makaji.aleksej.listopia.ui.base.ListopiaViewModelFactory;
import com.makaji.aleksej.listopia.ui.product.ProductViewModel;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ShoppingListViewModel.class)
    abstract ViewModel bindShoppingListViewModel(ShoppingListViewModel shoppingListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProductViewModel.class)
    abstract ViewModel bindProductViewModel(ProductViewModel productViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ListopiaViewModelFactory factory);
}
