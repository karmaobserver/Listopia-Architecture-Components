package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;

import java.util.Timer;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
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



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);

        //For quick test only
        //shoppingListViewModel.insertAll();
        shoppingListViewModel.setLogin("1");

        //For quick test only
        shoppingListViewModel.getShoppingLists().observe(this, shoppingList -> {
            Timber.d("Ceo objekat " + shoppingList);
            if (shoppingList.data == null) {
                Timber.d("It's NULL");
            }  else {
                Timber.d("List Size: " + shoppingList.data.size());
            }
        });

    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

