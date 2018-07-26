package com.richardgrable.budgettracker.views;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.richardgrable.budgettracker.R;

public class BudgetCardView {

    private ViewGroup container;
    private LayoutInflater inflater;
    private Activity activity;
    private View main;
    private TextView title, goal, spent;
    private float spentAmount, goalAmount;

    public BudgetCardView(ViewGroup container, Activity activity) {
        this.container = container;
        this.inflater = activity.getLayoutInflater();
        this.activity = activity;
        initialize();
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setGoal(float amount) {
        goal.setText(activity.getString(R.string.dollar, amount));
        goalAmount = amount;
    }

    public void setSpent(float amount) {
        spent.setText(activity.getString(R.string.dollar, amount));
        spentAmount = amount;
        if (spentAmount > goalAmount) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                spent.setTextColor(activity.getColor(R.color.red));
            }
        }
    }

    public void setOnClick(View.OnClickListener listener) {
        main.setOnClickListener(listener);
    }

    private void initialize() {
        main = inflater.inflate(R.layout.card_budget, container, false);
        title = main.findViewById(R.id.txt_name);
        goal = main.findViewById(R.id.txt_goal);
        spent = main.findViewById(R.id.txt_spent);
        spentAmount = 0.0f;
        goalAmount = 0.0f;
        container.addView(main);
    }
}
