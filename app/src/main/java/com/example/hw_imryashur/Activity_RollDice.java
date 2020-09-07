package com.example.hw_imryashur;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

public class Activity_RollDice extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final Random RANDOM = new Random();
    private Button main_BTN_rollDices;
    private ImageView main_IMG_cube1, main_IMG_cube2, main_IMG_ronaldo, main_IMG_messi;
    private int ronaldoScore, messiScore;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double lat = 0.0;
    private double lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolldice);
        findViews();
        initviews();
        initLocation();
        main_BTN_rollDices.setOnClickListener(cubeClickListener);
    }

    private void initLocation() {
        if (ActivityCompat.checkSelfPermission(Activity_RollDice.this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(Activity_RollDice.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }


    private void getLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void findViews() {
        main_BTN_rollDices = findViewById(R.id.main_BTN_rollDices);
        main_IMG_cube1 = findViewById(R.id.main_IMG_cube1);
        main_IMG_cube2 = findViewById(R.id.main_IMG_cube2);
        main_IMG_ronaldo = findViewById(R.id.main_IMG_ronaldo);
        main_IMG_messi = findViewById(R.id.main_IMG_messi);
    }

    private void initviews() {
        glide(R.drawable.ronaldo, main_IMG_ronaldo);
        glide(R.drawable.messi, main_IMG_messi);
        glide(R.drawable.dice_6, main_IMG_cube1);
        glide(R.drawable.dice_6, main_IMG_cube2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            }
        }
    }

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

    private View.OnClickListener cubeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final Animation anim1 = AnimationUtils.loadAnimation(Activity_RollDice.this, R.anim.shake);
            final Animation anim2 = AnimationUtils.loadAnimation(Activity_RollDice.this, R.anim.shake);
            final Animation.AnimationListener animationListener = new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int value = randomDiceValue();
                    int res = getResources().getIdentifier("dice_" + value, "drawable", "com.example.hw_imryashur");

                    if (animation == anim1) {
                        glide(res, main_IMG_cube1);
                        ronaldoScore = value;
                        Log.d("pttt", "ronaldo Score : " + ronaldoScore);
                        //main_IMG_cube1.setImageResource(res);
                    } else if (animation == anim2) {
                        glide(res, main_IMG_cube2);
                        messiScore = value;
                        Log.d("pttt", "messi Score : " + messiScore);
                        delayNewActivity();
                        //whoStart();
                        //main_IMG_cube2.setImageResource(res);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            };

            anim1.setAnimationListener(animationListener);
            anim2.setAnimationListener(animationListener);

            main_IMG_cube1.startAnimation(anim1);
            main_IMG_cube2.startAnimation(anim2);

        }
    };

    private void whoStart() {
        if (ronaldoScore > messiScore)
            startGame(ronaldoScore + " - " + messiScore + " -> Ronaldo Start!", true);
        else if (ronaldoScore < messiScore)
            startGame(ronaldoScore + " - " + messiScore + " -> Messi Start!", false);
        else MySignalV2.getInstance().showToast("It's A Tie Roll Again!");
    }

    private void startGame(String name, boolean ronaldoTurn) {
        MySignalV2.getInstance().showToast(name);
        Intent intent = new Intent(Activity_RollDice.this, Activity_Game.class);
        intent.putExtra(Activity_Game.EXTRA_KEY_TURN, ronaldoTurn);
        intent.putExtra(Activity_Game.EXTRA_KEY_LAT, lat);
        intent.putExtra(Activity_Game.EXTRA_KEY_LON, lon);
        startActivity(intent);
        finish();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                lon = mLastLocation.getLongitude();
                lat = mLastLocation.getLatitude();
            }
        } catch (SecurityException e) {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}