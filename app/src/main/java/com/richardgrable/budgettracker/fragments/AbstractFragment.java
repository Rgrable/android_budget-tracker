package com.richardgrable.budgettracker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.richardgrable.budgettracker.controllers.FragmentController;

public abstract class AbstractFragment extends Fragment {

    protected FragmentController controller;
    protected Bundle bundle;

    public void initialize(FragmentController controller) {
        this.controller = controller;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreate(view, savedInstanceState);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        this.bundle = args;
    }

    public abstract void onCreate(View view, Bundle savedInstanceState);
    public abstract String tag();
}
