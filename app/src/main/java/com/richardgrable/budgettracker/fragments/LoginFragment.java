package com.richardgrable.budgettracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.richardgrable.budgettracker.R;
import com.richardgrable.budgettracker.controllers.FragmentController;
import com.richardgrable.budgettracker.models.ResponseModel;
import com.richardgrable.budgettracker.models.UserModel;
import com.richardgrable.budgettracker.services.WebService;
import com.richardgrable.budgettracker.singletons.DataSingleton;
import com.richardgrable.budgettracker.utils.LogUtil;

public class LoginFragment extends AbstractFragment {

    private Button loginButton;
    private EditText userName, password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onCreate(View view, Bundle savedInstanceState) {
        loginButton = getActivity().findViewById(R.id.btn_login);
        userName = getActivity().findViewById(R.id.edit_user);
        password = getActivity().findViewById(R.id.edit_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public String tag() {
        return "LoginFragment";
    }

    private void login() {
        LogUtil.e(tag(), "Logging in...");
        String userName = this.userName.getText().toString();
        String password = this.password.getText().toString();

        WebService.getInstance().login(userName, password, new WebService.ResponseListener<UserModel>() {
            @Override
            public void onResults(ResponseModel<UserModel> results) {
                if (results.success) {
                    DataSingleton.getInstance().setUserModel(results.result);
                    controller.switchFragment(FragmentController.FragmentTypes.Budget);
                }
            }

            @Override
            public void onError() {

            }
        });
    }
}
