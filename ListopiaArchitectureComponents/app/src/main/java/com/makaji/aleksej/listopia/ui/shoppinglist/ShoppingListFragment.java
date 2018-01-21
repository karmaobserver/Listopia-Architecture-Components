package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.databinding.FragmentShoppingListBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.product.ProductActivity;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/4/2018.
 */

public class ShoppingListFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ShoppingListNavigationController shoppingListNavigationController;

    private static final String SHOPPING_LIST_ID = "shopping_list_id";
    private static final String SHOPPING_LIST_NAME = "shopping_list_name";

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private ShoppingListViewModel shoppingListViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentShoppingListBinding> binding;
    private AutoClearedValue<ShoppingListAdapter> adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentToolbarInteraction) {
            onFragmentToolbarInteraction = (OnFragmentToolbarInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShoppingListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list,
                container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);
        binding.get().setViewModel(shoppingListViewModel);

        setShoppingListCallBacks();

        subscribeToUi(shoppingListViewModel);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Set back navigation to toolbar
        //onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_shopping_list);
        onFragmentToolbarInteraction.enableNavigationDrawerIcon();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentToolbarInteraction = null;
    }

    /**
     * ShoppingListCallBack, with events onClick, onLongClick, onButtonClick
     */
    private void setShoppingListCallBacks() {
        ShoppingListAdapter shoppingListAdapter = new ShoppingListAdapter(dataBindingComponent, shoppingListOnClick -> {

            Intent intent = new Intent(getActivity(), ProductActivity.class);
            intent.putExtra(SHOPPING_LIST_ID, shoppingListOnClick.getId());
            intent.putExtra(SHOPPING_LIST_NAME, shoppingListOnClick.getName());
            startActivity(intent);

        }, shoppingListOnLongClick -> {
            Timber.d("LONG          KLiKNUOOOOOO" );
            //TODO: implenet Multi Select Feature
        }, shoppingListOnButtonClick -> {
            Timber.d("Button Clicked and ID is: " +shoppingListOnButtonClick.getId() );
            shoppingListNavigationController.navigateToRenameShoppingList(shoppingListOnButtonClick.getId());
        });
        binding.get().shoppingListRecycler.setAdapter(shoppingListAdapter);
        this.adapter = new AutoClearedValue<>(this, shoppingListAdapter);
    }

    private void subscribeToUi(ShoppingListViewModel shoppingListViewModel) {
        // Update the list when the data changes
        shoppingListViewModel.getShoppingLists().observe(this, shoppingList -> {
            binding.get().setResource(shoppingList);
            //binding.executePendingBindings();
            Timber.d("Ceo objekat " + shoppingList);
            if (shoppingList.data == null) {
                Timber.d("It's NULL");
            } else {
                Timber.d("List Size: " + shoppingList.data.size());
                adapter.get().replace(shoppingList.data);
            }
        });

        //FAB-addNewShoppingList
        shoppingListViewModel.getAddShoppingListClick().observe(this, fabButton -> {
            shoppingListNavigationController.navigateToAddShoppingList();
        });
    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.menu_fragment_back_only, menu);
    }*/

}
