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
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<ShoppingList>> loadFromDb() {
                Timber.d("LoadFromDB: ");
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

    public LiveData<Resource<ShoppingList>> findShoppingListById(int id) {
        return new NetworkBoundResource<ShoppingList, ShoppingList>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull ShoppingList items) {
            }

            @Override
            protected boolean shouldFetch(@Nullable ShoppingList data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<ShoppingList> loadFromDb() {
                Timber.d("LoadFromDB: findShoppingListById");
                return shoppingListDao.findShoppingListById(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ShoppingList>> createCall() {
                return null;
            }
        }.asLiveData();
    }

    //Temporary Solution
    @SuppressLint("StaticFieldLeak")
    public void insertShoppingList(ShoppingList shoppingList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shoppingListDao.insertShoppingList(shoppingList);
                return null;
            }
        }.execute();
    }

    //Temporary Solution
    @SuppressLint("StaticFieldLeak")
    public void updateShoppingList(ShoppingList shoppingList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shoppingListDao.updateShoppingList(shoppingList);
                return null;
            }
        }.execute();
    }

    //Temporary Solution
    /*@SuppressLint("StaticFieldLeak")
    public void findShoppingListById(int id) {
        new AsyncTask<Void, Void, ShoppingList>() {
            @Override
            protected ShoppingList doInBackground(Void... voids) {
                return shoppingListDao.findShoppingListById(id);
            }

            @Override
            protected void onPostExecute(ShoppingList result) {
                nesto(result);
            }
        }.execute();
    }*/
    /*public ShoppingList findShoppingListById(int id) {
        final ShoppingList[] shoppingList = {new ShoppingList()};
        appExecutors.diskIO().execute(() -> {
            shoppingList[0] = shoppingListDao.findShoppingListById(id);
        });
        return shoppingList[0];
    }*/




    //////////////////////////////////////// For Quick Test Only ////////////////////////////

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

    public void deleteAllShoppingLists() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                shoppingListDao.deleteAll();
                return null;
            }
        }.execute();

    }
}
