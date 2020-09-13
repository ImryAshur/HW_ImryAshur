package com.example.hw_imryashur;
/*
   Student Name - Imry Ashur
*/
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Activity_Game extends AppCompatActivity {
    public static final Random RANDOM = new Random();
    private final int DELAY = 2000;
    private final int SHOT = 10;
    private final int FREEKICK = 20;
    private final int PENALTY = 30;
    private int ronaldoLife = 100;
    private int messiLife = 100;
    private int numberOfStepsMessi = 0;
    private int numberOfStepsRonaldo = 0;
    private double lat = 0.0;
    private double lon = 0.0;
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
    private Handler handler = new Handler();
    public static final String EXTRA_KEY_TURN = "EXTRA_KEY_TURN";
    public static final String EXTRA_KEY_LAT = "EXTRA_KEY_LAT";
    public static final String EXTRA_KEY_LON = "EXTRA_KEY_LON";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findviews();
        initViews();
        getMyIntent();
        game();
        setListener(game_BTN_shot_M, shotClickListener);
        setListener(game_BTN_shot_R, shotClickListener);
        setListener(game_BTN_freeKick_M, freeKickClickListener);
        setListener(game_BTN_freeKick_R, freeKickClickListener);
        setListener(game_BTN_penalty_M, penaltyClickListener);
        setListener(game_BTN_penalty_R, penaltyClickListener);

    }
/*
    start and stop the timer if close/start the activity
*/
    @Override
    protected void onStart() {
        super.onStart();
        handler.postDelayed(run, DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(run);
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

    private void initViews() {
        game_IMG_ronalno.setImageResource(R.drawable.ic_ronaldo);
        game_IMG_messi.setImageResource(R.drawable.ic_messi);
    }

    private void getMyIntent() {
        Intent intent = getIntent();
        ronaldoTurn = intent.getBooleanExtra(EXTRA_KEY_TURN, true);
        lat = intent.getDoubleExtra(EXTRA_KEY_LAT,0.0);
        lon = intent.getDoubleExtra(EXTRA_KEY_LON,0.0);
    }

    Runnable run = new Runnable() {
        public void run() {
            randomMove();
            Log.d("pttt", "ronaldo life = " + ronaldoLife + " messi life = " + messiLife);
            if (endGame()) {
                handler.postDelayed(this, DELAY);
            }
        }
    };

    // get random value and make click button
    private void randomMove() {
        int value = RANDOM.nextInt(3) + 1;
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

    //set listener to each button
    private void setListener(Button btn, View.OnClickListener func) {
        btn.setOnClickListener(func);
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
        if (messiLife <= 20) {
            lifeColor(game_progressBar_messiLife);
        }
        if (ronaldoLife <= 20) {
            lifeColor(game_progressBar_ronaldoLife);
        }
    }

    //Set progressBar color
    private void lifeColor(ProgressBar playerLife) {
        playerLife.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    /*
        call functions in term which button was pressed
    */
    private View.OnClickListener shotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo = hit(R.raw.ronaldo_hook,numberOfStepsRonaldo);
                messiLife = setlife(messiLife, SHOT, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi = hit(R.raw.messi_hook,numberOfStepsMessi);
                ronaldoLife = setlife(ronaldoLife, SHOT, game_progressBar_ronaldoLife, true);
            }
            step();
        }
    };


    private View.OnClickListener freeKickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo = hit(R.raw.ronaldo_hook,numberOfStepsRonaldo);
                messiLife = setlife(messiLife, FREEKICK, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi = hit(R.raw.messi_hook,numberOfStepsMessi);
                ronaldoLife = setlife(ronaldoLife, FREEKICK, game_progressBar_ronaldoLife, true);
            }
            step();
        }
    };

    private View.OnClickListener penaltyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r")) {
                numberOfStepsRonaldo = hit(R.raw.ronaldo_hook,numberOfStepsRonaldo);
                messiLife = setlife(messiLife, PENALTY, game_progressBar_messiLife, false);
            } else if (((String) view.getTag()).equals("m")) {
                numberOfStepsMessi = hit(R.raw.messi_hook,numberOfStepsMessi);
                ronaldoLife = setlife(ronaldoLife, PENALTY, game_progressBar_ronaldoLife, true);
            }
            step();
        }
    };

    // make hit sound and update number of steps
    private int hit(int sound, int numberOfSteps) {
        MySignalV2.getInstance().makeSound(sound);
        numberOfSteps = numberOfSteps + 1;
        return numberOfSteps;
    }

    /* update player life , change turn and set new player life*/
    private int setlife(int playerLife, int type, ProgressBar playerBar, boolean isRonaldo) {
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

    private void postWinner(String winner, int pic ,int numberOfSteps) {
        Log.d("pttt", "WINNER ------> ronaldo life = " + ronaldoLife + " messi life = " + messiLife);
        addToScoresList(winner,numberOfSteps);
        Intent myIntent = new Intent(Activity_Game.this, Activity_Winner.class);
        myIntent.putExtra(Activity_Winner.winnerPlayer, winner);
        myIntent.putExtra(Activity_Winner.image, pic);
        myIntent.putExtra(Activity_Winner.numOfSteps,numberOfSteps);
        startActivity(myIntent);
        finish();
    }
/*
    adding new object to the list and remove the last object if the list is bigger than ten.
*/
    private void addToScoresList(String winner,int numberOfSteps) {
        scores = MySharedPreferencesV4.getInstance().getArray(Activity_Results.SP_DATA,new TypeToken<ArrayList<TopTen>>(){});
        if(scores == null) scores = new ArrayList<TopTen>();
        scores.add(new TopTen(winner,lat,lon,numberOfSteps));
        Collections.sort(scores);
        if(scores.size() > 10){
            scores.remove(scores.size() -1);
        }
        MySharedPreferencesV4.getInstance().putArray(Activity_Results.SP_DATA,scores);
        Log.d("pttt", scores.toString());
    }

    private boolean endGame() {
        if (messiLife <= 0) {
            postWinner("Ronaldo", R.drawable.ic_ronaldo ,numberOfStepsRonaldo);
            return false;
        } else if (ronaldoLife <= 0) {
            Log.d("pttt", "endGame: " + numberOfStepsMessi);
            return false;
        }
        return true;
    }
}