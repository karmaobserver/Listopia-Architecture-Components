package com.makaji.aleksej.listopia.data.api;

import android.arch.lifecycle.LiveData;

import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Aleksej on 12/29/2017.
 */

/**
 * REST API access points
 */
public interface ListopiaService {

    @GET("api/shoppingLists")
    LiveData<ApiResponse<List<ShoppingList>>> getShoppingLists();

    @GET("api/products")
    LiveData<ApiResponse<List<Product>>> getProductsByShoppingListId();
}
