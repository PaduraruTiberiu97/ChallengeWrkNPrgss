package com.example.challengewrknprgs.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.challengewrknprgs.MainActivity;
import com.example.challengewrknprgs.R;
import com.example.challengewrknprgs.SearchActivity;
import com.example.challengewrknprgs.holders.Locations;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HomeFragment extends Fragment {

    Toolbar toolbar;
    Button btnGetLocation;
    EditText edtLat;
    EditText edtLng;
    ImageView imgLocation;
    TextView txtLocationName;
    Button btnSearch;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        bindUI(root);
        setBtnGetLocationClickListener();
        btnSearchSetClickListener();
        return root;
    }

    private void bindUI(View view) {
        toolbar = view.findViewById(R.id.toolbar_search);
        btnGetLocation = view.findViewById(R.id.btn_get_location);
        edtLat = view.findViewById(R.id.editText_lat);
        edtLng = view.findViewById(R.id.editText_lng);
        imgLocation = view.findViewById(R.id.imageView_location);
        txtLocationName = view.findViewById(R.id.textView_location);
        btnSearch=view.findViewById(R.id.btn_search_home);
    }


    public Locations getJsonToGson() {
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

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        if ((lat1 == lat2) && (lng1 == lng2)) {
                return 0;
        } else {
            double theta = lng1 - lng2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return dist;
        }
    }

    private int getClosestLocation(double lat, double lng) {
        int closestLocation = 0;
        double min = distance(lat, lng, Double.parseDouble(getJsonToGson().getLocationList().get(0).getLat()), Double.parseDouble(getJsonToGson().getLocationList().get(0).getLng()));
        for (int i = 0; i < getJsonToGson().getSize(); i++) {
            if (min > distance(lat, lng, Double.parseDouble(getJsonToGson().getLocationList().get(i).getLat()), Double.parseDouble(getJsonToGson().getLocationList().get(i).getLng())) || min == distance(lat, lng, Double.parseDouble(getJsonToGson().getLocationList().get(i).getLat()), Double.parseDouble(getJsonToGson().getLocationList().get(i).getLng()))) {
                min = distance(lat, lng, Double.parseDouble(getJsonToGson().getLocationList().get(i).getLat()), Double.parseDouble(getJsonToGson().getLocationList().get(i).getLng()));
                closestLocation = i;
            }
        }
        return closestLocation;
    }

    private void setBtnGetLocationClickListener() {
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (checkEmptyText()) {
                        if (isNetworkConnected()) {
                            txtLocationName.setText(getJsonToGson().getLocationList().get(getClosestLocation(Double.parseDouble(edtLat.getText().toString()), Double.parseDouble(edtLng.getText().toString()))).getLabel());
                            txtLocationName.setVisibility(View.VISIBLE);
                            Picasso.get().load(getJsonToGson().getLocationList().get(getClosestLocation(Double.parseDouble(edtLat.getText().toString()), Double.parseDouble(edtLng.getText().toString()))).getImage()).into(imgLocation);
                            imgLocation.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getContext(), "No internet connection.Image might not be displayed", Toast.LENGTH_SHORT).show();
                            txtLocationName.setText(getJsonToGson().getLocationList().get(getClosestLocation(Double.parseDouble(edtLat.getText().toString()), Double.parseDouble(edtLng.getText().toString()))).getLabel());
                            txtLocationName.setVisibility(View.VISIBLE);
                            Picasso.get().load(getJsonToGson().getLocationList().get(getClosestLocation(Double.parseDouble(edtLat.getText().toString()), Double.parseDouble(edtLng.getText().toString()))).getImage()).into(imgLocation);
                            imgLocation.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(),"You must introduce valid numbers",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return mNetworkInfo != null;

        } catch (NullPointerException e) {
            return false;

        }
    }

    private boolean checkEmptyText() {
        String edtLatString = edtLat.getText().toString();
        String edtLngString = edtLng.getText().toString();

        if (TextUtils.isEmpty(edtLatString)) {
            edtLat.requestFocus();
            edtLat.setError("Cannot be empty");
            return false;

        } else if (TextUtils.isEmpty(edtLngString)) {
            edtLng.requestFocus();
            edtLng.setError("Cannot be empty");
            return false;
        }
        return true;
    }

    private void btnSearchSetClickListener(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);

            }
        });

    }
}