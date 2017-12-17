package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ShoppingListActivity extends AppCompatActivity {

    @Inject
    ShoppingListRepository shoppingListRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
