package com.makaji.aleksej.listopia.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.database.ListopiaDb;
import com.makaji.aleksej.listopia.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    ListopiaService provideListopiaService() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.10:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ListopiaService.class);
    }

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
