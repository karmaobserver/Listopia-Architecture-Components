package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.databinding.ActivityShoppingListBinding;
import com.makaji.aleksej.listopia.databinding.HeaderNavigationBinding;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.user.UserViewModel;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;


public class ShoppingListActivity extends BaseActivity implements HasSupportFragmentInjector, OnFragmentToolbarInteraction {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ShoppingListNavigationController shoppingListNavigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    GoogleSignInOptions googleSignInOptions;

    @Inject
    SharedPreferences sharedPreferences;

    private GoogleSignInClient googleSignInClient;

    private ShoppingListViewModel shoppingListViewModel;

    private UserViewModel userViewModel;

    private ActivityShoppingListBinding binding;

    private HeaderNavigationBinding headerBinding;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_list);

        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        binding.setViewModel(shoppingListViewModel);

        //Setup toolbar
        toolbar = (Toolbar) binding.toolbar;
        setSupportActionBar(toolbar);

        // Setup drawer view
        setupDrawerContent(userViewModel);

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        if (savedInstanceState == null) {
            shoppingListNavigationController.navigateToShoppingList();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();


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

    private void setupDrawerContent(UserViewModel userViewModel) {

        drawerLayout = binding.drawerLayout;
        navigationView = binding.navigationvView;
        headerBinding = HeaderNavigationBinding.bind(navigationView.getHeaderView(0));

        //TODO is it good solution?
       /* String resUserId = getResources().getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defaultUserId");
        userViewModel.setId(userId);*/

        subscribeToUser();

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
            drawerLayout.closeDrawers();
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.trash:
                Timber.d("TRASH1 ITEM GOT CLICKED" );
                break;
            case R.id.settings:
                shoppingListNavigationController.navigateToSettings();
                break;
            case R.id.sign_out:
                googleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Timber.d("SignOut");
                        sharedPreferences.edit().putString(getString(R.string.key_token), "").commit();
                        sharedPreferences.edit().putString(getString(R.string.key_user_id), "").commit();
                        userViewModel.setId("0");
                        //Refresh UI in fragment
                        ShoppingListFragment fragment = (ShoppingListFragment) getSupportFragmentManager().findFragmentByTag("FRAGMENT_SHOPPING_LIST_TAG");
                        fragment.subscribeToUi(shoppingListViewModel);
                        Snackbar.make(drawerLayout, R.string.menu_drawer_sign_out, Snackbar.LENGTH_LONG).show();
                    }
                });
            default:
                Timber.d("SomeDefualt ITEM GOT CLICKED" );
        }
        // Highlight the selected item has been done by NavigationView
        //menuItem.setChecked(true);
        // Set action bar title
       // setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get user by userId
        String resUserId = getResources().getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defaultUserId");
        userViewModel.setId(userId);
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

    public void subscribeToUser() {
        userViewModel.getUser().observe(this, user -> {
            Timber.d("Observing user " + user);
            if (user == null || user.data == null) {
                Timber.d("User is NULL");
                headerBinding.setUser(null);
                //Hide Sign Out item
                navigationView.getMenu().getItem(5).setVisible(false);

            } else {
                Timber.d("User which I set: " + user.data.getName());
                headerBinding.setUser(user.data);
                headerBinding.textName.setText(user.data.getName());
                headerBinding.textEmail.setText(user.data.getEmail());
                //Show Sign Out item
                navigationView.getMenu().getItem(5).setVisible(true);
            }
        });
    }

}

