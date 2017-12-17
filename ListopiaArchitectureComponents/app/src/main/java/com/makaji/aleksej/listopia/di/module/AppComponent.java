package com.makaji.aleksej.listopia.di.module;

import android.app.Application;

import com.makaji.aleksej.listopia.ListopiaApp;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Aleksej on 12/17/2017.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ShoppingListActivityModule.class,

})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
    void inject(ListopiaApp listopiaApp);
}
