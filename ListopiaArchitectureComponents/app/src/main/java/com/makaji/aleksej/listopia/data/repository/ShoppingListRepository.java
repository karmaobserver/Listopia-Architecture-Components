package com.makaji.aleksej.listopia.data.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.makaji.aleksej.listopia.data.api.ApiResponse;
import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.AppExecutors;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.vo.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 12/16/2017.
 */

public class ShoppingListRepository {
    private final AppExecutors appExecutors;
    private final ListopiaService listopiaService;
    private final ShoppingListDao shoppingListDao;

    @Inject
    public ShoppingListRepository(AppExecutors appExecutors, ShoppingListDao shoppingListDao, ListopiaService listopiaService) {
        this.appExecutors = appExecutors;
        this.shoppingListDao = shoppingListDao;
        this.listopiaService = listopiaService;
    }

    public LiveData<Resource<List<ShoppingList>>> loadAllShoppingLists() {
        return new NetworkBoundResource<List<ShoppingList>, List<ShoppingList>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ShoppingList> items) {
                Timber.d("SaveCall itemSize: " + items.size());
                shoppingListDao.insertAll(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ShoppingList> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<ShoppingList>> loadFromDb() {
                return shoppingListDao.loadAllShoppingLists();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ShoppingList>>> createCall() {
                Timber.d("Making call:");
                LiveData<ApiResponse<List<ShoppingList>>> result = listopiaService.getShoppingLists();
                Timber.d("Result is: " + result);
                return result;
                //return listopiaService.getShoppingLists();
            }
        }.asLiveData();
    }

    //For quick test only
    @SuppressLint("StaticFieldLeak")
    public void insertAll() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shoppingListDao.insertAll(generateShoppingLists());
                return null;
            }
        }.execute();
    }

    //For quick test only
    public static List<ShoppingList> generateShoppingLists() {
        List<ShoppingList> shoppingLists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ShoppingList shoppingList = new ShoppingList();
            shoppingList.setId(i);
            shoppingList.setName("Name of the List is " + i);
            shoppingLists.add(shoppingList);
        }
        return shoppingLists;
    }

    public void deleteAll() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shoppingListDao.deleteAll();
                return null;
            }
        }.execute();

    }
}
