package com.richardgrable.budgettracker.singletons;

import com.richardgrable.budgettracker.models.BudgetItemModel;
import com.richardgrable.budgettracker.models.BudgetModel;
import com.richardgrable.budgettracker.models.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class DataSingleton {
    private static DataSingleton ourInstance;

    public static DataSingleton getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataSingleton();
        }
        return ourInstance;
    }

    private UserModel userModel;
    private HashMap<Integer, BudgetModel> budgetModel;
    private boolean isDirty;

    private DataSingleton() {
        setDirty(true);
    }

    public void setUserModel(UserModel model) {
        userModel = model;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setBudgetModel(HashMap<Integer,BudgetModel> model) {
        budgetModel = model;
    }

    public HashMap<Integer,BudgetModel> getBudgetModel() {
        return budgetModel;
    }

    public boolean getIsDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        isDirty = dirty;
    }
}
