package com.makaji.aleksej.listopia.ui.shoppinglist;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingListWithProducts;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.databinding.ItemProductBinding;
import com.makaji.aleksej.listopia.databinding.ItemShoppingListFriendsShareBinding;
import com.makaji.aleksej.listopia.ui.common.DataBoundListAdapter;

import java.util.Objects;

import timber.log.Timber;

/**
 * Created by Aleksej on 3/28/2018.
 */


/**
 * A RecyclerView adapter for {@link User} class.
 */
public class ShoppingListFriendsShareAdapter extends DataBoundListAdapter<User, ItemShoppingListFriendsShareBinding> {
    private final DataBindingComponent dataBindingComponent;

    public ShoppingListFriendsShareAdapter(DataBindingComponent dataBindingComponent) {
        this.dataBindingComponent = dataBindingComponent;
    }

    @Override
    protected ItemShoppingListFriendsShareBinding createBinding(ViewGroup parent) {
        ItemShoppingListFriendsShareBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shopping_list_friends_share,
                        parent, false, dataBindingComponent);

        return binding;
    }

    @Override
    protected void bind(ItemShoppingListFriendsShareBinding binding, User item) {
        binding.setUser(item);
    }

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName()) &&
                Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl());
    }
}

