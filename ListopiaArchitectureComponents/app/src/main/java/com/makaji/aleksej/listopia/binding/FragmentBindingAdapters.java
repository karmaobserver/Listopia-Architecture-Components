package com.makaji.aleksej.listopia.binding;

/**
 * Created by Aleksej on 1/4/2018.
 */

import android.databinding.BindingAdapter;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import javax.inject.Inject;

/**
 * Binding adapters that work with a fragment instance.
 */
public class FragmentBindingAdapters {
    final Fragment fragment;

    @Inject
    public FragmentBindingAdapters(Fragment fragment) {
        this.fragment = fragment;
    }

    @BindingAdapter("imageUrl")
    public void bindImage(ImageView imageView, String url) {

        Glide.with(fragment).load(url).into(imageView);

    }

    @BindingAdapter("imageCircleUrl")
    public void bindImageCircle(ImageView imageView, String url) {

        Glide.with(fragment).load(url).apply(new RequestOptions().circleCrop()).into(imageView);
    }
}
