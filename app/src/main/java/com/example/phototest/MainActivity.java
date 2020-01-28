package com.example.phototest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    //Initial Variables GJ
    private String mCurrentPhotoPath = null;
    private int currentGalleryIndex = 0;    //GJ Variable to hold position in the photo gallery
    public ArrayList<String> photoGallery; //GJ Creates a list of strings called photoGallery, used to choose which photo to display

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    public static final int CAPTION_SET_CODE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    String SearchStartDate = "-";
    String SearchEndDate = "-";
    String Searchcaption = "-";
    String photoCaption = "";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GJ Set the date to be used in the building of a bloody array, I just want this to work. I think Liam is cheating
        //Sets the the min and maximum date value to the smallest possible Long value and largest Long value
        Date minDate = new Date(Long.MIN_VALUE);
        Date maxDate = new Date(Long.MAX_VALUE);

        //Calls the populateGallery Function, wasn't doing this before. Probably was why it was crashing.
        //Update: It was.
        photoGallery = createGallery(minDate, maxDate);

        //GJ Displays an image immediatly if there are existing files in the photogallery
        if (photoGallery.size() > 0)
            mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
        displayGallery(mCurrentPhotoPath);
    }

    //GJ Populate a photogallery so that the code hopefully stops breaking. Crashes all the bloody time,
    //probably cause I'm trying to pull pictures out of my ass? This makes an array out of known photos in
    //the storage
    private ArrayList<String> createGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        photoGallery = new ArrayList<String>();
        File [] fList = file.listFiles();
        if (fList != null) {
            for (File f : file.listFiles()) {
                photoGallery.add(f.getPath());
            }
        }
        return photoGallery;
    }

    //links the user to the search activity
    public void SearchPicture(View v) {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_ACTIVITY_REQUEST_CODE);
    }


    // Called when submit button is pressed
    public void Caption(View view){
        Intent addCaptionIntent = new Intent(this, CaptionActivity.class);
        startActivityForResult(addCaptionIntent, CAPTION_SET_CODE);
    }

    public void takePicture(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.phototest.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", mCurrentPhotoPath);
        return image;
    } //Create Image stuff, exactly like his

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                SearchStartDate = data.getStringExtra("STARTDATE");
                SearchEndDate = data.getStringExtra("ENDDATE");
                Searchcaption = data.getStringExtra("CAPTION");

                photoGallery = populateGallery();
                currentGalleryIndex = 0;
                mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
                displayGallery(mCurrentPhotoPath);
            }
        }
        if (requestCode == CAPTION_SET_CODE){
            if(resultCode == RESULT_OK){

                photoCaption = data.getStringExtra("CAPTIONEDIT");

                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");

                File[] fList = file.listFiles();
                String fileName = fList[currentGalleryIndex].getName();

                fileName = photoCaption + fileName;

                File newFile = new File(file,fileName);

                fList[currentGalleryIndex].renameTo(newFile);
                
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK) {
                photoGallery = populateGallery();
                currentGalleryIndex = 0;
                mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
                displayGallery(mCurrentPhotoPath);
            }
        }
    }

    //Update the gallery with new search criteria
    private ArrayList<String> populateGallery() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            boolean  SearchminDateStatus;
            boolean  SearchmaxDateStatus;
            boolean  SearchcaptionStatus;
            String FileCap;
            String Filename;
            String Combo;
            long Date;

            for (File f : file.listFiles()) {
                Filename = f.getName();
                String[] separated = Filename.split("_");

                Combo = separated[1] + separated[2];
                Date = Long.parseLong(Combo);

                if(separated.length == 5){
                    FileCap = separated[3];
                }else{
                    FileCap = "";
                }
                //////////////////

                if(SearchStartDate.equals("-")){
                    SearchminDateStatus = true;
                }else if(Long.parseLong(SearchStartDate) < Date){
                    SearchminDateStatus = true;
                }else{
                    SearchminDateStatus = false;
                }

                if(SearchEndDate.equals("-")){
                    SearchmaxDateStatus = true;
                }else if(Long.parseLong(SearchEndDate) > Date){
                    SearchmaxDateStatus = true;
                }else{
                    SearchmaxDateStatus = false;
                }

                if(Searchcaption.equals("-")){
                    SearchcaptionStatus = true;
                }else if(FileCap.toLowerCase().contains(Searchcaption.toLowerCase())){
                    SearchcaptionStatus = true;
                }else{
                    SearchcaptionStatus = false;
                }

                if((SearchminDateStatus == true)&&(SearchmaxDateStatus == true)&&(SearchcaptionStatus == true)){
                    photoGallery.add(f.getPath());
                }

                //////////////////

            }
        }
        return photoGallery;
    }

    //GJ Display the photo on the screen
    private void displayGallery(String path) {
        ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
        mImageView.setRotation(90);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    //GJ When the left button is clicked, calls this function to move a pointer and display the image
    //to the left
    public void moveLeft(View v) {
        --currentGalleryIndex;
        if(currentGalleryIndex < 0)
            currentGalleryIndex = 0;

        mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
        displayGallery(mCurrentPhotoPath);
    }

    //When the right button is clicked, calls this function to move a pointer and display the image
    //to the right
    public void moveRight(View v) {
        ++currentGalleryIndex;
        if(currentGalleryIndex >= photoGallery.size())
            currentGalleryIndex = photoGallery.size() - 1;

        mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
        displayGallery(mCurrentPhotoPath);
    }

}
