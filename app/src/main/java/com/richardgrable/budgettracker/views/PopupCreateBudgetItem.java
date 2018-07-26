package com.richardgrable.budgettracker.views;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.richardgrable.budgettracker.R;

public class PopupCreateBudgetItem {
    public interface PopupCreateBudgetItemListener {
        void onCreate(String name, String category, float price);

        void onError(String error);

        void onDismiss();
    }

    public PopupCreateBudgetItem(Activity activity, ViewGroup container, final PopupCreateBudgetItem.PopupCreateBudgetItemListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LayoutInflater inflater = activity.getLayoutInflater();

        View m = inflater.inflate(R.layout.popup_create_budget_item, null);
        final PopupWindow window = new PopupWindow(m, (int) (width * 0.75f), (int) (height * 0.65f));
        window.setElevation(5.0f);
        window.setOutsideTouchable(true);
        window.setFocusable(true);

        final EditText name = m.findViewById(R.id.edit_name);
        final EditText category = m.findViewById(R.id.edit_category);
        final EditText price = m.findViewById(R.id.edit_price);
        Button create = m.findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    listener.onError("please enter a name");
                } else if (category.getText().toString().isEmpty()) {
                    listener.onError("please enter a category");
                } else if (price.getText().toString().isEmpty()) {
                    listener.onError("please enter a price");
                } else {
                    String n = name.getText().toString();
                    String c = category.getText().toString();
                    float p = Float.parseFloat(price.getText().toString());
                    listener.onCreate(n,c,p);
                    window.dismiss();
                }
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                listener.onDismiss();
            }
        });
        window.showAtLocation(container, Gravity.CENTER, 0, 0);
    }
}
