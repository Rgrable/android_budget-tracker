package com.richardgrable.budgettracker.services;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.richardgrable.budgettracker.configs.WebConfig;
import com.richardgrable.budgettracker.models.BudgetModel;
import com.richardgrable.budgettracker.models.UserModel;
import com.richardgrable.budgettracker.models.ResponseModel;
import com.richardgrable.budgettracker.utils.LogUtil;
import com.richardgrable.budgettracker.utils.StringUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class WebService {

    private static final String TAG = "WebService";

    public interface ResponseListener<T> {
        void onResults(ResponseModel<T> results);
        void onError();
    }

    public interface WebServiceListener {
        void onConnection(String message);
        void onFinish(String results);
        void onError(String message);
    }

    private static WebService instance;
    public static WebService getInstance() {
        if (instance == null) {
            instance = new WebService();
        }

        return instance;
    }

    private WebServiceListener webServiceListener;

    private WebService() {

    }

    public void setWebServiceListener(WebServiceListener listener) {
        webServiceListener = listener;
    }

    public void login(String username, String password, final ResponseListener<UserModel> listener) {
        password = StringUtil.encryptPassword(password);
        HashMap<String, Object> data = new HashMap<>();
        data.put("user", username);
        data.put("password", password);
        LogUtil.d(TAG, password);
        webServiceListener.onConnection("logging in");
        new Connection(new Connection.ConnectionListener() {
            @Override
            public void onComplete(String results) {
                try {
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ResponseModel<UserModel>>(){}.getType();
                    ResponseModel<UserModel> res = gson.fromJson(results, collectionType);
                    if (res.success) {
                        webServiceListener.onFinish(results);
                    } else {
                        LogUtil.e(TAG, results);
                        webServiceListener.onError(res.message);
                    }
                    listener.onResults(res);
                } catch (Exception e) {
                    e.printStackTrace();
                    webServiceListener.onError(e.getMessage());
                    listener.onError();
                }
            }
        }).execute(getPostData("login", data));
    }

    public void fetchBudget(String familyId, String token, final ResponseListener<HashMap<Integer,BudgetModel>> listener) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("familyId", familyId);
        data.put("token", token);
        webServiceListener.onConnection("fetching budget");
        new Connection(new Connection.ConnectionListener() {
            @Override
            public void onComplete(String results) {
                try {
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ResponseModel<HashMap<Integer,BudgetModel>>>(){}.getType();
                    ResponseModel<HashMap<Integer,BudgetModel>> res = gson.fromJson(results, collectionType);
                    if (res.success) {
                        webServiceListener.onFinish(results);
                    } else {
                        webServiceListener.onError(res.message);
                    }
                    listener.onResults(res);
                } catch (Exception e) {
                    e.printStackTrace();
                    webServiceListener.onError(e.getMessage());
                    listener.onError();
                }
            }
        }).execute(getPostData("fetchBudget", data));
    }

    public void createBudget(String familyId, String token, float goal, final ResponseListener<Integer> listener) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("familyId", familyId);
        data.put("goal", goal);
        data.put("token", token);
        webServiceListener.onConnection("creating budget");
        new Connection(new Connection.ConnectionListener() {
            @Override
            public void onComplete(String results) {
                try {
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ResponseModel<Integer>>(){}.getType();
                    ResponseModel<Integer> res = gson.fromJson(results, collectionType);
                    if (res.success) {
                        webServiceListener.onFinish(results);
                    } else {
                        webServiceListener.onError(res.message);
                    }
                    listener.onResults(res);
                } catch (Exception e) {
                    webServiceListener.onError(e.getMessage());
                    e.printStackTrace();
                    listener.onError();
                }
            }
        }).execute(getPostData("createBudget", data));
    }

    public void createBudgetItem(int budgetId, String token, String name, String category, float price, final ResponseListener<Integer> listener) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("budgetId", budgetId);
        data.put("category", category);
        data.put("name", name);
        data.put("price", price);
        data.put("token", token);
        webServiceListener.onConnection("creating budget item");
        new Connection(new Connection.ConnectionListener() {
            @Override
            public void onComplete(String results) {
                try {
                    Gson gson = new Gson();
                    Type collectionType = new TypeToken<ResponseModel<Integer>>(){}.getType();
                    ResponseModel<Integer> res = gson.fromJson(results, collectionType);
                    if (res.success) {
                        webServiceListener.onFinish(results);
                    } else {
                        webServiceListener.onError(res.message);
                    }
                    listener.onResults(res);
                } catch (Exception e) {
                    webServiceListener.onError(e.getMessage());
                    e.printStackTrace();
                    listener.onError();
                }
            }
        }).execute(getPostData("createBudgetItem", data));
    }

    private byte[] getPostData(String action, HashMap<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        builder.append("controller=").append(WebConfig.CONTROLLER);
        builder.append("&action=").append(action);
        builder.append("&access=").append(Uri.encode(WebConfig.ACCESS));
        for (String key : data.keySet()) {
            builder.append("&").append(key).append("=").append(Uri.encode(data.get(key).toString()));
        }

        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static class Connection extends AsyncTask<byte[], Void, String> {

        public interface ConnectionListener {
            void onComplete(String results);
        }

        private ConnectionListener listener;

        public Connection(ConnectionListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(byte[]... postData) {
            String res = "";
            try {
                URL url = new URL(WebConfig.URL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(15*1000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty( "Content-Length", Integer.toString( postData[0].length ));
                try( DataOutputStream wr = new DataOutputStream( connection.getOutputStream())) {
                    wr.write( postData[0] );
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                res = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            listener.onComplete(s);
        }
    }
}
