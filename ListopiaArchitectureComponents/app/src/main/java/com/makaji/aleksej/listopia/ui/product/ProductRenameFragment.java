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
import com.makaji.aleksej.listopia.databinding.FragmentProductRenameBinding;
import com.makaji.aleksej.listopia.databinding.FragmentShoppingListRenameBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListNavigationController;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/21/2018.
 */

public class ProductRenameFragment extends Fragment implements Injectable {

    private static final String ID_KEY = "id";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProductNavigationController productNavigationController;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Resources resources;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProductRenameBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private ProductViewModel productViewModel;

    public static ProductRenameFragment create(int id) {
        ProductRenameFragment productRenameFragment = new ProductRenameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ID_KEY, id);
        productRenameFragment.setArguments(bundle);
        return productRenameFragment;
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
        FragmentProductRenameBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_rename,
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

        //Set currency from sharedPreferences
        String currency = sharedPreferences.getString(resources.getString(R.string.key_currency), "");
        binding.get().textCurrency.setText(currency);

        //Automatically focuse edit text and show keyboard
        binding.get().editName.requestFocus();
        showKeyboard();

        Bundle args = getArguments();
        if (args != null && args.containsKey(ID_KEY)) {
            int id = args.getInt(ID_KEY);
            Timber.d("ID is: " + id);

            if (savedInstanceState == null) {
                Timber.d("Saved Instace je NULL, TREBA DA POZOVEM METOD");
                productViewModel.setProductId(id);
            }

            productViewModel.getProduct().observe(this, productResource -> {
                Timber.d("product OBSERVING: " + productResource);
                if (productResource.data == null) {
                    Timber.d("Product je NULL");
                } else {
                    Timber.d("product name je: " + productResource.data.getName());
                    binding.get().setProduct(productResource.data);
                }
            });

        } else {
            Timber.d("ARGS je NULL ili ne sadrzi ID_KEY");
        }

      /*  shoppingListViewModel.getErrorTextListName().observe(this, error -> {
            Timber.d("Error: " + error);
            if (error == null) {
                binding.get().textInputLayoutListName.setError(error);
            } else {
                binding.get().textInputLayoutListName.setError(error);
                hideKeyboard();
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.fragment_shopping_list_add_fail, Snackbar.LENGTH_LONG).show();
            }
        });*/

        productViewModel.getRenameProductClick().observe(this, renameButton -> {
            productNavigationController.popBackStackMethod();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Setup toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_product_rename);
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
