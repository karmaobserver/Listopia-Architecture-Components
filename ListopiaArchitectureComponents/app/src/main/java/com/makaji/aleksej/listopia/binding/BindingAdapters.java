package com.makaji.aleksej.listopia.binding;

import android.databinding.BindingAdapter;
import android.view.View;
/**
 * Created by Aleksej on 12/30/2017.
 */

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
