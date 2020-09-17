package com.example.challengewrknprgs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.challengewrknprgs.holders.Locations;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;
    ImageButton imageButtonClear;
    TextView textViewLocationName;
    ImageView imageViewLocationImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        bindUI();
        isNetworkConnected();
        setAutoCompleteTextView();
        setClearButton();

    }

    private void bindUI() {
        autoCompleteTextView = findViewById(R.id.autoComplete_txt_view);
        imageButtonClear = findViewById(R.id.img_btn_clear);
        textViewLocationName = findViewById(R.id.txt_name_search_activity);
        imageViewLocationImage = findViewById(R.id.image_search_activity);
    }

    private void setAutoCompleteTextView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.suggestions));
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    imageButtonClear.setVisibility(View.VISIBLE);
                } else {
                    imageButtonClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SearchActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_LONG).show();
            }
        });

        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((i == EditorInfo.IME_ACTION_DONE)) {
                    setSearchedData();
                }
                return false;
            }
        });
    }

    private void setClearButton() {

        imageButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.getText().clear();
            }
        });
    }

    public Locations getJsonToGson() {
        String string;
        try {
            InputStream inputStream = SearchActivity.this.getAssets().open("locations.json");
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

    private void setSearchedData() {
        for (int p = 0; p < getJsonToGson().getSize(); p++) {
            if (getJsonToGson().getLocationList().get(p).getLabel().equals(autoCompleteTextView.getText().toString())) {
                if (getJsonToGson().getLocationList().get(p).getImage() == null) {
                    Picasso.get().load("https://tacm.com/wp-content/uploads/2018/01/no-image-available.jpeg").into(imageViewLocationImage);
                } else {
                    Picasso.get().load(getJsonToGson().getLocationList().get(p).getImage()).into(imageViewLocationImage);
                }
                textViewLocationName.setText(autoCompleteTextView.getText());
            }
        }
    }

    protected void isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) SearchActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo == null) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), "You are not connected to internet.Images might not be displayed", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        } catch (NullPointerException e) {
            Log.d("TAG","Exception "+ e);
        }
    }

}
