package com.makaji.aleksej.listopia.data.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Aleksej on 1/14/2018.
 */

@Entity(tableName = "product")
public class Product {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    private int id;

    private int shoppingListId;

    @SerializedName("name")
    private String name;

    @SerializedName("quantity")
    private Float quantity;

    @SerializedName("unit")
    private String unit;

    @SerializedName("price")
    private Float price;

    @SerializedName("notes")
    private String notes;

    @SerializedName("is_checked")
    private Boolean isChecked;

    public Product(int id, int shoppingListId, String name, Float quantity, String unit, Float price, String notes, Boolean isChecked) {
        this.id = id;
        this.shoppingListId = shoppingListId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.notes = notes;
        this.isChecked = isChecked;
    }

    @Ignore
    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
