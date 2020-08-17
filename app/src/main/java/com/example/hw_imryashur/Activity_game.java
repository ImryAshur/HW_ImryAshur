package com.example.hw_imryashur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import static com.example.hw_imryashur.R.color.game_BTN_backgroundbtn;
import static com.example.hw_imryashur.R.color.game_BTN_backgroundbtndef;

public class Activity_game extends AppCompatActivity {
    private int ronaldoLife = 100;
    private int messiLife = 100;
    private final int shot = 10;
    private final int freeKick = 20;
    private final int penalty = 30;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findviews();
        game();
        game_BTN_shot_M.setOnClickListener(shotClickListener);
        game_BTN_shot_R.setOnClickListener(shotClickListener);
        game_BTN_freeKick_M.setOnClickListener(freeKickClickListener);
        game_BTN_freeKick_R.setOnClickListener(freeKickClickListener);
        game_BTN_penalty_M.setOnClickListener(penaltyClickListener);
        game_BTN_penalty_R.setOnClickListener(penaltyClickListener);

        Glide
                .with(this)
                .load(R.drawable.ronaldo)
                .centerCrop()
                .into(game_IMG_ronalno);
        Glide
                .with(this)
                .load(R.drawable.messi)
                .centerCrop()
                .into(game_IMG_messi);
    }


    private void endGame() {
        if (messiLife <= 0) postWinner("Ronaldo Wins!" , game_IMG_ronalno);
        if (ronaldoLife <= 0) postWinner("Messi Wins!", game_IMG_messi);
    }

    private void postWinner(String winner , ImageView pic) {
        pic.buildDrawingCache();
        Bitmap bitmap = pic.getDrawingCache();
        Intent myIntent = new Intent(Activity_game.this, Activity_menu.class);
        myIntent.putExtra("winnerPlayer", winner); //Optional parameters
        myIntent.putExtra("Image", bitmap);
        Activity_game.this.startActivity(myIntent);
        finish();
    }

    private void game() {
        if (ronaldoTurn == true) {
            setButtons(game_BTN_shot_R,game_BTN_shot_M);
            setButtons(game_BTN_freeKick_R,game_BTN_freeKick_M);
            setButtons(game_BTN_penalty_R,game_BTN_penalty_M);
        }
        else  {
            setButtons(game_BTN_shot_M,game_BTN_shot_R);
            setButtons(game_BTN_freeKick_M,game_BTN_freeKick_R);
            setButtons(game_BTN_penalty_M,game_BTN_penalty_R);
        }
    }

    private void setButtons(Button isTurn , Button notIsTurn){
        isTurn.setEnabled(true);
        notIsTurn.setEnabled(false);
        isTurn.setBackgroundColor(getResources().getColor(game_BTN_backgroundbtn));
        notIsTurn.setBackgroundColor(getResources().getColor(game_BTN_backgroundbtndef));
    }

    private void updateLifeColor() {
        if (messiLife <= 20 ){
            lifeColor(game_progressBar_messiLife);
        }
        if (ronaldoLife <= 20){
            lifeColor(game_progressBar_ronaldoLife);
        }
    }
    private void lifeColor(ProgressBar playerLife) {
        playerLife.getProgressDrawable().setColorFilter(
                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
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


    private View.OnClickListener shotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r") ) {
                messiLife = setlife(messiLife,shot,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                ronaldoLife = setlife(ronaldoLife,shot,game_progressBar_ronaldoLife,true);
            }
            updateLifeColor();
            game();
            endGame();
        }
    };



    private View.OnClickListener freeKickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r") ) {
                messiLife = setlife(messiLife,freeKick,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                ronaldoLife = setlife(ronaldoLife,freeKick,game_progressBar_ronaldoLife,true);
            }
            updateLifeColor();
            game();
            endGame();
        }
    };

    private View.OnClickListener penaltyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r") ) {
                messiLife = setlife(messiLife,penalty,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                ronaldoLife = setlife(ronaldoLife,penalty,game_progressBar_ronaldoLife,true);
            }
            updateLifeColor();
            game();
            endGame();
        }
    };
    private int setlife(int playerLife , int type , ProgressBar playerBar , boolean isRonaldo){
        playerLife = playerLife - type;
        playerBar.setProgress(playerLife);
        ronaldoTurn = isRonaldo;
        Log.d("pttt", type + " " + isRonaldo + " Life = " + playerLife);
        return playerLife;
    }
}