package com.example.hw_imryashur;
/*
    Student - Imry Ashur
*/
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Activity_Winner extends AppCompatActivity {
    private ImageView winner_IMG_winnerPic;
    private TextView winner_LBL_winnerName;
    private TextView winner_LBL_numOfSteps;
    private Button winner_BTN_newGame;
    private Button winner_BTN_topTen;
    private String winnerName;
    private int pic = 0;
    private int steps = 0;
    public static final String winnerPlayer = "winnerPlayer";
    public static final String image = "image";
    public static final String numOfSteps = "numOfSteps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);
        findViews();
        getMyIntent();
        MySignalV2.getInstance().makeSound(R.raw.victory_sound);
        winner_IMG_winnerPic.setImageResource(pic);
        winner_LBL_winnerName.setText(winnerName + " Wins!");
        winner_LBL_numOfSteps.setText("Number Of Steps: " + steps);
        winner_BTN_newGame.setOnClickListener(newGameBtn);
        winner_BTN_topTen.setOnClickListener(topTen);

    }

    private void getMyIntent() {
        Intent intent = getIntent();
        winnerName = intent.getStringExtra(winnerPlayer);
        steps = intent.getIntExtra(numOfSteps,0);
        pic = intent.getIntExtra(image,0);
    }

    private void findViews() {
        winner_IMG_winnerPic = findViewById(R.id.winner_IMG_winnerPic);
        winner_LBL_winnerName = findViewById(R.id.winner_LBL_winnerName);
        winner_LBL_numOfSteps = findViewById(R.id.winner_LBL_numOfSteps);
        winner_BTN_newGame = findViewById(R.id.winner_BTN_newGame);
        winner_BTN_topTen = findViewById(R.id.winner_BTN_topTen);
    }

    private View.OnClickListener newGameBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MySignalV2.getInstance().makeSound(R.raw.click_on);
            Intent myIntent = new Intent(Activity_Winner.this, Activity_RollDice.class);
            Activity_Winner.this.startActivity(myIntent);
            finish();
        }
    };
    private View.OnClickListener topTen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            MySignalV2.getInstance().makeSound(R.raw.click_on);
            Intent myIntent = new Intent(Activity_Winner.this, Activity_Results.class);
            Activity_Winner.this.startActivity(myIntent);
        }
    };
}