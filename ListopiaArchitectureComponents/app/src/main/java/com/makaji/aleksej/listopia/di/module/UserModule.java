package com.makaji.aleksej.listopia.di.module;

import com.makaji.aleksej.listopia.ui.product.ProductAddFragment;
import com.makaji.aleksej.listopia.ui.product.ProductFragment;
import com.makaji.aleksej.listopia.ui.product.ProductRenameFragment;
import com.makaji.aleksej.listopia.ui.user.FindFriendFragment;
import com.makaji.aleksej.listopia.ui.user.FriendShareFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Aleksej on 2/20/2018.
 */

@Module
public abstract class UserModule {

    @ContributesAndroidInjector
    abstract FriendShareFragment contributeFriendShareFragment();

    @ContributesAndroidInjector
    abstract FindFriendFragment contributeFindFriendFragment();


}
