package com.makaji.aleksej.listopia.data.repository;

import com.makaji.aleksej.listopia.data.Dao.ShoppingListDao;
import com.makaji.aleksej.listopia.AppExecutors;

import javax.inject.Inject;

/**
 * Created by Aleksej on 12/16/2017.
 */

public class ShoppingListRepository {
    private final AppExecutors appExecutors;
    private final ShoppingListDao shoppingListDao;

    @Inject
    public ShoppingListRepository(AppExecutors appExecutors, ShoppingListDao shoppingListDao) {
        this.appExecutors = appExecutors;
        this.shoppingListDao = shoppingListDao;
    }
}
