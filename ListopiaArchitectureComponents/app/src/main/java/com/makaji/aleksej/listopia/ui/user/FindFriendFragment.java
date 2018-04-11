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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.binding.FragmentDataBindingComponent;
import com.makaji.aleksej.listopia.data.vo.Status;
import com.makaji.aleksej.listopia.databinding.FragmentFriendFindBinding;
import com.makaji.aleksej.listopia.di.module.Injectable;
import com.makaji.aleksej.listopia.ui.common.OnFragmentToolbarInteraction;
import com.makaji.aleksej.listopia.util.AutoClearedValue;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 2/21/2018.
 */

public class FindFriendFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    UserNavigationController userNavigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<FragmentFriendFindBinding> binding;

    private OnFragmentToolbarInteraction onFragmentToolbarInteraction;

    private UserViewModel userViewModel;

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
        FragmentFriendFindBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_find,
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
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        binding.get().setViewModel(userViewModel);

        //Automatically focuse edit text and show keyboard
        binding.get().textEmail.requestFocus();
        showKeyboard();

        userViewModel.getErrorTextEmail().observe(this, error -> {
            Timber.d("Error: " + error);
            if (error == null) {
                binding.get().textInputLayoutEmail.setError(error);
            } else {
                binding.get().textInputLayoutEmail.setError(error);
                hideKeyboard();
                //Snackbar.make(getActivity().findViewById(R.id.drawer_layout), R.string.fragment_shopping_list_add_fail, Snackbar.LENGTH_LONG).show();
            }
        });

        userViewModel.getUserWithFriends().observe(this, user -> {
            Timber.d("getUserWithFriends() observer happen");
            if (user.status == Status.ERROR) {
                Timber.d("Error: " + user.message);
                Snackbar.make(getView(), user.message, Snackbar.LENGTH_LONG).show();
            } else {
                if (user.data == null) {
                    Timber.d("friend is NULL");
                } else {
                    Timber.d("Friend Size: " + user.data.getName());
                    //Timber.d("Friend Size: " + user.data.getFriends().size());
                }
                //userNavigationController.popBackStackMethod();
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        //Setup toolbar
        onFragmentToolbarInteraction.enableNavigationBackIcon();
        onFragmentToolbarInteraction.setToolbarTitle(R.string.toolbar_shopping_list_add);
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
        imm.hideSoftInputFromWindow(binding.get().textEmail.getWindowToken(), 0);
    }

}
