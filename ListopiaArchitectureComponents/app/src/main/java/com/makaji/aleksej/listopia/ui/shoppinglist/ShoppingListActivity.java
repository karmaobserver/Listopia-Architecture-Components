package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.databinding.ActivityShoppingListBinding;
import com.makaji.aleksej.listopia.databinding.HeaderNavigationBinding;
import com.makaji.aleksej.listopia.databinding.ItemShoppingListBinding;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;

import android.databinding.DataBindingComponent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import java.util.List;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;


public class ShoppingListActivity extends BaseActivity implements HasSupportFragmentInjector, OnFragmentToolbarInteraction{

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ShoppingListNavigationController shoppingListNavigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private ShoppingListViewModel shoppingListViewModel;

    private ActivityShoppingListBinding binding;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list);

        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);
        binding.setViewModel(shoppingListViewModel);

        //Setup toolbar
        toolbar = (Toolbar) binding.toolbar;
        setSupportActionBar(toolbar);

        // Setup drawer view
        setupDrawerContent();



        if (savedInstanceState == null) {
            shoppingListNavigationController.navigateToShoppingList();
        }


    }

    @Override
    public void enableNavigationBackIcon() {
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void setToolbarTitle(int resId) {
        toolbar.setTitle(resId);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void enableNavigationDrawerIcon() {
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setupDrawerContent() {
        //navigationView.getMenu().getItem(R.id.sign_out).setVisible(false);

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navigationvView;
        HeaderNavigationBinding headerBinding = HeaderNavigationBinding.bind(navigationView.getHeaderView(0));
        //test only
        headerBinding.setIsLogged(true);

        //To show original color of icons
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        headerBinding.signIn.setOnClickListener(view -> {
            shoppingListNavigationController.navigateToSignIn();
            // Close the navigation drawer
            drawerLayout.closeDrawers();
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.trash:
                Timber.d("TRASH1 ITEM GOT CLICKED" );
                break;
            case R.id.settings:
                Timber.d("Settings ITEM GOT CLICKED" );
                shoppingListNavigationController.navigateToSettings();
                break;
            default:
                Timber.d("TRASH3 ITEM GOT CLICKED" );
        }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //It has submenu, in case we need logic when settings has been clicked, we write here
                return true;
            case R.id.submenu_settings_delete_all:
                shoppingListViewModel.deleteAllShoppingLists();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawer(navigationView);
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return;
        }
        finish();
    }
}

