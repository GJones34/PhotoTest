package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    public void Search(View v) {
        EditText starttime = findViewById(R.id.editText2);
        EditText endtime = findViewById(R.id.editText3);
        EditText caption = findViewById(R.id.editText4);

        Intent i = new Intent();
        i.putExtra("STARTDATE", starttime.getText().toString());
        i.putExtra("ENDDATE", endtime.getText().toString());
        i.putExtra("CAPTION", caption.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }

    public void Cancel(View v) {
        finish();
    }

}
