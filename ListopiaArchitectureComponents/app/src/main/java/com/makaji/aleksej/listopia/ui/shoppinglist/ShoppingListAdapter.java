package com.makaji.aleksej.listopia.ui.shoppinglist;

/**
 * Created by Aleksej on 12/20/2017.
 */

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.entity.ShoppingListWithProducts;
import com.makaji.aleksej.listopia.databinding.ItemShoppingListBinding;
import com.makaji.aleksej.listopia.ui.common.DataBoundListAdapter;

import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * A RecyclerView adapter for {@link ShoppingList} class.
 */
public class ShoppingListAdapter extends DataBoundListAdapter<ShoppingListWithProducts, ItemShoppingListBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final ShoppingListClickCallback shoppingListClickCallback;
    private final ShoppingListLongClickCallback shoppingListLongClickCallback;
    private final ShoppingListOptionsClickCallback shoppingListOptionsClickCallback;

    public ShoppingListAdapter(DataBindingComponent dataBindingComponent,
                               ShoppingListClickCallback shoppingListClickCallback, ShoppingListLongClickCallback shoppingListLongClickCallback, ShoppingListOptionsClickCallback shoppingListOptionsClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.shoppingListClickCallback = shoppingListClickCallback;
        this.shoppingListLongClickCallback = shoppingListLongClickCallback;
        this.shoppingListOptionsClickCallback = shoppingListOptionsClickCallback;
    }

    @Override
    protected ItemShoppingListBinding createBinding(ViewGroup parent) {
        ItemShoppingListBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_shopping_list,
                        parent, false, dataBindingComponent);

        //binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(view -> {
            ShoppingListWithProducts shoppingList = binding.getShoppingListWithProducts();
            if (shoppingList != null && shoppingListClickCallback != null) {
                shoppingListClickCallback.onClick(shoppingList);
            }
        });
        binding.getRoot().setOnLongClickListener(view -> {
            ShoppingListWithProducts shoppingList = binding.getShoppingListWithProducts();
            if (shoppingList != null && shoppingListLongClickCallback != null) {
                shoppingListLongClickCallback.onLongClick(shoppingList);
            }
            return true;
        });
        //Options Click
        binding.buttonOptions.setOnClickListener(view -> {
            //setupPopupMenu(view, binding);
            ShoppingListWithProducts shoppingList = binding.getShoppingListWithProducts();
            if (shoppingList != null && shoppingListOptionsClickCallback != null) {
                shoppingListOptionsClickCallback.onOptionsClick(shoppingList, view);
            }
        });

        return binding;
    }

    @Override
    protected void bind(ItemShoppingListBinding binding, ShoppingListWithProducts item) {
        binding.setShoppingListWithProducts(item);
        //setup text progress
        int numberOfCheckedItems = countCheckedProducts(item);
        binding.textMaxProducts.setText("" + item.products.size());
        binding.textCheckedProducts.setText("" + numberOfCheckedItems);
        //setup progressBar
        binding.progressBar.setMax(item.products.size());
        binding.progressBar.setProgress(numberOfCheckedItems);
    }

    @Override
    protected boolean areItemsTheSame(ShoppingListWithProducts oldItem, ShoppingListWithProducts newItem) {
        return oldItem.shoppingList.getId() == newItem.shoppingList.getId();
        //Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(ShoppingListWithProducts oldItem, ShoppingListWithProducts newItem) {
        return Objects.equals(oldItem.shoppingList.getName(), newItem.shoppingList.getName()) &&
                Objects.equals(oldItem.products, newItem.products);

    }

    public interface ShoppingListClickCallback {
        void onClick(ShoppingListWithProducts shoppingList);
    }

    public interface ShoppingListLongClickCallback {
        void onLongClick(ShoppingListWithProducts shoppingList);
    }

    public interface ShoppingListOptionsClickCallback {
        void onOptionsClick(ShoppingListWithProducts shoppingList, View view);
    }

    //Logic has been transfered to fragment
   /* private void setupPopupMenu(View view, ItemShoppingListBinding binding) {
        //creating a popup menu
        PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), view);
        //inflating menu from xml resource
        popup.inflate(R.menu.popup_menu_item_shopping_list);
        //adding click listener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_rename_shopping_list:

                        ShoppingListWithProducts shoppingList = binding.getShoppingListWithProducts();
                        if (shoppingList != null && shoppingListButtonClickCallback != null) {
                            shoppingListButtonClickCallback.onButtonClick(shoppingList);
                        }
                        break;
                    case R.id.menu_delete_shopping_list:
                        //handle menu2 click
                        break;
                    case R.id.menu_share_shopping_list:
                        //handle menu3 click
                        break;
                    case R.id.menu_copy_shopping_list:
                        //handle menu4 click
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }*/

    /**
     * Count checked items in a shoppingList
     * @param binding
     * @return number of checked items
     */
    public int countCheckedProducts(ShoppingListWithProducts binding) {
        int productNumber = 0;
        for (Product product : binding.products) {
            if (product.getChecked())
                productNumber++;
        }
        return productNumber;
    }
}
