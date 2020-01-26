package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class CaptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption2);
    }

    public void editCaption(View view){
        EditText userEntry = (EditText) findViewById(R.id.captionEntry);
        String caption = userEntry.getText().toString();


        finish();
    }




}
