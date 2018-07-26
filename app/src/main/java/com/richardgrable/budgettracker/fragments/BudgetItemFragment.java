package com.richardgrable.budgettracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.richardgrable.budgettracker.R;
import com.richardgrable.budgettracker.models.BudgetItemModel;
import com.richardgrable.budgettracker.models.ResponseModel;
import com.richardgrable.budgettracker.models.UserModel;
import com.richardgrable.budgettracker.services.WebService;
import com.richardgrable.budgettracker.singletons.DataSingleton;
import com.richardgrable.budgettracker.views.BudgetItemView;
import com.richardgrable.budgettracker.views.PopupCreateBudgetItem;

import java.util.ArrayList;

public class BudgetItemFragment extends AbstractFragment {

    private TextView goal;
    private ViewGroup container;
    private UserModel userModel;
    private ArrayList<BudgetItemModel> budgetItemModels;
    private int index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget_items, container, false);
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
        goal = view.findViewById(R.id.txt_goal);
        index = bundle.getInt("index");
        budgetItemModels = DataSingleton.getInstance().getBudgetModel().get(index).budgetItems;

        refreshView();
    }

    @Override
    public String tag() {
        return "BudgetItemFragment";
    }

    private void refreshView() {
        container.removeAllViews();
        goal.setText(getActivity().getString(R.string.dollar, DataSingleton.getInstance().getBudgetModel().get(index).goal));
        for (BudgetItemModel bim : budgetItemModels) {
            addToList(bim);
        }
    }

    private void addToList(BudgetItemModel bim) {
        BudgetItemView biv = new BudgetItemView(container, getActivity());
        biv.setAmount(bim.price);
        biv.setCategory(bim.category);
        biv.setName(bim.name);
    }

    private void createEntry() {
        controller.hideFab();
        new PopupCreateBudgetItem(getActivity(), container, new PopupCreateBudgetItem.PopupCreateBudgetItemListener() {
            @Override
            public void onCreate(final String name, final String category, final float price) {
                WebService.getInstance().createBudgetItem(index, userModel.token, name, category, price, new WebService.ResponseListener<Integer>() {
                    @Override
                    public void onResults(ResponseModel<Integer> results) {
                        if (results.success) {
                            BudgetItemModel bim = new BudgetItemModel();
                            bim.price = price;
                            bim.category = category;
                            bim.name = name;
                            DataSingleton.getInstance().setDirty(true);
                            addToList(bim);
                        }
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
