package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class CaptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
    }

    public void

    EditText userEntry = (EditText) findViewById(R.id.captionEntry);
    caption = userEntry.getText().toString();

}
