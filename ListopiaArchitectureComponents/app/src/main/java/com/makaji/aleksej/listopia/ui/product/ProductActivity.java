package com.makaji.aleksej.listopia.ui.product;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.databinding.ActivityProductBinding;
import com.makaji.aleksej.listopia.databinding.ActivityShoppingListBinding;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListNavigationController;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

public class ProductActivity extends BaseActivity implements HasSupportFragmentInjector, OnFragmentToolbarInteraction {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProductNavigationController productNavigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private static final String SHOPPING_LIST_ID = "shopping_list_id";
    private static final String SHOPPING_LIST_NAME = "shopping_list_name";

    private ProductViewModel productViewModel;

    private ActivityProductBinding binding;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product);

        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);
        binding.setViewModel(productViewModel);

        //Setup toolbar
        toolbar = (Toolbar) binding.toolbar;
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Integer id = intent.getIntExtra(SHOPPING_LIST_ID, 0);
            String name = intent.getStringExtra(SHOPPING_LIST_NAME);
            productNavigationController.navigateToProduct(id, name);
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
        //Has no navigation drawer
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //It has submenu, in case we need logic when settings has been clicked, we write here
                return true;
            case R.id.submenu_settings_delete_all:
                productViewModel.deleteAllProducts();
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
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return;
        }
        finish();
    }

}
