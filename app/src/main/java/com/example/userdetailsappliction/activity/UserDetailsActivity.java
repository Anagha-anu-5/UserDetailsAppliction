package com.example.userdetailsappliction.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.userdetailsappliction.R;
import com.example.userdetailsappliction.interfaces.MainUrl;
import com.example.userdetailsappliction.model.UsersListModel;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {
    String userName;
    Context context;
    TextView nameTextView, emailTextView, phoneTextView, cNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_details);
        nameTextView = (TextView) findViewById(R.id.user_name);
        emailTextView = (TextView) findViewById(R.id.email_id);
        phoneTextView = (TextView) findViewById(R.id.phone);
        cNameTextView = (TextView) findViewById(R.id.company_name);

        userName = getIntent().getExtras().getString("name");
        getUserData(userName);
    }

    private void getUserData(final String uName) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MainUrl.MAIN_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        if (jsonObject.getString("name").equals(userName)) {
                            String getUserName = jsonObject.getString("name");
                            String getEmailId = jsonObject.getString("email");
                            String getPhoneNo = jsonObject.getString("phone");

                            JSONObject jsonObjectCompany = jsonObject.getJSONObject("company");

                            String companyName = jsonObjectCompany.getString("name");
                            String catchPhrase = jsonObjectCompany.getString("catchPhrase");
                            String bs = jsonObjectCompany.getString("bs");

                            nameTextView.setText(getUserName);
                            emailTextView.setText(getEmailId);
                            phoneTextView.setText(getPhoneNo);
                            cNameTextView.setText(companyName);


                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", uName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}