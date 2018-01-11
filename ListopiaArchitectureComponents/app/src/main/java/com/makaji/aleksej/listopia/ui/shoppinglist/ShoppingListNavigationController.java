package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.support.v4.app.FragmentManager;

import com.makaji.aleksej.listopia.R;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/3/2018.
 */

/**
 * A utility class that handles navigation in {@link ShoppingListActivity}.
 */
public class ShoppingListNavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public ShoppingListNavigationController(ShoppingListActivity shoppingListActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = shoppingListActivity.getSupportFragmentManager();
    }

    public void navigateToShoppingList() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss();
        //Careful, need to be tested
        //clearBackStack();
    }

    public void navigateToAddShoppingList() {
        ShoppingListAddFragment fragment = new ShoppingListAddFragment();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(containerId, fragment)
                .commitAllowingStateLoss();
    }

    public void popBackStackMethod() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            return;
        }
        Timber.d("BACKSTACK IS EMPTY!!!!!!!!!!!!!!!!!!");
    }

    public void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return;
        }
        Timber.d("BACKSTACK IS EMPTY!!!!!!!!!!!!!!!!!!");

    }



   /* public void navigateToRepo(String owner, String name) {
        RepoFragment fragment = RepoFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToUser(String login) {
        String tag = "user" + "/" + login;
        UserFragment userFragment = UserFragment.create(login);
        fragmentManager.beginTransaction()
                .replace(containerId, userFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }*/
}

