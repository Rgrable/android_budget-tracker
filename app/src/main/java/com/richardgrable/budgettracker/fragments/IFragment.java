package com.richardgrable.budgettracker.fragments;

import android.app.Activity;
import android.app.Fragment;

import com.richardgrable.budgettracker.controllers.FragmentController;

public interface IFragment {
    void initialize(FragmentController controller);
    void show();
    void hide();
    String tag();
    Fragment fragment();
}
