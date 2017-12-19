package com.makaji.aleksej.listopia.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

/**
 * Created by Aleksej on 12/15/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
