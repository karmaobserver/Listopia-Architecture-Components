package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.databinding.ActivityShoppingListBinding;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;
import android.databinding.DataBindingComponent;
import android.util.Log;


import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;


public class ShoppingListActivity extends BaseActivity implements HasSupportFragmentInjector{

    @Inject
    ShoppingListRepository shoppingListRepository;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ShoppingListViewModel shoppingListViewModel;


    DataBindingComponent dataBindingComponent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShoppingListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list);

        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);
        binding.setViewModel(shoppingListViewModel);

        /**
         * ShoppingListCallBack, with events onClick, onLongClick, onButtonClick
         */
        ShoppingListAdapter adapter = new ShoppingListAdapter(dataBindingComponent, shopping -> {
            Timber.d("KLiKNUOOOOOO" );
        }, shoppingLong -> {
            Timber.d("LONG          KLiKNUOOOOOO" );
        }, shoppingButton -> {
            Timber.d("Button Clicked" );
        });

        shoppingListViewModel.getShoppingLists().observe(this, shoppingList -> {
            Timber.d("Ceo objekat " + shoppingList);
            if (shoppingList.data == null) {
                Timber.d("It's NULL");
            }  else {
                Timber.d("List Size: " + shoppingList.data.size());
                //this.adapter = new AutoClearedValue<>(this, adapter);
                binding.shoppingListRecycler.setAdapter(adapter);
                adapter.replace(shoppingList.data);
            }
        });

        //FAB-addNewShoppingList
        shoppingListViewModel.getAddShoppingListEvent().observe(this, movie -> {
            Timber.d("ShoppingList have been clicked: " + movie);
            shoppingListViewModel.insertAll();
        });


        //For quick test only
        //shoppingListViewModel.insertAll();
        shoppingListViewModel.deleteAll();
        //shoppingListViewModel.setLogin("1");

        //For quick test only
        /*shoppingListViewModel.getShoppingLists().observe(this, shoppingList -> {
            Timber.d("Ceo objekat " + shoppingList);
            if (shoppingList.data == null) {
                Timber.d("It's NULL");
            }  else {
                Timber.d("List Size: " + shoppingList.data.size());
            }
        });*/

    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

