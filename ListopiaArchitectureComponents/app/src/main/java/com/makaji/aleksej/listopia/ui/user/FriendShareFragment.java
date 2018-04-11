package com.makaji.aleksej.listopia.ui.user;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.databinding.FragmentFriendShareBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;

import com.makaji.aleksej.listopia.ui.shoppinglist.ShoppingListViewModel;
import com.makaji.aleksej.listopia.util.AutoClearedValue;
import com.negusoft.compountadapter.recyclerview.AdapterGroup;
import com.negusoft.compountadapter.recyclerview.SingleAdapter;


import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 2/20/2018.
 */

public class FriendShareFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    UserNavigationController userNavigationController;

    @Inject
    SharedPreferences sharedPreferences;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private UserViewModel userViewModel;
    private ShoppingListViewModel shoppingListViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentFriendShareBinding> binding;
    private AutoClearedValue<FriendAdapter> adapter;
    private AutoClearedValue<FriendAdapter> adapterFriendShare;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentFriendShareBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_share,
                container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        binding.get().setViewModel(userViewModel);

        shoppingListViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShoppingListViewModel.class);

        /*Bundle args = getArguments();
        if (args != null && args.containsKey(SHOPPING_LIST_ID)) {
            int id = args.getInt(SHOPPING_LIST_ID);
            Timber.d("ID u productFragment is: " + id);
            productViewModel.setId(id);

            subscribeToFab(productViewModel, id);

        }*/

        setFriendCallBacks();

        subscribeToUi(userViewModel, shoppingListViewModel);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Set back navigation to toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_friend_share);
        //onFragmentToolbarInteraction.enableNavigationDrawerIcon();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentToolbarInteraction = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.menu_fragment_friend_share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                //It has submenu, in case we need logic when settings has been clicked, we write here
                return true;
            case R.id.submenu_settings_delete_all:
                //Delete all friends and observe error in case something happen during deleting all friends
                userViewModel.deleteAllFriends().observe(this, error -> {
                    Timber.d("Observing DeleteAllFriends Error:  " + error);
                    binding.get().setResource(error);
                    if (error.data != null) {
                        Snackbar.make(getView(), error.data, Snackbar.LENGTH_LONG).show();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFriendCallBacks() {
        FriendAdapter friendAdapter = new FriendAdapter(dataBindingComponent, friendOnClick -> {

            Timber.d("Friend has been Clicked!" + friendOnClick.getName());
            /*Intent intent = new Intent(getActivity(), ProductActivity.class);
            intent.putExtra(SHOPPING_LIST_ID, shoppingListOnClick.shoppingList.getId());
            intent.putExtra(SHOPPING_LIST_NAME, shoppingListOnClick.shoppingList.getName());
            startActivity(intent);*/

        });

        AdapterGroup adapterGroupFriends = new AdapterGroup();
        adapterGroupFriends.addAdapter(SingleAdapter.create(R.layout.header_adapter_friend));
        adapterGroupFriends.addAdapter(friendAdapter);

        binding.get().friendRecycler.setAdapter(adapterGroupFriends);
        this.adapter = new AutoClearedValue<>(this, friendAdapter);



    }

    public void subscribeToUi(UserViewModel userViewModel, ShoppingListViewModel shoppingListViewModel) {

        String resUserId = getResources().getString(R.string.key_user_id);
        String userId = sharedPreferences.getString(resUserId, "defualtValue");
        userViewModel.setId(userId);

        userViewModel.getUser().observe(this, user -> {
            Timber.d("Observing User " + user);
            binding.get().setResource(user);
            //binding.executePendingBindings();

            if (user.data == null) {
                Timber.d("User is NULL");
                adapter.get().replace(null);
            } else {
                adapter.get().replace(user.data.getFriends());
            }
        });

        //Button-findFriend
        userViewModel.getFindFriendClick().observe(this, findFriendButton -> {
            userNavigationController.navigateToFindFriend();
        });

        shoppingListViewModel.getShoppingList().observe(this, shoppingList -> {
            Timber.d("Observing ShoppingList " + shoppingList);
            binding.get().setResource(shoppingList);
            //binding.executePendingBindings();

            if (shoppingList.data == null) {
                Timber.d("shoppingList is NULL");
                adapterFriendShare.get().replace(null);
            } else {
                Timber.d("shoppingList is NOT NULL");
                adapterFriendShare.get().replace(shoppingList.data.getFriendsWhoShare());
            }
        });

    }
}
