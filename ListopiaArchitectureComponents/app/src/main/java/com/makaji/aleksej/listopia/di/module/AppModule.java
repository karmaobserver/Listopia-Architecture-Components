package com.makaji.aleksej.listopia.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.database.ListopiaDb;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    ListopiaDb provideDb(Application app) {
        return Room.databaseBuilder(app, ListopiaDb.class,"listopia.db").build();
    }

    @Singleton @Provides
    ShoppingListDao provideShoppingListDao(ListopiaDb db) {
        return db.shoppingListDao();
    }

}
