package com.makaji.aleksej.listopia.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.makaji.aleksej.listopia.data.Dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;

/**
 * Created by Aleksej on 12/17/2017.
 */

/**
 * Main database description.
 */
@Database(entities = {ShoppingList.class}, version = 3)
public abstract class ListopiaDb extends RoomDatabase {

    abstract public ShoppingListDao shoppingListDao();

}
