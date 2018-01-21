package com.makaji.aleksej.listopia.ui.product;

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
import android.view.View;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.databinding.FragmentProductBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListAdapter;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListNavigationController;
import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

public class ProductFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    ProductNavigationController productNavigationController;

    private static final String SHOPPING_LIST_ID = "shopping_list_id";
    private static final String SHOPPING_LIST_NAME = "shopping_list_name";

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private ProductViewModel productViewModel;

    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentProductBinding> binding;
    private AutoClearedValue<ProductAdapter> adapter;



    public static ProductFragment create(Integer id, String name) {
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SHOPPING_LIST_ID, id);
        bundle.putString(SHOPPING_LIST_NAME, name);
        productFragment.setArguments(bundle);
        return productFragment;
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
        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentProductBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product,
                container, false, dataBindingComponent);

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

            subscribeToFab(productViewModel, id);

        }

        setProductCallBacks();

        subscribeToUi(productViewModel);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Set back navigation to toolbar
        String name = getActivity().getIntent().getExtras().getString(SHOPPING_LIST_NAME);
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(name);
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
    private void setProductCallBacks() {
        ProductAdapter productAdapter = new ProductAdapter(dataBindingComponent, productOnClick -> {
            Timber.d("KLiKNUOOOOOO" );

        }, productOnLongClick -> {
            Timber.d("LONG          KLiKNUOOOOOO" );
            //TODO: implenet Multi Select Feature
        }, productOnProductEditClick -> {
            Timber.d("Button Clicked and ID is: " +productOnProductEditClick.getId() );
            productNavigationController.navigateToRenameProduct(productOnProductEditClick.getId());
        });
        binding.get().productRecycler.setAdapter(productAdapter);
        this.adapter = new AutoClearedValue<>(this, productAdapter);
    }
    private void subscribeToUi(ProductViewModel productViewModel) {
        // Update the list when the data changes
        productViewModel.getProducts().observe(this, product -> {
            binding.get().setResource(product);
            //binding.executePendingBindings();
            Timber.d("Ceo objekat " + product);
            if (product.data == null) {
                Timber.d("It's NULL");
            } else {
                Timber.d("List Size: " + product.data.size());
                adapter.get().replace(product.data);
            }
        });
    }

    private void subscribeToFab(ProductViewModel productViewModel, Integer id) {
        //FAB-addNewProduct
        productViewModel.getAddProductClick().observe(this, fabButton -> {
            productNavigationController.navigateToAddProduct(id);
        });
    }
}
