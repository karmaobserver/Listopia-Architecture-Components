package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Aleksej on 12/17/2017.
 */

public class ShoppingListViewModel extends ViewModel {

    private final ShoppingListRepository shoppingListRepository;

    @Inject
    public ShoppingListViewModel(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;

    }
}
