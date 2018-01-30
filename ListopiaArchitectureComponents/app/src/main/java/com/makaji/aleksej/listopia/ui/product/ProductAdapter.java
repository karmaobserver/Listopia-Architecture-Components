package com.makaji.aleksej.listopia.ui.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.makaji.aleksej.listopia.R;
import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.databinding.ItemProductBinding;
import com.makaji.aleksej.listopia.ui.common.DataBoundListAdapter;

import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

/**
 * A RecyclerView adapter for {@link Product} class.
 */
public class ProductAdapter extends DataBoundListAdapter<Product, ItemProductBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final ProductClickCallback productClickCallback;
    private final ProductLongClickCallback productLongClickCallback;
    private final ProductEditClickCallback productEditClickCallback;

    private SharedPreferences sharedPreferences;
    private Resources resources;

    public ProductAdapter(DataBindingComponent dataBindingComponent,
                          ProductClickCallback productClickCallback, ProductLongClickCallback productLongClickCallback, ProductEditClickCallback productEditClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.productClickCallback = productClickCallback;
        this.productLongClickCallback = productLongClickCallback;
        this.productEditClickCallback = productEditClickCallback;
    }


    @Override
    protected ItemProductBinding createBinding(ViewGroup parent) {
        ItemProductBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.item_product,
                        parent, false, dataBindingComponent);

        //binding.setShowFullName(showFullName);
        binding.getRoot().setOnClickListener(view -> {
            Product product = binding.getProduct();
            if (product != null && productClickCallback != null) {
                Timber.d("ProductAdapter getCheked" + product.getChecked());
                if (!product.getChecked())
                    markItemAsChecked(binding);
                else
                    markItemAsUnchecked(binding);

                productClickCallback.onClick(product);
            }
        });
        binding.getRoot().setOnLongClickListener(view -> {
            Product product = binding.getProduct();
            if (product != null && productLongClickCallback != null) {
                productLongClickCallback.onLongClick(product);
            }
            return true;
        });
        //Edit product button click
        binding.buttonEditProduct.setOnClickListener(view -> {
            Product product = binding.getProduct();
            productEditClickCallback.onProductEditClick(product);
        });

        //Set currency text from shared preferences
        resources = binding.getRoot().getContext().getResources();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.getRoot().getContext());
        String resCurrency = resources.getString(R.string.key_currency);
        String currency = sharedPreferences.getString(resCurrency, "defaultStringIfNothingFound");
        Timber.d("currency is: " + currency);
        binding.textCurrency.setText(currency);

        return binding;
    }

    @Override
    protected void bind(ItemProductBinding binding, Product item) {
        binding.setProduct(item);
    }

    @Override
    protected boolean areItemsTheSame(Product oldItem, Product newItem) {
        return oldItem.getId() == newItem.getId();
        //Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    protected boolean areContentsTheSame(Product oldItem, Product newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName()) &&
               oldItem.getQuantity() == newItem.getQuantity() &&
                Objects.equals(oldItem.getUnit(), newItem.getUnit()) &&
               oldItem.getPrice() == newItem.getPrice() &&
                Objects.equals(oldItem.getNotes(), newItem.getNotes()) &&
                oldItem.getChecked() == newItem.getChecked();
                //TODO: add what has left to be added


    }

    public interface ProductClickCallback {
        void onClick(Product product);
    }

    public interface ProductLongClickCallback {
        void onLongClick(Product product);
    }

    public interface ProductEditClickCallback {
        void onProductEditClick(Product product);
    }

    /**Instant update UI, mark item as checked
     *
     * @param binding
     */
    public void markItemAsChecked(ItemProductBinding binding) {
        binding.textName.setPaintFlags(binding.textName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textQuantity.setPaintFlags(binding.textQuantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textUnit.setPaintFlags(binding.textUnit.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textNotes.setPaintFlags(binding.textName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textCurrency.setPaintFlags(binding.textCurrency.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textPrice.setPaintFlags(binding.textPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        binding.textName.setEnabled(false);
        binding.textQuantity.setEnabled(false);
        binding.textUnit.setEnabled(false);
        binding.textNotes.setEnabled(false);
        binding.textQuantity.setEnabled(false);
        //Change it
        binding.textCurrency.setEnabled(false);
        binding.textPrice.setEnabled(false);
}

    public void markItemAsUnchecked(ItemProductBinding binding) {
        binding.textName.setPaintFlags(binding.textName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textQuantity.setPaintFlags(binding.textQuantity.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textUnit.setPaintFlags(binding.textUnit.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textNotes.setPaintFlags(binding.textNotes.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textCurrency.setPaintFlags(binding.textCurrency.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textPrice.setPaintFlags(binding.textPrice.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        binding.textName.setEnabled(true);
        binding.textQuantity.setEnabled(true);
        binding.textUnit.setEnabled(true);
        binding.textNotes.setEnabled(true);
        binding.textQuantity.setEnabled(true);
        //Change it
        binding.textCurrency.setEnabled(true);
        binding.textPrice.setEnabled(true);


    }
}
