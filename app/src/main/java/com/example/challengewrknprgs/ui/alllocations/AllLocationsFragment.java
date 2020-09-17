package com.example.challengewrknprgs.ui.alllocations;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.challengewrknprgs.R;
import com.example.challengewrknprgs.SearchActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AllLocationsFragment extends Fragment {
    private ArrayList<Locations> locationsArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    SearchView searchView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_locations, container, false);
        Toast.makeText(getContext(), "Here i tried to sort the recyclerview but i'm doing something wrong...", Toast.LENGTH_LONG).show();
        bindUI(root);
        isNetworkConnected();
        addJsonDataToArrayList();
        buildRecyclerView();


        return root;
    }

    private void bindUI(View view) {
        recyclerView = view.findViewById(R.id.rv_all_locations);
        searchView = view.findViewById(R.id.search_view);

    }

    private void buildRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new LocationsRecyclerViewAdapter(locationsArrayList));
    }

    public com.example.challengewrknprgs.holders.Locations getJsonToGson() {
        String string;
        try {
            InputStream inputStream = getContext().getAssets().open("locations.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            string = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            com.example.challengewrknprgs.holders.Locations locations = gson.fromJson(string, com.example.challengewrknprgs.holders.Locations.class);
            return locations;

        } catch (Exception e) {
            Log.d("TAG", "Exception: " + e);
        }
        return null;

    }

    private void addJsonDataToArrayList() {
        for (int i = 0; i < getJsonToGson().getSize(); i++) {
            if (!(getJsonToGson().getLocationList().get(i).getImage() == null))
                locationsArrayList.add(new Locations(getJsonToGson().getLocationList().get(i).getLat(),
                        getJsonToGson().getLocationList().get(i).getLng(),
                        getJsonToGson().getLocationList().get(i).getLabel(),
                        getJsonToGson().getLocationList().get(i).getAddress(),
                        getJsonToGson().getLocationList().get(i).getImage()));
            else {
                locationsArrayList.add(new Locations(getJsonToGson().getLocationList().get(i).getLat(),
                        getJsonToGson().getLocationList().get(i).getLng(),
                        getJsonToGson().getLocationList().get(i).getLabel(),
                        getJsonToGson().getLocationList().get(i).getAddress(),
                        "https://tacm.com/wp-content/uploads/2018/01/no-image-available.jpeg"));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = new MenuInflater(getContext());
        menuInflater.inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                LocationsRecyclerViewAdapter locationsRecyclerViewAdapter = new LocationsRecyclerViewAdapter(locationsArrayList);
                locationsRecyclerViewAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                LocationsRecyclerViewAdapter locationsRecyclerViewAdapter = new LocationsRecyclerViewAdapter(locationsArrayList);
                locationsRecyclerViewAdapter.getFilter().filter(s);
                return false;
            }
        });
    }

    protected void isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo == null) {
                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content), "You are not connected to internet.Images might not be displayed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        } catch (NullPointerException e) {
            Log.d("TAG", "Exception " + e);
        }
    }
}