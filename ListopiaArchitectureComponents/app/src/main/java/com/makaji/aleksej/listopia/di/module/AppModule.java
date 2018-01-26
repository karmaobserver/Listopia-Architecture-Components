package com.makaji.aleksej.listopia.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ProductDao;
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

    @Provides
    @Singleton
    Context provideAppContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    Resources provideResources(Application application) {
        return application.getResources();
    }

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
    ListopiaDb provideDb(Application application) {
        return Room.databaseBuilder(application, ListopiaDb.class,"listopia.db").build();
        //For destroying database and creating new, also I need to change version in ListopiaDB
        //return Room.databaseBuilder(app, ListopiaDb.class, "listopia.db").fallbackToDestructiveMigration().build();
    }

    @Singleton
    @Provides
    ShoppingListDao provideShoppingListDao(ListopiaDb listopiaDb) {
        return listopiaDb.shoppingListDao();
    }

    @Singleton
    @Provides
    ProductDao provideProductDao(ListopiaDb listopiaDb) {
        return listopiaDb.productDao();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Singleton
    @Provides
    GoogleSignInOptions provideGoogleSignInOptions() {
        return new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    /*@Singleton
    @Provides
    GoogleApiClient provideGoogleApiClient(Context context, GoogleSignInOptions googleSignInOptions) {
        return new GoogleApiClient.Builder(context)
                .enableAutoManage(context, connectionFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }*/
}
