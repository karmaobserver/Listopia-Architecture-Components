package com.makaji.aleksej.listopia.ui.product;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.makaji.aleksej.listopia.databinding.FragmentProductAddBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListNavigationController;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/15/2018.
 */

public class ProductAddFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProductNavigationController productNavigationController;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Resources resources;

    private static final String SHOPPING_LIST_ID = "shopping_list_id";

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProductAddBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private ProductViewModel productViewModel;

    public static ProductAddFragment create(Integer id) {
        ProductAddFragment productAddFragment = new ProductAddFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SHOPPING_LIST_ID, id);
        productAddFragment.setArguments(bundle);
        return productAddFragment;
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
        FragmentProductAddBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_add,
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
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);
        binding.get().setViewModel(productViewModel);

        Bundle args = getArguments();
        if (args != null && args.containsKey(SHOPPING_LIST_ID)) {
            int id = args.getInt(SHOPPING_LIST_ID);
            Timber.d("ID u productFragment is: " + id);
            productViewModel.setId(id);
        }

        //Set currency from sharedPreferences
        String currency = sharedPreferences.getString(resources.getString(R.string.key_currency), "");
        binding.get().textCurrency.setText(currency);

        //Automatically focuse edit text and show keyboard
        binding.get().editName.requestFocus();
        showKeyboard();

        productViewModel.getErrorTextListName().observe(this, error -> {
            Timber.d("Error: " + error);
            if (error == null) {
                binding.get().textInputLayoutName.setError(error);
            } else {
                binding.get().textInputLayoutName.setError(error);
                hideKeyboard();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.fragment_shopping_list_add_fail, Snackbar.LENGTH_LONG).show();
            }
        });

        productViewModel.getCreateProductClick().observe(this, createProductButton -> {
            //shoppingListNavigationController.navigateToShoppingList();
            productNavigationController.popBackStackMethod();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Setup toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_product_add);
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
        imm.hideSoftInputFromWindow(binding.get().editName.getWindowToken(), 0);
    }

}

