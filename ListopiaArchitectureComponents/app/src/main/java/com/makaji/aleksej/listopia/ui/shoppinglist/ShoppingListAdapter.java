package com.makaji.aleksej.listopia.ui.shoppinglist;

/**
 * Created by Aleksej on 12/20/2017.
 */

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.databinding.ItemShoppingListBinding;
import com.makaji.aleksej.listopia.ui.common.DataBoundListAdapter;

import java.util.Objects;

import timber.log.Timber;


/**
 * A RecyclerView adapter for {@link ShoppingList} class.
 */
public class ShoppingListAdapter extends DataBoundListAdapter<ShoppingList, ItemShoppingListBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final ShoppingListClickCallback shoppingListClickCallback;
    private final ShoppingListLongClickCallback shoppingListLongClickCallback;
    private final ShoppingListButtonClickCallback shoppingListButtonClickCallback;

    public ShoppingListAdapter(DataBindingComponent dataBindingComponent,
                               ShoppingListClickCallback shoppingListClickCallback, ShoppingListLongClickCallback shoppingListLongClickCallback, ShoppingListButtonClickCallback shoppingListButtonClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.shoppingListClickCallback = shoppingListClickCallback;
        this.shoppingListLongClickCallback = shoppingListLongClickCallback;
        this.shoppingListButtonClickCallback = shoppingListButtonClickCallback;
    }



    @Override
    protected ItemShoppingListBinding createBinding(ViewGroup parent) {
        ItemShoppingListBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shopping_list,
                        parent, false, dataBindingComponent);
        //binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(view -> {
            ShoppingList shoppingList = binding.getShoppingList();
            if (shoppingList != null && shoppingListClickCallback != null) {
                shoppingListClickCallback.onClick(shoppingList);
            }
        });
        binding.getRoot().setOnLongClickListener(view -> {
            ShoppingList shoppingList = binding.getShoppingList();
            if (shoppingList != null && shoppingListLongClickCallback != null) {
                shoppingListLongClickCallback.onLongClick(shoppingList);
            }
            return true;
        });

        binding.button.setOnClickListener(view -> {
            ShoppingList shoppingList = binding.getShoppingList();
            if (shoppingList != null && shoppingListButtonClickCallback != null) {
                shoppingListButtonClickCallback.onButtonClick(shoppingList);
            }
        });
        return binding;
    }

    @Override
    protected void bind(ItemShoppingListBinding binding, ShoppingList item) {
        binding.setShoppingList(item);
    }

    @Override
    protected boolean areItemsTheSame(ShoppingList oldItem, ShoppingList newItem) {
        return oldItem.getId() == newItem.getId();
        //Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(ShoppingList oldItem, ShoppingList newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName());

    }

    public interface ShoppingListClickCallback {
        void onClick(ShoppingList shoppingList);
    }

    public interface ShoppingListLongClickCallback {
        void onLongClick(ShoppingList shoppingList);
    }

    public interface ShoppingListButtonClickCallback {
        void onButtonClick(ShoppingList shoppingList);
    }
}
