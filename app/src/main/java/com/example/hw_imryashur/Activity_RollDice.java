package com.example.hw_imryashur;
/*
    Student - Imry Ashur
*/

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Random;

public class Activity_RollDice extends AppCompatActivity {

    public static final Random RANDOM = new Random();
    private Button rollDice_BTN_rollDices, rollDice_BTN_topTen;
    private ImageView rollDice_IMG_cube1, rollDice_IMG_cube2, rollDice_IMG_ronaldo, rollDice_IMG_messi;
    private int ronaldoScore = 0;
    private int messiScore = 0;
    private double lat = 0.0;
    private double lon = 0.0;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean firstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolldice);
        myLocation();
        findViews();
        initviews();
        rollDice_BTN_rollDices.setOnClickListener(cubeClickListener);
        rollDice_BTN_topTen.setOnClickListener(topTenListener);
    }

    private void findViews() {
        rollDice_BTN_rollDices = findViewById(R.id.rollDice_BTN_rollDices);
        rollDice_BTN_topTen = findViewById(R.id.rollDice_BTN_topTen);
        rollDice_IMG_cube1 = findViewById(R.id.rollDice_IMG_cube1);
        rollDice_IMG_cube2 = findViewById(R.id.rollDice_IMG_cube2);
        rollDice_IMG_ronaldo = findViewById(R.id.rollDice_IMG_ronaldo);
        rollDice_IMG_messi = findViewById(R.id.rollDice_IMG_messi);
    }
/*
    Glide for png images
*/
    private void initviews() {
        rollDice_IMG_messi.setImageResource(R.drawable.ic_messi);
        rollDice_IMG_ronaldo.setImageResource(R.drawable.ic_ronaldo);
        glide(R.drawable.dice_6, rollDice_IMG_cube1);
        glide(R.drawable.dice_6, rollDice_IMG_cube2);
    }

    // use glide for dice pictures
    private void glide(int img, ImageView into) {
        Glide
                .with(Activity_RollDice.this)
                .load(img)
                .centerCrop()
                .into(into);
    }

    private int randomDiceValue() {
        return RANDOM.nextInt(6) + 1;
    }

    private View.OnClickListener topTenListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MySignalV2.getInstance().makeSound(R.raw.click_on);
            Intent intent = new Intent(Activity_RollDice.this, Activity_Results.class);
            startActivity(intent);
        }
    };
    /*
        this method makes animation for the cubes and set new images
    */
    private View.OnClickListener cubeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            final Animation anim1 = AnimationUtils.loadAnimation(Activity_RollDice.this, R.anim.shake);
            final Animation anim2 = AnimationUtils.loadAnimation(Activity_RollDice.this, R.anim.shake);
            final Animation.AnimationListener animationListener = new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    MySignalV2.getInstance().makeSound(R.raw.shake_dice);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int value = randomDiceValue();
                    int res = getResources().getIdentifier("dice_" + value, "drawable", "com.example.hw_imryashur");

                    if (animation == anim1) {
                        glide(res, rollDice_IMG_cube1);
                        ronaldoScore = value;
                        Log.d("pttt", "ronaldo Score : " + ronaldoScore);
                    } else if (animation == anim2) {
                        glide(res, rollDice_IMG_cube2);
                        messiScore = value;
                        Log.d("pttt", "messi Score : " + messiScore);
                        delayNewActivity();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

            anim1.setAnimationListener(animationListener);
            anim2.setAnimationListener(animationListener);

            rollDice_IMG_cube1.startAnimation(anim1);
            rollDice_IMG_cube2.startAnimation(anim2);

        }
    };

    private void whoStart() {
        if (ronaldoScore > messiScore)
            startGame(ronaldoScore + " - " + messiScore + " -> Ronaldo Start!", true);
        else if (ronaldoScore < messiScore)
            startGame(ronaldoScore + " - " + messiScore + " -> Messi Start!", false);
        else MySignalV2.getInstance().showToast("It's A Tie Roll Again!");
    }

    //sending lat,lon and who start to the new activity
    private void startGame(String name, boolean ronaldoTurn) {
        MySignalV2.getInstance().showToast(name);
        Intent intent = new Intent(Activity_RollDice.this, Activity_Game.class);
        intent.putExtra(Activity_Game.EXTRA_KEY_TURN, ronaldoTurn);
        intent.putExtra(Activity_Game.EXTRA_KEY_LAT, lat);
        intent.putExtra(Activity_Game.EXTRA_KEY_LON, lon);
        startActivity(intent);
    }

    private void delayNewActivity() {
        final Handler handler = new Handler();
        final int delay = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                whoStart();
            }
        }, delay);
    }

/*
    This method for the first time after restart your cell phone and you still don't have a last location.
*/
    private void initLocation() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        Log.d("pttt", "init " + lat + ", " + lon);
                        firstTime = false;
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        break;
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
/*
    if the user confirmed the permission - get his location
*/
    private void myLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Activity_RollDice.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Log.d("pttt", lat + ", " + lon);
                }
                else if(firstTime){
                    initLocation();
                }
            }
        });
    }
/*
    checking if we get the permissions if not tell the user that his location in the map will be wrong
*/
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLocation();
                } else MySignalV2.getInstance().showToast("Keep Playing! But Without Your Location...");
            }
        }
    }
}