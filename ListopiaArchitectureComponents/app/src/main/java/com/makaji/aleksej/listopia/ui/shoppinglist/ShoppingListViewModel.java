package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.makaji.aleksej.listopia.ListopiaApp;
import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.ShoppingListWithProducts;
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
    private LiveData<Resource<ShoppingList>> shoppingList;
    private final LiveData<Resource<List<ShoppingListWithProducts>>> shoppingListsWithProducts;
    final MutableLiveData<Integer> shoppingListId = new MutableLiveData<>();
    private MutableLiveData<String> userId = new MutableLiveData<>();
    private final SingleLiveData<Void> addShoppingListClick = new SingleLiveData<>();
    private final SingleLiveData<Void> createShoppingListClick = new SingleLiveData<>();
    private final SingleLiveData<Void> renameShoppingListClick = new SingleLiveData<>();
    public final MutableLiveData<String> textListName = new MutableLiveData<>();
    public final MutableLiveData<String> errorTextListName = new MutableLiveData<>();


    @Inject
    ShoppingListRepository shoppingListRepository;

    @Inject
    Resources resources;

    @Inject
    SharedPreferences sharedPreferences;


    @Inject
    public ShoppingListViewModel(ShoppingListRepository shoppingListRepository) {
        shoppingLists = shoppingListRepository.loadAllShoppingLists();
        Timber.d("ShoppingListViewModel ShoppingLists");
        shoppingList = Transformations.switchMap(shoppingListId, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return shoppingListRepository.findShoppingListById(id);
            }
        });

        shoppingListsWithProducts = Transformations.switchMap(userId, id -> {
            Timber.d("Transformations shoppingListsWithProducts");
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return shoppingListRepository.loadShoppingListsWithProducts(id);
            }
        });
    }

    public LiveData<Resource<List<ShoppingList>>> getShoppingLists() {
        return shoppingLists;
    }

    public LiveData<Resource<ShoppingList>> getShoppingList() {
        return shoppingList;
    }

    public LiveData<Resource<List<ShoppingListWithProducts>>> getShoppingListsWithProducts() {
        return shoppingListsWithProducts;
    }

    //Set id to get shoppingList (call findShoppingListById)
    public void setId(Integer id) {
        if (Objects.equals(shoppingListId.getValue(), id)) {
            return;
        }
        shoppingListId.setValue(id);
    }

    //Set id to get ShoppingListsWithProducts based on UserId (call loadShoppingListsWithProducts)
    public void setUserId(String id) {
        Timber.d(" pre setting userId ShoppingListViewModel");
        if (Objects.equals(userId.getValue(), id)) {
            return;
        }
        Timber.d("setting userId ShoppingListViewModel");
        userId.setValue(id);
    }

    //FAB click event
    public void onAddShoppingListClick() {
        //insertAll();
        addShoppingListClick.call();
    }
    //Get FAB click event
    public SingleLiveData<Void> getAddShoppingListClick() {
        return addShoppingListClick;
    }

    //Create shoppingList click event
    public void onCreateShoppingListClick() {
        if (!validateCreateForm()) {
            return;
        }

        String resUserId = resources.getString(R.string.key_user_id);
        Timber.d("Create resUserId " + resUserId);
        String userId = sharedPreferences.getString(resUserId, "defualtValue");
        Timber.d("Create userId " + userId);

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setUserId(userId);
        shoppingList.setName(textListName.getValue());

        shoppingListRepository.insertShoppingList(shoppingList);

        createShoppingListClick.call();
    }

    //Get create shoppingList click event
    public LiveData<Void> getCreateShoppingListClick() {
        return createShoppingListClick;
    }

    //Rename shoppingList click event
    public void onRenameShoppingListClick(ShoppingList shoppingList) {
        if (!validateRenameForm()) {
            return;
        }
        shoppingListRepository.updateShoppingList(shoppingList);
        renameShoppingListClick.call();
    }

    //Get rename shoppingList click event
    public LiveData<Void> getRenameShoppingListClick() {
        return renameShoppingListClick;
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
        } else if (textListName.getValue().length() > resources.getInteger(R.integer.listNameMaxLength)) {
            errorTextListName.setValue("List name need to be shorter");
            isValid = false;
        } else {
            errorTextListName.setValue(null);
        }
        return isValid;
    }

    private boolean validateRenameForm() {
        boolean isValid = true;
        String listName = shoppingList.getValue().data.getName();
        if ((listName == null) || (listName.length() == 0)) {
            errorTextListName.setValue("List name can't be empty");
            isValid = false;
        } else if (listName.length() > resources.getInteger(R.integer.listNameMaxLength)) {
            errorTextListName.setValue("List name need to be shorter");
            isValid = false;
        } else {
            errorTextListName.setValue(null);
        }
        return isValid;
    }

    //Get error
    public LiveData<String> getErrorTextListName() {
        return errorTextListName;
    }

    public void deleteAllShoppingLists() {
        shoppingListRepository.deleteAllShoppingLists();
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        shoppingListRepository.deleteShoppingList(shoppingList);
    }

    /////////////////////////////// Quick TEST ONLY //////////////////////

    //For quick test only
    public void insertAll() {
        shoppingListRepository.insertAll();
    }



}
