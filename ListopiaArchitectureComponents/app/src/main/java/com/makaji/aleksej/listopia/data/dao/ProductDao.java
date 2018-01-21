package com.makaji.aleksej.listopia.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;

import java.util.List;

/**
 * Created by Aleksej on 1/14/2018.
 */

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product WHERE shoppingListId = :id")
    LiveData<List<Product>> getProductsByShoppingListId(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(Product product);

    @Query("DELETE FROM product")
    void deleteAll();

    @Update
    void updateProduct(Product product);

    @Query("SELECT * FROM product WHERE id = :id")
    LiveData<Product> findProductById(int id);

}
