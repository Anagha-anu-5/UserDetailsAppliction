package com.example.userdetailsappliction.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.userdetailsappliction.R;
import com.example.userdetailsappliction.adapter.UsersListAdapter;
import com.example.userdetailsappliction.interfaces.MainUrl;
import com.example.userdetailsappliction.model.UsersListModel;

import java.util.ArrayList;
import java.util.List;

import com.example.userdetailsappliction.interfaces.MainUrl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserListsActivity extends AppCompatActivity implements UsersListAdapter.UserAdapterListener {

    private RecyclerView uList;

    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<UsersListModel> usersList;
    private UsersListAdapter adapter;
    private SearchView searchView;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);


        uList = findViewById(R.id.user_list_recycler);

        usersList = new ArrayList<>();
        adapter = new UsersListAdapter(this, usersList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(uList.getContext(), linearLayoutManager.getOrientation());

        uList.setHasFixedSize(true);
        uList.setLayoutManager(linearLayoutManager);
        uList.addItemDecoration(dividerItemDecoration);
        uList.setAdapter(adapter);

        getData();

    }

    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(MainUrl.MAIN_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                List<UsersListModel> items = new Gson().fromJson(response.toString(), new TypeToken<List<UsersListModel>>() {
                }.getType());


                usersList.clear();
                usersList.addAll(items);

                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.flav2) {
            getSupportActionBar().setTitle(R.string.app_name);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_color1)));
            return true;
        }

        if (id == R.id.flav1) {

            getSupportActionBar().setTitle("Client Information");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_color2)));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            // getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onUserSelected(UsersListModel user) {
        Toast.makeText(getApplicationContext(), "Selected: " + user.getName(), Toast.LENGTH_LONG).show();
    }
}