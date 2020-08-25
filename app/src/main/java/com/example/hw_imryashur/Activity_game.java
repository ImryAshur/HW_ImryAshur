package com.example.hw_imryashur;

/* Student Name - Imry Ashur
 */
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.bumptech.glide.Glide;

public class Activity_game extends AppCompatActivity {
    private int RONALDOLIFE = 100;
    private int MESSILIFE = 100;
    private final int SHOT = 10;
    private final int FREEKICK = 20;
    private final int PENALTY = 30;
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

        setListener(game_BTN_shot_M,shotClickListener);
        setListener(game_BTN_shot_R,shotClickListener);
        setListener(game_BTN_freeKick_M,freeKickClickListener);
        setListener(game_BTN_freeKick_R,freeKickClickListener);
        setListener(game_BTN_penalty_M,penaltyClickListener);
        setListener(game_BTN_penalty_R,penaltyClickListener);

        glide(R.drawable.ronaldo,game_IMG_ronalno);
        glide(R.drawable.messi,game_IMG_messi);
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

    private void setListener(Button btn, View.OnClickListener func){
        btn.setOnClickListener(func);
    }

    private void glide(int img, ImageView into){
        Glide
                .with(Activity_game.this)
                .load(img)
                .centerCrop()
                .into(into);
    }

    private void endGame() {
        if (MESSILIFE <= 0) postWinner("Ronaldo Wins!" , game_IMG_ronalno);
        if (RONALDOLIFE <= 0) postWinner("Messi Wins!", game_IMG_messi);
    }

    private void postWinner(String winner , ImageView pic) {
        pic.buildDrawingCache();
        Bitmap bitmap = pic.getDrawingCache();
        Intent myIntent = new Intent(Activity_game.this, Activity_menu.class);
        myIntent.putExtra(Activity_menu.winnerPlayer, winner); //Optional parameters
        myIntent.putExtra(Activity_menu.image, bitmap);
        Activity_game.this.startActivity(myIntent);
        finish();
    }

    private void game() {
        if (ronaldoTurn) {
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


/* enable / disenable buttons , set drawable with style and color*/
    private void setButtons(Button isTurn , Button notIsTurn){
        isTurn.setEnabled(true);
        notIsTurn.setEnabled(false);
        isTurn.setBackgroundResource(R.drawable.game_playbuttons);
        notIsTurn.setBackgroundResource(R.drawable.unplaybuttons);
    }

    private void updateLifeColor() {
        if (MESSILIFE <= 20 ){
            lifeColor(game_progressBar_messiLife);
        }
        if (RONALDOLIFE <= 20){
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
            if (((String) view.getTag()).equals("r") ) {
                MESSILIFE = setlife(MESSILIFE, SHOT,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                RONALDOLIFE = setlife(RONALDOLIFE, SHOT,game_progressBar_ronaldoLife,true);
            }
            step();
        }
    };

    private View.OnClickListener freeKickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r") ) {
                MESSILIFE = setlife(MESSILIFE, FREEKICK,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                RONALDOLIFE = setlife(RONALDOLIFE, FREEKICK,game_progressBar_ronaldoLife,true);
            }
            step();
        }
    };

    private View.OnClickListener penaltyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((String) view.getTag()).equals("r") ) {
                MESSILIFE = setlife(MESSILIFE, PENALTY,game_progressBar_messiLife,false);
            }
            else if (((String) view.getTag()).equals("m") ){
                RONALDOLIFE = setlife(RONALDOLIFE, PENALTY,game_progressBar_ronaldoLife,true);
            }
            step();
        }
    };

/* update player life , change turn and set new player life*/
    private int setlife(int playerLife , int type , ProgressBar playerBar , boolean isRonaldo){
        playerLife = playerLife - type;
        playerBar.setProgress(playerLife);
        ronaldoTurn = isRonaldo;
        return playerLife;
    }

    /*after every click check if the game is over , if no - check whether progress bar need to change
    * and than set buttons for the next round  */
    private void step(){
        endGame();
        updateLifeColor();
        game();
    }
}