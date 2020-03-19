package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

//LA activity used to enter search criteria
public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public void Search(View v) {
        EditText starttime = findViewById(R.id.StartDate);
        EditText endtime = findViewById(R.id.EndDate);
        EditText caption = findViewById(R.id.Caption);
        EditText latitude = findViewById(R.id.Latitude);
        EditText longitude = findViewById(R.id.Longitude);
        EditText radius = findViewById(R.id.Radius);

        Intent i = new Intent();
        if (starttime.getText().toString().equals("")) {
            i.putExtra("STARTDATE", "-");
        } else {
            i.putExtra("STARTDATE", starttime.getText().toString());
            String x = starttime.getText().toString();
        }
        if (endtime.getText().toString().equals("")) {
            i.putExtra("ENDDATE", "-");
        } else {
            i.putExtra("ENDDATE", endtime.getText().toString());
        }
        if (caption.getText().toString().equals("")) {
            i.putExtra("CAPTION", "-");
        } else {
            i.putExtra("CAPTION", caption.getText().toString());
        }
        if (latitude.getText().toString().equals("")) {
            i.putExtra("LATITUDE", "-");
        } else {
            i.putExtra("LATITUDE", latitude.getText().toString());
        }
        if (longitude.getText().toString().equals("")) {
            i.putExtra("LONGITUDE", "-");
        } else {
            i.putExtra("LONGITUDE", longitude.getText().toString());
        }
        if (radius.getText().toString().equals("")) {
            i.putExtra("RADIUS" , "-");
        } else {
            i.putExtra("RADIUS", radius.getText().toString());
        }
        setResult(RESULT_OK, i);
        finish();
    }

    public void Cancel(View v) {
        finish();
    }

}
