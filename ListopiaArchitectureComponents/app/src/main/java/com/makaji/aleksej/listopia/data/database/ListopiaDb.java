package com.makaji.aleksej.listopia.data.database;

import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.makaji.aleksej.listopia.AppExecutors;
import com.makaji.aleksej.listopia.data.dao.ProductDao;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.dao.UserDao;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Aleksej on 12/17/2017.
 */

/**
 * Main database description.
 */
@Database(entities = {ShoppingList.class, Product.class, User.class}, version = 8)
public abstract class ListopiaDb extends RoomDatabase {

    abstract public ShoppingListDao shoppingListDao();
    abstract public ProductDao productDao();
    abstract public UserDao userDao();

    //For destroying database and creating new, also I need to change version in ListopiaDB and in AppModule change builder

}
