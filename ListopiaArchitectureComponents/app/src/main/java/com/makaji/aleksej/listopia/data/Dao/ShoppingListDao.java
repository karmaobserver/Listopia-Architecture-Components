package com.makaji.aleksej.listopia.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.makaji.aleksej.listopia.data.entity.ShoppingList;

import java.util.List;

/**
 * Created by Aleksej on 12/16/2017.
 */

@Dao
public interface ShoppingListDao {

    @Query("SELECT * FROM shopping_list")
    LiveData<List<ShoppingList>> loadAllShoppingLists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ShoppingList> shoppingLists);

    @Query("DELETE FROM shopping_list")
    void deleteAll();

}
