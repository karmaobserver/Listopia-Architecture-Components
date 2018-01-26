package com.makaji.aleksej.listopia.binding;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.TextView;

import timber.log.Timber;
/**
 * Created by Aleksej on 12/30/2017.
 */

/**
 * Data Binding adapters specific to the app.
 */
public class BindingAdapters {
    @BindingAdapter("visibilityGone")
    public static void hideShow(View view, boolean hide) {
        view.setVisibility(hide ? View.GONE : View.VISIBLE);
    }
    //For float value in edit text
    @BindingAdapter("android:text")
    public static void setFloat(TextInputEditText view, float value) {
        //Check if it is undefine
        if (Float.isNaN(value)) {
            view.setText("");
            //check if value at start 0.0F
        } else if (value == 0.0F) {
            view.setText("");
        } else {
            //Timber.d("Number is: " + value);
            double remains = value - Math.floor(value);
            // Timber.d("Remains is: " + remains);
            //if remains equal to 0, that mean we need to show Integer
            if (remains == 0) {
                view.setText(""+(int) value);
            } else {
                view.setText("" + value);
            }
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloat(TextInputEditText view) {
        String num = view.getText().toString();
        if(num.isEmpty()) return 0.0F;
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            return 0.0F;
        }
    }

    //For float value in edit text
    @BindingAdapter("android:text")
    public static void setFloat(TextView view, float value) {
        //Check if it is undefine
        if (Float.isNaN(value)) {
            view.setText("");
            //check if value at start 0.0F
        } else if (value == 0.0F) {
            view.setText("");
        } else {
            //Timber.d("Number is: " + value);
            double remains = value - Math.floor(value);
            //Timber.d("Remains is: " + remains);
            //if remains equal to 0, that mean we need to show Integer
            if (remains == 0) {
                view.setText(""+ (int) value);
            } else {
                view.setText(""+ value);
            }
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloat(TextView view) {
        String num = view.getText().toString();
        if(num.isEmpty()) return 0.0F;
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            return 0.0F;
        }
    }

    //For float value in edit text
  /*  @BindingAdapter("android:text")
    public static void setFloat(TextInputEditText view, float value) {
        if (Float.isNaN(value)) view.setText("");
        else view.setText( "Float value " + value );
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static float getFloat(TextInputEditText view) {
        String num = view.getText().toString();
        if(num.isEmpty()) return 0.0F;
        try {
            return Float.parseFloat(num);
        } catch (NumberFormatException e) {
            return 0.0F;
        }
    }*/

}
