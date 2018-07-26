package com.richardgrable.budgettracker.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.richardgrable.budgettracker.R;
import com.richardgrable.budgettracker.fragments.AbstractFragment;
import com.richardgrable.budgettracker.fragments.BudgetFragment;
import com.richardgrable.budgettracker.fragments.BudgetItemFragment;
import com.richardgrable.budgettracker.fragments.IFragment;
import com.richardgrable.budgettracker.fragments.LoginFragment;

public class FragmentController {

    private static final String TAG = "FragmentController";
    public enum FragmentTypes {
        None,
        Login,
        Budget,
        BudgetItems,
        Account
    }

    private Activity activity;
    private FragmentManager fragmentManager;
    private FragmentTypes curFragmentType;
    private AbstractFragment curFragment;

    private FloatingActionButton addFab;

    public FragmentController(Activity activity) {
        this.activity = activity;
        this.curFragmentType = FragmentTypes.None;
        fragmentManager = this.activity.getFragmentManager();

        addFab = this.activity.findViewById(R.id.fab_add);
    }

    public void goBack() {
        switch (curFragmentType) {
            case BudgetItems:
                switchFragment(FragmentTypes.Budget);
                break;
            default:
                activity.finish();
                break;
        }
    }

    public void showFab() {
        addFab.setVisibility(View.VISIBLE);
    }

    public void hideFab() {
        addFab.setVisibility(View.GONE);
    }

    public void setAddFabListener(View.OnClickListener listener) {
        addFab.setOnClickListener(listener);
    }

    public void switchFragment(FragmentTypes next) {
        switchFragment(next, null);
    }

    public void switchFragment(FragmentTypes next, @Nullable Bundle bundle) {
        switch (next) {
            case Login:
                curFragment = new LoginFragment();
                hideFab();
                break;
            case Budget:
                curFragment = new BudgetFragment();
                showFab();
                break;
            case BudgetItems:
                curFragment = new BudgetItemFragment();
                showFab();
                break;
            default:
                break;
        }

        assert curFragment != null;
        curFragment.setArguments(bundle);
        curFragment.initialize(this);
        transition();
        curFragmentType = next;
    }

    @SuppressLint("ResourceType")
    private void transition() {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (curFragmentType == FragmentTypes.None) {
            fragmentTransaction.add(R.id.layout_container, curFragment, curFragment.tag());
        } else {
            fragmentTransaction.replace(R.id.layout_container, curFragment, curFragment.tag());
        }
        fragmentTransaction.commit();
    }
}
