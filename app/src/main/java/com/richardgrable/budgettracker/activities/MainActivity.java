package com.richardgrable.budgettracker.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

import com.richardgrable.budgettracker.R;
import com.richardgrable.budgettracker.controllers.FragmentController;
import com.richardgrable.budgettracker.fragments.IFragment;
import com.richardgrable.budgettracker.fragments.LoginFragment;
import com.richardgrable.budgettracker.services.WebService;
import com.richardgrable.budgettracker.utils.LogUtil;

public class MainActivity extends Activity implements WebService.WebServiceListener {

    private static final String TAG = "MainActivity";
    private FragmentController fragmentController;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebService.getInstance().setWebServiceListener(this);
        fragmentController = new FragmentController(this);
        fragmentController.switchFragment(FragmentController.FragmentTypes.Login);
    }

    @Override
    public void onBackPressed() {
        fragmentController.goBack();
    }

    @Override
    public void onConnection(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void onFinish(String results) {
        progressDialog.dismiss();
        LogUtil.w(TAG, results);
    }

    @Override
    public void onError(final String message) {
        progressDialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
