package com.makaji.aleksej.listopia.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.data.repository.UserRepository;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.AbsentLiveData;

import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 2/4/2018.
 */

public class UserViewModel  extends ViewModel {

    final MutableLiveData<String> userId = new MutableLiveData<>();
    private LiveData<Resource<User>> user;

    @Inject
    UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        user = Transformations.switchMap(userId, id -> {
            Timber.d("User Transformation " + id);
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.findUserById(id);
            }
        });
    }

    //Set id to get shoppingList (call findShoppingListById)
    public void setId(String id) {
        if (Objects.equals(userId.getValue(), id)) {
            return;
        }
        Timber.d("Setting User Id");
        userId.setValue(id);
    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }

   /* public void validateToken() {
        userRepository.validateToken();
    }*/
   /* public MutableLiveData<Resource<User>> validateToken() {
        return userRepository.validateToken();
    }*/
   public LiveData<Resource<User>> validateToken(String userId) {
       return userRepository.validateToken(userId);
   }


}
