package com.makaji.aleksej.listopia.ui.product;

import android.support.v4.app.FragmentManager;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListAddFragment;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListRenameFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

/**
 * A utility class that handles navigation in {@link ProductActivity}.
 */
public class ProductNavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public ProductNavigationController(ProductActivity productActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = productActivity.getSupportFragmentManager();
    }

    public void navigateToProduct(Integer id, String name) {
        ProductFragment fragment = ProductFragment.create(id, name);
        String tag = "product" + "/" + id + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
        //Careful, need to be tested
        //clearBackStack();
    }

    public void navigateToAddProduct(Integer id) {
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
}
