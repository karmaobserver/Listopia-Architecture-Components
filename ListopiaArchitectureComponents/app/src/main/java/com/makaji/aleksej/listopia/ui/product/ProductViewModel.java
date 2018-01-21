package com.makaji.aleksej.listopia.ui.product;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.makaji.aleksej.listopia.data.entity.Product;
import com.makaji.aleksej.listopia.data.entity.ShoppingList;
import com.makaji.aleksej.listopia.data.repository.ProductRepository;
import com.makaji.aleksej.listopia.data.repository.ShoppingListRepository;
import com.makaji.aleksej.listopia.data.vo.Resource;
import com.makaji.aleksej.listopia.util.AbsentLiveData;
import com.makaji.aleksej.listopia.util.SingleLiveData;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Aleksej on 1/14/2018.
 */

public class ProductViewModel extends ViewModel {

    private final LiveData<Resource<List<Product>>> products;
    private final LiveData<Resource<Product>> product;
    final MutableLiveData<Integer> shoppingListId = new MutableLiveData<>();
    final MutableLiveData<Integer> productId = new MutableLiveData<>();
    private final SingleLiveData<Void> addProductClick = new SingleLiveData<>();
    private final SingleLiveData<Void> renameProductClick = new SingleLiveData<>();
    public final MutableLiveData<String> textProductName = new MutableLiveData<>();
    public final MutableLiveData<Float> textProductQuantity = new MutableLiveData<>();
    public final MutableLiveData<String> textProductUnit = new MutableLiveData<>();
    public final MutableLiveData<Float> textProductPrice = new MutableLiveData<>();
    public final MutableLiveData<String> textProductNotes = new MutableLiveData<>();

    public final MutableLiveData<String> errorTextListName = new MutableLiveData<>();


    @Inject
    ProductRepository productRepository;

    @Inject
    public ProductViewModel(ProductRepository productRepository) {
        Timber.d("ProductViewModel prodict/products");
        products = Transformations.switchMap(shoppingListId, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return productRepository.getProductsByShoppingListId(id);
            }
        });
        product = Transformations.switchMap(productId, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return productRepository.findProductById(id);
            }
        });
    }

    public LiveData<Resource<List<Product>>> getProducts() {
        return products;
    }

    public LiveData<Resource<Product>> getProduct() {
        return product;
    }

    //Set id to get shoppingList (call getProductsByShoppingListId)
    public void setId(Integer id) {
        if (Objects.equals(shoppingListId.getValue(), id)) {
            Timber.d("SAME ID!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        Timber.d("Setting ID: " + id);
        shoppingListId.setValue(id);
    }

    //Set id to get product (call getProductById)
    public void setProductId(Integer id) {
        if (Objects.equals(shoppingListId.getValue(), id)) {
            Timber.d("SAME ID!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        Timber.d("Setting ID: " + id);
        productId.setValue(id);
    }

    //FAB click event
    public void onAddProductClick() {
        addProductClick.call();
    }
    //Get FAB click event
    public SingleLiveData<Void> getAddProductClick() {
        return addProductClick;
    }


    //Create shoppingList click event
    public void onCreateProductClick() {
       /* if (!validateCreateForm()) {
            return;
        }*/

       //create product
        Product product = new Product();
        product.setShoppingListId(shoppingListId.getValue());
        product.setName(textProductName.getValue());
        product.setPrice(textProductPrice.getValue());
        product.setQuantity(textProductQuantity.getValue());
        product.setUnit(textProductUnit.getValue());
        product.setNotes(textProductNotes.getValue());
        product.setChecked(false);

        productRepository.insertProduct(product);

        addProductClick.call();
    }

    //Get create shoppingList click event
    public LiveData<Void> getCreateProductClick() {
        return addProductClick;
    }

    //Get error
    public LiveData<String> getErrorTextListName() {
        return errorTextListName;
    }

    public void deleteAllProducts() {
        productRepository.deleteAllProducts();
    }

    //Rename product click event
    public void onRenameProductClick(Product product) {
        /*if (!validateRenameForm()) {
            return;
        }*/
        productRepository.updateProduct(product);
        renameProductClick.call();
    }

    //Get rename product click event
    public LiveData<Void> getRenameProductClick() {
        return renameProductClick;
    }
}
