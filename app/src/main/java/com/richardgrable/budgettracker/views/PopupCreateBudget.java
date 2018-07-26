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

public class PopupCreateBudget {

    public interface PopupCreateBudgetListener {
        void onCreate(float goal);

        void onError(String error);

        void onDismiss();
    }

    public PopupCreateBudget(Activity activity, ViewGroup container, final PopupCreateBudgetListener listener) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        LayoutInflater inflater = activity.getLayoutInflater();

        View m = inflater.inflate(R.layout.popup_create_budget, null);
        final PopupWindow window = new PopupWindow(m, (int) (width * 0.75f), (int) (height * 0.45f));
        window.setElevation(5.0f);
        window.setOutsideTouchable(true);
        window.setFocusable(true);

        final EditText edit = m.findViewById(R.id.edit_goal);
        Button create = m.findViewById(R.id.btn_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit.getText().toString().isEmpty()) {
                    listener.onError("please enter a value");
                } else {
                    float goal = Float.parseFloat(edit.getText().toString());
                    listener.onCreate(goal);
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
