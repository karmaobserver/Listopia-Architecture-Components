package com.makaji.aleksej.listopia.data.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.makaji.aleksej.listopia.AppExecutors;
import com.makaji.aleksej.listopia.data.api.ApiResponse;
import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ProductDao;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

public class ProductRepository {
    private final AppExecutors appExecutors;
    private final ListopiaService listopiaService;
    private final ProductDao productDao;

    @Inject
    public ProductRepository(AppExecutors appExecutors, ProductDao productDao, ListopiaService listopiaService) {
        this.appExecutors = appExecutors;
        this.productDao = productDao;
        this.listopiaService = listopiaService;
    }

    public LiveData<Resource<List<Product>>> getProductsByShoppingListId(int id) {
        return new NetworkBoundResource<List<Product>, List<Product>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Product> items) {
                Timber.d("SaveCall itemSize: " + items.size());
                productDao.insertAll(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Product> data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<List<Product>> loadFromDb() {
                Timber.d("LoadFromDB: ");
                return productDao.getProductsByShoppingListId(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Product>>> createCall() {
                Timber.d("Making call:");
                LiveData<ApiResponse<List<Product>>> result = listopiaService.getProductsByShoppingListId();
                Timber.d("Result is: " + result);
                return result;
                //return listopiaService.getShoppingLists();
            }
        }.asLiveData();
    }

    public LiveData<Resource<Product>> findProductById(int id) {
        return new NetworkBoundResource<Product, Product>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Product items) {
            }

            @Override
            protected boolean shouldFetch(@Nullable Product data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Product> loadFromDb() {
                Timber.d("LoadFromDB: findProductById");
                return productDao.findProductById(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Product>> createCall() {
                return null;
            }
        }.asLiveData();
    }


    //Temporary Solution
    @SuppressLint("StaticFieldLeak")
    public void insertProduct(Product product) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                productDao.insertProduct(product);
                return null;
            }
        }.execute();
    }

    //Temporary Solution
    public void deleteAllProducts() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                productDao.deleteAll();
                return null;
            }
        }.execute();
    }

    //Temporary Solution
    @SuppressLint("StaticFieldLeak")
    public void updateProduct(Product product) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                productDao.updateProduct(product);
                return null;
            }
        }.execute();
    }
}
