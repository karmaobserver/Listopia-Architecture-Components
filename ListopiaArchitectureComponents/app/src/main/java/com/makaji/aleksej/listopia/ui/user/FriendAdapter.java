package com.makaji.aleksej.listopia.ui.user;

/**
 * Created by Aleksej on 2/20/2018.
 */

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.User;
import com.makaji.aleksej.listopia.databinding.ItemFriendBinding;
import com.makaji.aleksej.listopia.ui.common.DataBoundListAdapter;

import java.util.Objects;

import timber.log.Timber;

/**
 * A RecyclerView adapter for {@link User} class.
 */
public class FriendAdapter extends DataBoundListAdapter<User, ItemFriendBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final UserClickCallback userClickCallback;

    private SharedPreferences sharedPreferences;
    private Resources resources;

    public FriendAdapter(DataBindingComponent dataBindingComponent, UserClickCallback userClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.userClickCallback = userClickCallback;
    }

    @Override
    protected ItemFriendBinding createBinding(ViewGroup parent) {
        ItemFriendBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_friend,
                        parent, false, dataBindingComponent);

        binding.getRoot().setOnClickListener(view -> {
            User user = binding.getUser();
            if (user != null && userClickCallback != null) {
                Timber.d("UserAdapter getCheked" + user.getName());
                userClickCallback.onClick(user);
            }
        });

        return binding;
    }

    @Override
    protected void bind(ItemFriendBinding binding, User item) {
        binding.setUser(item);
    }

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
                //oldItem.getId() == newItem.getId();
        //Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName()) &&
                Objects.equals(oldItem.getEmail(), newItem.getEmail()) &&
                Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl());
                //TODO: add what has left to be added   FRIENDS LiST
    }

    public interface UserClickCallback {
        void onClick(User user);
    }

}
