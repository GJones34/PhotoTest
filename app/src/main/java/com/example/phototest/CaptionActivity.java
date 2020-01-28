package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        EditText caption = findViewById(R.id.captionEntry);

        Intent c = new Intent();
        c.putExtra("CAPTIONEDIT", caption.getText().toString());
        setResult(RESULT_OK, c);

        finish();
    }

    public void cancelCaption (View view){finish();}


}
