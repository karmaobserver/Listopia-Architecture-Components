package com.makaji.aleksej.listopia.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.util.StringUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.preference.PreferenceManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.api.ListopiaService;
import com.makaji.aleksej.listopia.data.dao.ProductDao;
import com.makaji.aleksej.listopia.data.dao.ShoppingListDao;
import com.makaji.aleksej.listopia.data.dao.UserDao;
import com.makaji.aleksej.listopia.data.database.ListopiaDb;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListActivity;
import com.makaji.aleksej.listopia.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 15;

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

   /* @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }*/

    @Singleton
    @Provides
    ListopiaService provideListopiaService(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.0.10:8080/")
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(ListopiaService.class);
    }

    /**
     * Interceptor for http requests, to set dynamic header, token
     * @param sharedPreferences
     * @param resources
     * @return
     */
    @Provides
    @Singleton
    Interceptor provideInterceptor(SharedPreferences sharedPreferences, Resources resources) {
        return chain -> {
            String resToken = resources.getString(R.string.key_token);
            Timber.d("Interceptor resToken: " + resToken);
            String token = sharedPreferences.getString(resToken, "defult");
            Timber.d("Interceptor token: " + token);
            if (!(token.isEmpty())) {
                //final String bearer = "BEARER " + token;
                final String bearer = token;
                final Request.Builder builder = chain.request().newBuilder()
                        .header("Authorization", bearer)
                        .header("Accept", "application/json");

                return chain.proceed(builder.build());
            } else {
                return chain.proceed(chain.request());
            }
        };
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(Cache cache, Interceptor interceptor) {
        return new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(Application application) {
        final int cacheSize = 10 * 1024 * 1024; // 10MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Singleton
    @Provides
    ListopiaDb provideDb(Application application) {
        return Room.databaseBuilder(application, ListopiaDb.class,"listopia.db").build();
        //For destroying database and creating new, also I need to change version in ListopiaDB
        //return Room.databaseBuilder(application, ListopiaDb.class, "listopia.db").fallbackToDestructiveMigration().build();
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

    @Singleton
    @Provides
    UserDao provideUserDao(ListopiaDb listopiaDb) {
        return listopiaDb.userDao();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Singleton
    @Provides
    GoogleSignInOptions provideGoogleSignInOptions(Resources resources) {
        Timber.d("GSO Client ID string is: " + resources.getString(R.string.server_client_id));
        return new  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.server_client_id))
                .requestEmail()
                .build();
    }



}
