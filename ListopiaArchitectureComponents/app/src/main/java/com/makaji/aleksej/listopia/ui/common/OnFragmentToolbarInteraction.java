package com.makaji.aleksej.listopia.ui.common;

/**
 * Created by Aleksej on 1/4/2018.
 */

/**
 * Interface which activity implements so we can change navigation icon, title... from fragments
 */
public interface OnFragmentToolbarInteraction {
    void enableNavigationBackIcon();
    void setToolbarTitle(int resId);
    void setToolbarTitle(String title);
    void enableNavigationDrawerIcon();
}
