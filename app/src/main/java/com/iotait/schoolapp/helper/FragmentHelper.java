package com.iotait.schoolapp.helper;

import android.os.Bundle;
import android.view.View;

import androidx.navigation.Navigation;

public class FragmentHelper {
    public static void changeFragmet(View view, int action, Bundle bundle) {

        Navigation.findNavController(view).navigate(action, bundle);
    }
}
