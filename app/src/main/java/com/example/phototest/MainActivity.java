package com.example.phototest;

import com.example.phototest.support_package.DistanceCalc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;


public class MainActivity extends AppCompatActivity {
    //Initial Variables GJ
    private String mCurrentPhotoPath = null;
    private int currentGalleryIndex = 0;    //GJ Variable to hold position in the photo gallery
    public ArrayList<String> photoGallery; //GJ Creates a list of strings called photoGallery, used to choose which photo to display

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    public static final int CAPTION_SET_CODE = 2;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    DistanceCalc DistanceCalc = new DistanceCalc();

    String SearchStartDate = "-";
    String SearchEndDate = "-";
    String Searchcaption = "-";
    String SearchLatitude = "-";
    String SearchLongitude = "-";
    String SearchRadius = "-";
    String photoCaption = "";
    String lat = "";
    String lng = "";

    int TEST = 1;

    //Fused stuff, doing what the interwebs tells me
    public FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        TEST);
            }
        } else {
        }


        //Stuff for location stuff, fusedlocation things that i dont know
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    //GJ Populate a photogallery so that the code hopefully stops breaking. Crashes all the bloody time,
    //probably cause I'm trying to pull pictures out of my ass? This makes an array out of known photos in
    //the storage
    private ArrayList<String> createGallery(Date minDate, Date maxDate) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
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
    public void Caption(View view) {
        Intent addCaptionIntent = new Intent(this, CaptionActivity.class);
        addCaptionIntent.putExtra("path", mCurrentPhotoPath);
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
        File image = File.createTempFile(imageFileName, ".jpeg", storageDir);
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
                SearchLatitude = data.getStringExtra("LATITUDE");
                SearchLongitude = data.getStringExtra("LONGITUDE");
                SearchRadius = data.getStringExtra("RADIUS");

                photoGallery = populateGallery();
                mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
                displayGallery(mCurrentPhotoPath);
            }
        }
        if (requestCode == CAPTION_SET_CODE) {
            if (resultCode == RESULT_OK) {

                photoCaption = data.getStringExtra("CAPTIONEDIT");

                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");

                File[] fList = file.listFiles();
                String fileName = fList[currentGalleryIndex].getName();

                String[] editName = fileName.split("_");

                if (editName.length == 6) {
                    fileName = editName[0] + "_" + editName[1] + "_" + editName[2] + "_" + photoCaption + "_" + editName[3] + "_" +
                            editName[4] + "_" + editName[5];
                }else{
                    fileName = editName[0] + "_" + editName[1] + "_" + editName[2] + "_" + photoCaption + "_" + editName[4] + "_" +
                            editName[5] + "_" + editName[6];
                }

                File newFile = new File(file, fileName);

                fList[currentGalleryIndex].renameTo(newFile);
                photoGallery = populateGallery();
                mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
                displayGallery(mCurrentPhotoPath);

            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    GPSLocation();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                photoGallery = populateGallery();
                mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
                displayGallery(mCurrentPhotoPath);
            }
        }
    }

    //LA Updates the gallery with new search criteria
    private ArrayList<String> populateGallery() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            boolean  SearchminDateStatus;
            boolean  SearchmaxDateStatus;
            boolean  SearchcaptionStatus;
            boolean  SearchLocationStatus;
            String FileCap = "-";
            String Filename;
            String Combo;
            long Date = 0;
            float LAT = 0;
            float LON = 0;

            for (File f : file.listFiles()) {
                Filename = f.getName();
                String[] separated = Filename.split("_");

                Combo = separated[1] + separated[2];
                Date = Long.parseLong(Combo);
                if (separated.length == 7) {
                    LAT = Float.parseFloat(separated[4]);
                    LON = Float.parseFloat(separated[5]);
                }else{
                    LAT = Float.parseFloat(separated[3]);
                    LON = Float.parseFloat(separated[4]);
                }

                if(separated.length == 7){
                    FileCap = separated[5];
                }else{
                    FileCap = "-";
                }
                //////////////////commented out until location tagging is complete


                if (SearchStartDate.equals("-")) {
                    SearchminDateStatus = true;
                } else if (Long.parseLong(SearchStartDate) < Date) {
                    SearchminDateStatus = true;
                } else {
                    SearchminDateStatus = false;
                }

                if (SearchEndDate.equals("-")) {
                    SearchmaxDateStatus = true;
                } else if (Long.parseLong(SearchEndDate) > Date) {
                    SearchmaxDateStatus = true;
                } else {
                    SearchmaxDateStatus = false;
                }

                if (Searchcaption.equals("-")) {
                    SearchcaptionStatus = true;
                } else if (FileCap.toLowerCase().contains(Searchcaption.toLowerCase())) {
                    SearchcaptionStatus = true;
                } else {
                    SearchcaptionStatus = false;
                }
                /////////////////////////////////////////////WIP

                if(SearchLatitude.equals("-")||SearchLongitude.equals("-")||SearchRadius.equals("-")){
                    SearchLocationStatus = true;
                }else if(DistanceCalc.DistanceCoords(LAT,LON,Float.parseFloat(SearchLatitude),Float.parseFloat(SearchLongitude)) < Double.parseDouble(SearchRadius)){////////////////////NEEDS EDIT AFTER FUNCTION IS COMPLETE
                    SearchLocationStatus = true;
                }else{
                    SearchLocationStatus = false;
                }

                if ((SearchminDateStatus == true) && (SearchmaxDateStatus == true) && (SearchcaptionStatus == true) && SearchLocationStatus == true) {
                    photoGallery.add(f.getPath());
                }

                //////////////////

            }
        }
        // CL if
        if(photoGallery.size() == 0){
            Date minDate = new Date(Long.MIN_VALUE);
            Date maxDate = new Date(Long.MAX_VALUE);
            photoGallery = createGallery(minDate, maxDate);
        }
        return photoGallery;
    }

    //GJ Display the photo on the screen
    private void displayGallery(String path) {
        ImageView mImageView = (ImageView) findViewById(R.id.ivGallery);
        TextView mDateView = (TextView) findViewById(R.id.timeStamp);
        TextView mCaptionView = (TextView) findViewById(R.id.captionText);
        TextView mLatView = (TextView) findViewById(R.id.Lat);
        TextView mLngView = (TextView) findViewById(R.id.Lng);

        mImageView.setRotation(90);
        mImageView.setImageBitmap(BitmapFactory.decodeFile(path));
        if (path != null) {
            String[] attr = path.split("_");
            /*Regex statement to split up the date string, splits it into an array of chars
            then prints them out with dashes between for better visibility
             */
            String[] sdate = attr[1].split("(?!^)");

            if (attr.length == 7) {
                mDateView.setText(sdate[0] + sdate[1] + sdate[2] + sdate[3] +
                        "-" + sdate[4] + sdate[5] + "-" + sdate[6] + sdate[7]);
                mCaptionView.setText(attr[3]);
                mLatView.setText(attr[4] + " Latitude");
                mLngView.setText(attr[5] + " Longitude");
            } else {
                mDateView.setText(sdate[0] + sdate[1] + sdate[2] + sdate[3] +
                        "-" + sdate[4] + sdate[5] + "-" + sdate[6] + sdate[7]);
                mLatView.setText(attr[3] + " Latitude");
                mLngView.setText(attr[4] + " Longitude");
                mCaptionView.setText("");
            }
        }
    }

    //GJ When the left button is clicked, calls this function to move a pointer and display the image
    //to the left
    public void moveLeft(View v) {
        --currentGalleryIndex;
        if (currentGalleryIndex < 0)
            currentGalleryIndex = 0;

        mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
        displayGallery(mCurrentPhotoPath);
    }

    //When the right button is clicked, calls this function to move a pointer and display the image
    //to the right
    public void moveRight(View v) {
        ++currentGalleryIndex;
        if (currentGalleryIndex >= photoGallery.size())
            currentGalleryIndex = photoGallery.size() - 1;

        mCurrentPhotoPath = photoGallery.get(currentGalleryIndex);
        displayGallery(mCurrentPhotoPath);
    }


    public void GPSLocation() throws IOException {

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        //Starts fused location client, gets location, puts it in location variable, pulls lat and lng out
        Task<Location> location = fusedLocationClient.getLastLocation();
        //Had to wait for the fusedlocationclient to obtain all the data, kept crashing before
        while(!location.isComplete()){ }
        //Turns out this code just blitzes through at the speed of light and doesn't allow process' to finish
        Location loc = location.getResult();

        lat = String.format("%.6f", loc.getLatitude());
        lng = String.format("%.6f", loc.getLongitude());

        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        File[] fList = file.listFiles();

        String fileName = fList[currentGalleryIndex].getName();

        String[] editName = fileName.split("_");

        fileName = editName[0] + "_" + editName[1] + "_" + editName[2] + "_" + lat + "_" + lng + "_" + editName[3];

        File newFile = new File(file, fileName);

        fList[currentGalleryIndex].renameTo(newFile);
        photoGallery = populateGallery();
    }

    //LA Uploads photo currently being viewed in the gallery to facebook as your profile pic
    public void UploadPicture(View v) {

        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.example.phototest/files/Pictures");
        File[] fList = file.listFiles();
        String Filename = "/storage/emulated/0/Android/data/com.example.phototest/files/Pictures/" + fList[currentGalleryIndex].getName();
        Uri uri = Uri.parse(Filename);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri); // set uri
        shareIntent.setPackage("com.facebook.katana");
        startActivity(shareIntent);

    }
}
