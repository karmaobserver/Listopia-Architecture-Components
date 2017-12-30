package com.makaji.aleksej.listopia.ui.common;

/**
 * Created by Aleksej on 12/20/2017.
 */

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * A generic ViewHolder that works with a {@link ViewDataBinding}.
 * @param <T> The type of the ViewDataBinding.
 */
public class DataBoundViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    public final T binding;
    DataBoundViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
