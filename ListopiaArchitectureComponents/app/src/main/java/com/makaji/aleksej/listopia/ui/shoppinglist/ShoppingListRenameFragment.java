package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.databinding.FragmentShoppingListRenameBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/12/2018.
 */

public class ShoppingListRenameFragment extends Fragment implements Injectable {

    private static final String ID_KEY = "id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ShoppingListNavigationController shoppingListNavigationController;

    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentShoppingListRenameBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private ShoppingListViewModel shoppingListViewModel;

    public static ShoppingListRenameFragment create(int id) {
        ShoppingListRenameFragment shoppingListRenameFragment = new ShoppingListRenameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID_KEY, id);
        shoppingListRenameFragment.setArguments(bundle);
        return shoppingListRenameFragment;
    }

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentShoppingListRenameBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list_rename,
                container, false, dataBindingComponent);
        //Bind listeners
        //....
        //Clear values
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);
        binding.get().setViewModel(shoppingListViewModel);

        //Automatically focuse edit text and show keyboard
        binding.get().textListName.requestFocus();
        showKeyboard();

        Bundle args = getArguments();
        if (args != null && args.containsKey(ID_KEY)) {
            int id = args.getInt(ID_KEY);
            Timber.d("ID is: " + id);

            if (savedInstanceState == null) {
                Timber.d("Saved Instace je NULL, TREBA DA POZOVEM METOD");
                shoppingListViewModel.setId(id);
            }



        } else {
            Timber.d("ARGS je NULL ili ne sadrzi ID_KEY");
        }

        shoppingListViewModel.getShoppingList().observe(this, shoppingListResource -> {
            Timber.d("shoppingList OBSERVING: " + shoppingListResource);
            if (shoppingListResource.data == null) {
                Timber.d("Shopping List je NULL");
            } else {
                Timber.d("shoppingList name je: " + shoppingListResource.data.getName());
                binding.get().setShoppingList(shoppingListResource.data);
            }
        });

        shoppingListViewModel.getErrorTextListName().observe(this, error -> {
            Timber.d("Error: " + error);
            if (error == null) {
                binding.get().textInputLayoutListName.setError(error);
            } else {
                binding.get().textInputLayoutListName.setError(error);
                hideKeyboard();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.fragment_shopping_list_add_fail, Snackbar.LENGTH_LONG).show();
            }
        });

        shoppingListViewModel.getRenameShoppingListClick().observe(this, renameButton -> {
            //shoppingListNavigationController.navigateToShoppingList();
            shoppingListNavigationController.popBackStackMethod();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Setup toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_shopping_list_rename);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideKeyboard();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentToolbarInteraction = null;
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Timber.d("Saved Instance STATE has been called");
        outState.putBoolean("isFirstTime", true);
     }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menu.clear();
        menuInflater.inflate(R.menu.menu_fragment_back_only, menu);
    }

    public void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.get().textListName.getWindowToken(), 0);
    }

}
