package com.makaji.aleksej.listopia.ui.user;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.databinding.ActivityFriendBinding;
import com.makaji.aleksej.listopia.databinding.ActivityProductBinding;
import com.makaji.aleksej.listopia.ui.base.BaseActivity;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.product.ProductNavigationController;
import com.makaji.aleksej.listopia.ui.product.ProductViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by Aleksej on 2/20/2018.
 */

public class FriendActivity extends BaseActivity implements HasSupportFragmentInjector, OnFragmentToolbarInteraction {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    UserNavigationController userNavigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private UserViewModel userViewModel;

    private ActivityFriendBinding binding;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_friend);

        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        binding.setViewModel(userViewModel);

        //Setup toolbar
        toolbar = (Toolbar) binding.toolbar;
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            userNavigationController.navigateToFriend();
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
        getMenuInflater().inflate(R.menu.menu_friend_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.settings:
                //It has submenu, in case we need logic when settings has been clicked, we write here
                return true;
            case R.id.submenu_settings_delete_all:
                userViewModel.deleteAllFriends();
                return true;*/
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
