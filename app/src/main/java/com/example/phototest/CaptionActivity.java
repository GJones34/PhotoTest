package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;


public class CaptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption2);
        String imagePath = getIntent().getStringExtra("path");

        ImageView mImageView = (ImageView) findViewById(R.id.captionPhoto);
        mImageView.setRotation(90);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(imagePath));

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
