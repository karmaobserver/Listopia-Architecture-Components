package com.makaji.aleksej.listopia.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by Aleksej on 1/14/2018.
 */

public class ShoppingListWithProducts {
    @Embedded
    public ShoppingList shoppingList;

    @Relation(parentColumn = "id", entityColumn = "shoppingListId", entity = Product.class)
    public List<Product> products; // or use simply 'List pets;'


   /* Alternatively you can use projection to fetch a specific column (i.e. only name of the pets) from related Pet table. You can uncomment and try below;

   @Relation(parentColumn = "id", entityColumn = "userId", entity = Pet.class, projection = "name")
   public List<String> pets;
   */
}
