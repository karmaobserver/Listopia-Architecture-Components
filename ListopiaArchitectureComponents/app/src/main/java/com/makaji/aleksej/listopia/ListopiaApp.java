package com.makaji.aleksej.listopia;

import android.app.Activity;
import android.app.Application;

import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.di.module.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Aleksej on 12/17/2017.
 */

public class ListopiaApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
       /* if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }*/
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
