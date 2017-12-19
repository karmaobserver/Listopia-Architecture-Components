package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.di.module.Injectable;

import javax.inject.Inject;

/**
 * Created by Aleksej on 12/18/2017.
 */

public class MyFragment extends LifecycleFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    public static MyFragment create() {
        MyFragment myFragment = new MyFragment();
        //Bundle bundle = new Bundle();
        //bundle.putString(LOGIN_KEY, login);
       // myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        return view;
    }

    /*@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }*/


}
