package com.makaji.aleksej.listopia.ui.user;

/**
 * Created by Aleksej on 2/20/2018.
 */

import android.support.v4.app.FragmentManager;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.ui.product.ProductFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A utility class that handles navigation in {@link FriendActivity}.
 */
public class UserNavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public UserNavigationController(FriendActivity productActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = productActivity.getSupportFragmentManager();
    }

    /*public void navigateToFriend(Integer id, String name) {
        FriendShareFragment fragment = FriendShareFragment.create(id, name);
        String tag = "friend" + "/" + id + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
        //Careful, need to be tested
        //clearBackStack();


    }*/

    public void navigateToFriend() {
        FriendShareFragment fragment = new FriendShareFragment();
        String tag = "FRAGMENT_FRIEND_SHARE_TAG";
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
        //Careful, need to be tested
        //clearBackStack();
    }

    public void navigateToFindFriend() {
        FindFriendFragment fragment = new FindFriendFragment();
        String tag = "FRAGMENT_FRIEND_FIND_TAG";
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }



    /*public void navigateToAddProduct(Integer id) {
        ProductAddFragment fragment = ProductAddFragment.create(id);
        String tag = "product" + "/" + id;
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    public void navigateToRenameProduct(int id) {
        ProductRenameFragment fragment = ProductRenameFragment.create(id);
        String tag = "product" + "/" + id;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }*/

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
}
