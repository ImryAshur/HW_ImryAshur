package com.example.hw_imryashur;
/*
   Student Name - Imry Ashur
*/

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Activity_Game extends AppCompatActivity {
    public static final Random RANDOM = new Random();
    private Handler handler = new Handler();
    private final int DELAY = 3000;
    private int RONALDOLIFE = 100;
    private int MESSILIFE = 100;
    private final int SHOT = 10;
    private final int FREEKICK = 20;
    private final int PENALTY = 30;
    private int numberOfStepsMessi = 0;
    private int numberOfStepsRonaldo = 0;
    private boolean ronaldoTurn = true;
    private ImageView game_IMG_ronalno;
    private ImageView game_IMG_messi;
    private Button game_BTN_shot_R;
    private Button game_BTN_shot_M;
    private Button game_BTN_freeKick_R;
    private Button game_BTN_freeKick_M;
    private Button game_BTN_penalty_R;
    private Button game_BTN_penalty_M;
    private ProgressBar game_progressBar_ronaldoLife;
    private ProgressBar game_progressBar_messiLife;
    private ArrayList<TopTen> scores = new ArrayList<>();
    private double lat = 0.0;
    private double lon = 0.0;
    public static final String EXTRA_KEY_TURN = "EXTRA_KEY_TURN";
    public static final String EXTRA_KEY_LAT = "EXTRA_KEY_LAT";
    public static final String EXTRA_KEY_LON = "EXTRA_KEY_LON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findviews();
        getMyIntent();
        game();
        setListener(game_BTN_shot_M, shotClickListener);
        setListener(game_BTN_shot_R, shotClickListener);
        setListener(game_BTN_freeKick_M, freeKickClickListener);
        setListener(game_BTN_freeKick_R, freeKickClickListener);
        setListener(game_BTN_penalty_M, penaltyClickListener);
        setListener(game_BTN_penalty_R, penaltyClickListener);

        glide(R.drawable.ronaldo, game_IMG_ronalno);
        glide(R.drawable.messi, game_IMG_messi);


    }

    @Override
    protected void onStart() {
        Log.d("pttt", "onStart");
        super.onStart();
        handler.postDelayed(run, DELAY);
    }

    @Override
    protected void onStop() {
        Log.d("pttt", "onStop");
        super.onStop();

        handler.removeCallbacks(run);
    }

    Runnable run = new Runnable() {
        public void run() {
            randomMove();
            Log.d("pttt", "ronaldo life = " + RONALDOLIFE + " messi life = " + MESSILIFE);
            if (endGame()) {
                handler.postDelayed(this, DELAY);
            }
        }
    };

    private void randomMove() {
        int value = RANDOM.nextInt(3) + 1;
        Log.d("pttt", "value: " + value);

        switch (value) {
            case 1:
                makeClick(ronaldoTurn, game_BTN_shot_R, game_BTN_shot_M, "Shot: 10 Points Less");
                break;
            case 2:
                makeClick(ronaldoTurn, game_BTN_freeKick_R, game_BTN_freeKick_M, "Free Kick: 20 Points Less");
                break;
            case 3:
                makeClick(ronaldoTurn, game_BTN_penalty_R, game_BTN_penalty_M, "Penalty: 30 Points Less");
                break;
        }
    }

    private void makeClick(boolean ronaldoTurn, Button ronaldoBtn, Button messiBtn, String text) {
        if (ronaldoTurn) {
            ronaldoBtn.performClick();
            MySignalV2.getInstance().showToast("Ronaldo Choose - " + text);
        } else {
            messiBtn.performClick();
            MySignalV2.getInstance().showToast("Messi Choose - " + text);
        }
    }

    private void getMyIntent() {
        Intent intent = getIntent();
        ronaldoTurn = intent.getBooleanExtra(EXTRA_KEY_TURN, true);
        lat = intent.getDoubleExtra(EXTRA_KEY_LAT,0.0);
        lon = intent.getDoubleExtra(EXTRA_KEY_LON,0.0);
    }

    private void findviews() {
        game_IMG_ronalno = findViewById(R.id.game_IMG_ronaldo);
        game_IMG_messi = findViewById(R.id.game_IMG_messi);
        game_progressBar_ronaldoLife = findViewById(R.id.game_progressBar_ronaldoLife);
        game_progressBar_messiLife = findViewById(R.id.game_progressBar_messiLife);
        game_BTN_shot_R = findViewById(R.id.game_BTN_shot_R);
        game_BTN_shot_M = findViewById(R.id.game_BTN_shot_M);
        game_BTN_freeKick_R = findViewById(R.id.game_BTN_freeKick_R);
        game_BTN_freeKick_M = findViewById(R.id.game_BTN_freeKick_M);
        game_BTN_penalty_R = findViewById(R.id.game_BTN_penalty_R);
        game_BTN_penalty_M = findViewById(R.id.game_BTN_penalty_M);
    }

    private void setListener(Button btn, View.OnClickListener func) {
        btn.setOnClickListener(func);
    }

    private void glide(int img, ImageView into) {
        Glide
                .with(Activity_Game.this)
                .load(img)
                .centerCrop()
                .into(into);
    }

    private boolean endGame() {
        if (MESSILIFE <= 0) {
            postWinner("Ronaldo", game_IMG_ronalno,numberOfStepsRonaldo);
            return false;
        } else if (RONALDOLIFE <= 0) {
            postWinner("Messi", game_IMG_messi,numberOfStepsMessi);
            return false;
        }
        return true;
    }

    private void postWinner(String winner, ImageView pic,int numberOfSteps) {
        Log.d("pttt", "WINNER ------> ronaldo life = " + RONALDOLIFE + " messi life = " + MESSILIFE);
        addToScoresList(winner,numberOfSteps);
        pic.buildDrawingCache();
        Bitmap bitmap = pic.getDrawingCache();
        Intent myIntent = new Intent(Activity_Game.this, Activity_Winner.class);
        myIntent.putExtra(Activity_Winner.winnerPlayer, winner);
        myIntent.putExtra(Activity_Winner.image, bitmap);
        myIntent.putExtra(Activity_Winner.numOfSteps,numberOfSteps);
        startActivity(myIntent);
        finish();
    }

    private void addToScoresList(String winner,int numberOfSteps) {
        scores = MySharedPreferencesV4.getInstance().getArray("DATA",new TypeToken<ArrayList<TopTen>>(){});
        if(scores == null) scores = new ArrayList<TopTen>();
        scores.add(new TopTen(winner,lat,lon,123,numberOfSteps));
        Collections.sort(scores);
        if(scores.size() > 10){
            scores.remove(scores.size() -1);
        }
        MySharedPreferencesV4.getInstance().putArray("DATA",scores);
        Log.d("pttt", scores.toString());
    }

    private void game() {
        if (ronaldoTurn) {
            setButtons(game_BTN_shot_R, game_BTN_shot_M);
            setButtons(game_BTN_freeKick_R, game_BTN_freeKick_M);
            setButtons(game_BTN_penalty_R, game_BTN_penalty_M);
        } else {
            setButtons(game_BTN_shot_M, game_BTN_shot_R);
            setButtons(game_BTN_freeKick_M, game_BTN_freeKick_R);
            setButtons(game_BTN_penalty_M, game_BTN_penalty_R);
        }
    }

    /* enable / disenable buttons , set drawable with style and color*/
    private void setButtons(Button isTurn, Button notIsTurn) {
        isTurn.setClickable(false);
        isTurn.setEnabled(true);
        notIsTurn.setEnabled(false);
        isTurn.setBackgroundResource(R.drawable.game_playbuttons);
        notIsTurn.setBackgroundResource(R.drawable.unplaybuttons);
    }

    private void updateLifeColor() {
        if (MESSILIFE <= 20) {
            lifeColor(game_progressBar_messiLife);
        }
        if (RONALDOLIFE <= 20) {
            lifeColor(game_progressBar_ronaldoLife);
        }
    }

    //Set progressBar color
    private void lifeColor(ProgressBar playerLife) {
        playerLife.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private View.OnClickListener shotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo++;
                MESSILIFE = setlife(MESSILIFE, SHOT, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi++;
                RONALDOLIFE = setlife(RONALDOLIFE, SHOT, game_progressBar_ronaldoLife, true);
            }

            step();
        }
    };

    private View.OnClickListener freeKickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo++;
                MESSILIFE = setlife(MESSILIFE, FREEKICK, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi++;
                RONALDOLIFE = setlife(RONALDOLIFE, FREEKICK, game_progressBar_ronaldoLife, true);
            }

            step();
        }
    };

    private View.OnClickListener penaltyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo++;
                MESSILIFE = setlife(MESSILIFE, PENALTY, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi++;
                RONALDOLIFE = setlife(RONALDOLIFE, PENALTY, game_progressBar_ronaldoLife, true);
            }

            step();
        }
    };

    /* update player life , change turn and set new player life*/
    private int setlife(int playerLife, int type, ProgressBar playerBar, boolean isRonaldo) {
        Log.d("pttt", "setlife: ronaldo turn " + isRonaldo);
        playerLife = playerLife - type;
        playerBar.setProgress(playerLife);
        ronaldoTurn = isRonaldo;
        return playerLife;
    }

    /*after every click check if the game is over , if no - check whether progress bar need to change
     * and than set buttons for the next round  */
    private void step() {
        updateLifeColor();
        game();
    }
}