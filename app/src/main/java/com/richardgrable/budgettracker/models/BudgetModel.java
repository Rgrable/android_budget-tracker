package com.richardgrable.budgettracker.models;

import java.util.ArrayList;
import java.util.Date;

public class BudgetModel {
    public int id;
    public String familyId;
    public float goal;
    public ArrayList<BudgetItemModel> budgetItems;

    public float getSpent() {
        float spent = 0.0f;
        for(BudgetItemModel item : budgetItems) {
            spent += item.price;
        }

        return spent;
    }
}
