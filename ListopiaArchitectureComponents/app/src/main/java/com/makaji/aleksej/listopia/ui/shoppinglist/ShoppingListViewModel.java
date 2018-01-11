package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
import android.util.Log;

import com.makaji.aleksej.listopia.ListopiaApp;
import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.database.ListopiaDb;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.AbsentLiveData;
import com.makaji.aleksej.listopia.util.SingleLiveData;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 12/17/2017.
 */

public class ShoppingListViewModel extends ViewModel {

    private final LiveData<Resource<List<ShoppingList>>> shoppingLists;
    final MutableLiveData<String> login = new MutableLiveData<>();
    private final SingleLiveData<Void> addShoppingListClick = new SingleLiveData<>();
    private final SingleLiveData<Void> createShoppingListClick = new SingleLiveData<>();
    public final MutableLiveData<String> textListName = new MutableLiveData<>();
    public final MutableLiveData<String> errorTextListName = new MutableLiveData<>();


    @Inject
    ShoppingListRepository shoppingListRepository;


    @Inject
    public ShoppingListViewModel(ShoppingListRepository shoppingListRepository) {
        shoppingLists = shoppingListRepository.loadAllShoppingLists();
        Timber.d("ShoppingListViewModel ShoppingLists");
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



    //FAB click event
    public void onAddShoppingListClick() {
        Timber.d("onAdd in ViewModel");
        //insertAll();
        addShoppingListClick.call();
    }
    //Get FAB click event
    public SingleLiveData<Void> getAddShoppingListClick() {
        Timber.d("getAdd in ViewModel");
        return addShoppingListClick;
    }

    //Create shoppingList click event
    public void onCreateShoppingListClick() {
        Timber.d("onCreate in ViewModel");

        if (!validateCreateForm()) {
            return;
        }

        Random r = new Random();
        int id = r.nextInt((1000 - 1) + 1) + 1;

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName(textListName.getValue());
        shoppingList.setId(id);
        shoppingListRepository.insertShoppingList(shoppingList);

        createShoppingListClick.call();
    }

    /*//Get create shoppingList click event
    public LiveData<String> getCreateShoppingListClick() {
        Timber.d("getCreate in ViewModel");
        return textListName;
    }*/

    //Get create shoppingList click event
    public LiveData<Void> getCreateShoppingListClick() {
        Timber.d("getCreate in ViewModel");
        return createShoppingListClick;
    }

    public LiveData<String> getErrorTextListName() {
        return errorTextListName;
    }



    /**
     * Validate form
     * @return
     */
    private boolean validateCreateForm() {
        boolean isValid = true;

        if ((textListName.getValue() == null) || (textListName.getValue().length() == 0)) {
            errorTextListName.setValue("List name can't be empty");
            isValid = false;
        } else if (textListName.getValue().length() > ListopiaApp.getContext().getResources().getInteger(R.integer.listNameMaxLength)) {
            errorTextListName.setValue("List name need to be shorter");
            isValid = false;
        } else {
            errorTextListName.setValue(null);
        }
        return isValid;
    }

    /////////////////////////////// Quick TEST ONLY //////////////////////


    //For quick test only
    public void insertAll() {
        shoppingListRepository.insertAll();
    }

    //For quick delete testing only
    public void deleteAll() {
        shoppingListRepository.deleteAll();
    }

}
