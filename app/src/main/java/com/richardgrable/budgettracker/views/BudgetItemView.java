package com.richardgrable.budgettracker.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.richardgrable.budgettracker.R;

public class BudgetItemView {

    private ViewGroup container;
    private Activity activity;
    private LayoutInflater inflater;

    private TextView name, amount, category;

    public BudgetItemView(ViewGroup container, Activity activity) {
        this.activity = activity;
        this.container = container;
        this.inflater = activity.getLayoutInflater();
        initialize();
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setAmount(float amount) {
        this.amount.setText(activity.getString(R.string.dollar, amount));
    }

    public void setCategory(String category) {
        this.category.setText(category);
    }

    private void initialize() {
        View main = inflater.inflate(R.layout.card_budget_item, container, false);
        name = main.findViewById(R.id.txt_name);
        amount = main.findViewById(R.id.txt_amount);
        category = main.findViewById(R.id.txt_category);
        container.addView(main);
    }
}
