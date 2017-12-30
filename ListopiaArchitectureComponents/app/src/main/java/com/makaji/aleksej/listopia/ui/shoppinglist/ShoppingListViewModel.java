package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.makaji.aleksej.listopia.data.database.ListopiaDb;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.AbsentLiveData;
import com.makaji.aleksej.listopia.util.SingleLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 12/17/2017.
 */

public class ShoppingListViewModel extends ViewModel {

    private final LiveData<Resource<List<ShoppingList>>> shoppingLists;
    final MutableLiveData<String> login = new MutableLiveData<>();
    private final SingleLiveData<Void> addShoppingListEvent = new SingleLiveData<>();

    @Inject
    ShoppingListRepository shoppingListRepository;


    @Inject
    public ShoppingListViewModel(ShoppingListRepository shoppingListRepository) {
        shoppingLists = shoppingListRepository.loadAllShoppingLists();
       /* shoppingLists = Transformations.switchMap(login, login -> {
            if (login == null) {
                return AbsentLiveData.create();
            } else {
                return shoppingListRepository.loadAllShoppingLists();
            }
        });*/
    }

    public LiveData<Resource<List<ShoppingList>>> getShoppingLists() {
        return shoppingLists;
    }

    public void setLogin(String login) {
        if (Objects.equals(this.login.getValue(), login)) {
            return;
        }
        this.login.setValue(login);
    }

    //For quick test only
    public void insertAll() {
        shoppingListRepository.insertAll();
    }

    //For quick delete testing only
    public void deleteAll() {
        shoppingListRepository.deleteAll();
    }


    public void onAddShoppingListClick() {
        Timber.d("Usao u viewmodel onAdd");
        addShoppingListEvent.call();
    }

    public SingleLiveData<Void> getAddShoppingListEvent() {
        Timber.d("Usao u viewmodel getAdd");
        return addShoppingListEvent;
    }

}
