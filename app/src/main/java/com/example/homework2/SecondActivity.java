package com.example.homework2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SecondActivity extends AppCompatActivity {

    private static final String api_url="https://api.punkapi.com/v2/beers?";
    private String constructed_url;
    private static AsyncHttpClient client = new AsyncHttpClient();

    private EditText editText_name;
    private EditText editText_from;
    private EditText editText_to;
    private Switch switch_highPoint;
    private Boolean highPointCheck = false;
    private Button button_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        editText_name = findViewById(R.id.editText_name);
        editText_from = findViewById(R.id.editText_from);
        editText_to = findViewById(R.id.editText_to);
        switch_highPoint = findViewById(R.id.switch_highPoint);
        button_search = findViewById(R.id.button_search);

        if (switch_highPoint != null) {
            switch_highPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        highPointCheck = true;
                    }
                    else {
                        highPointCheck = false;
                    }
                }
            });
        }

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNextActivity(v);
            }
        });

    }

    public void launchNextActivity(View v){
        // get text input and construct url
        constructed_url = api_url;
        if (!editText_name.getText().toString().trim().matches("")) {
            String name = editText_name.getText().toString().trim();
            constructed_url = constructed_url+"beer_name="+name;
        }
        if (!editText_from.getText().toString().trim().matches("")) {
            String from = editText_from.getText().toString().trim();
            constructed_url = constructed_url+"brewed_after="+from;
        }
        if (!editText_to.getText().toString().trim().matches("")) {
            String from = editText_to.getText().toString().trim();
            constructed_url = constructed_url+"brewed_before="+from;
        }
        if (highPointCheck) {
            constructed_url = constructed_url+"abv_gt=3.9";
        }

        // set the header because of the api endpoint
        client.addHeader("Accept", "application/json");
        // send a get request to the api url
        client.get(constructed_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody));
                // System.out.println(constructed_url);
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("api error", new String(responseBody));
            }
        });
    }
}
