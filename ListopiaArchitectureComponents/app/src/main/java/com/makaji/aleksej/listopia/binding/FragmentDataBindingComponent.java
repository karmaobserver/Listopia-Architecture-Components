package com.makaji.aleksej.listopia.binding;

import android.databinding.DataBindingComponent;
import android.support.v4.app.Fragment;
/**
 * Created by Aleksej on 1/4/2018.
 */

/**
 * A Data Binding Component implementation for fragments.
 */
public class FragmentDataBindingComponent implements DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
