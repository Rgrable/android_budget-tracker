package com.richardgrable.budgettracker.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.richardgrable.budgettracker.R;
import com.richardgrable.budgettracker.controllers.FragmentController;
import com.richardgrable.budgettracker.models.BudgetModel;
import com.richardgrable.budgettracker.models.ResponseModel;
import com.richardgrable.budgettracker.models.UserModel;
import com.richardgrable.budgettracker.services.WebService;
import com.richardgrable.budgettracker.singletons.DataSingleton;
import com.richardgrable.budgettracker.utils.LogUtil;
import com.richardgrable.budgettracker.views.BudgetCardView;
import com.richardgrable.budgettracker.views.PopupCreateBudget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BudgetFragment extends AbstractFragment {

    private ViewGroup container;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onCreate(View view, Bundle savedInstanceState) {
        controller.setAddFabListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEntry();
            }
        });
        container = view.findViewById(R.id.layout_container);
        userModel = DataSingleton.getInstance().getUserModel();

        fetchBudget();
    }

    @Override
    public String tag() {
        return "BudgetFragment";
    }

    private void fetchBudget() {
        container.removeAllViews();
        if (DataSingleton.getInstance().getIsDirty()) {
            WebService.getInstance().fetchBudget(userModel.familyId, userModel.token, new WebService.ResponseListener<HashMap<Integer,BudgetModel>>() {
                @Override
                public void onResults(ResponseModel<HashMap<Integer,BudgetModel>> results) {
                    if (results.success) {
                        DataSingleton.getInstance().setBudgetModel(results.result);
                        for (Integer bm : results.result.keySet()) {
                            addToList(results.result.get(bm));
                        }
                        DataSingleton.getInstance().setDirty(false);
                    }
                }

                @Override
                public void onError() {

                }
            });
        } else {
            HashMap<Integer, BudgetModel> bms = DataSingleton.getInstance().getBudgetModel();
            for (Integer bm : bms.keySet()) {
                addToList(bms.get(bm));
            }
        }
    }

    private void addToList(final BudgetModel bm) {
        BudgetCardView cardView = new BudgetCardView(container, getActivity());
        cardView.setTitle(bm.familyId);
        cardView.setGoal(bm.goal);
        cardView.setSpent(bm.getSpent());
        cardView.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("index", bm.id);
                controller.switchFragment(FragmentController.FragmentTypes.BudgetItems, bundle);
            }
        });
    }

    private void createEntry() {
        controller.hideFab();
        new PopupCreateBudget(getActivity(), container, new PopupCreateBudget.PopupCreateBudgetListener() {
            @Override
            public void onCreate(float goal) {
                WebService.getInstance().createBudget(userModel.familyId, userModel.token, goal, new WebService.ResponseListener<Integer>() {
                    @Override
                    public void onResults(ResponseModel<Integer> results) {
                        fetchBudget();
                        DataSingleton.getInstance().setDirty(true);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void onError(final String error) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDismiss() {
                controller.showFab();
            }
        });
    }
}
