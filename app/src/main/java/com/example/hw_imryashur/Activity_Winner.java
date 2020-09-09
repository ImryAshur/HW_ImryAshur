package com.example.hw_imryashur;
/*
    Student - Imry Ashur
*/

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

public class Activity_Winner extends AppCompatActivity {
    private ImageView menu_IMG_winnerPic;
    private TextView menu_LBL_winnerName;
    private TextView menu_LBL_numOfSteps;
    private Button menu_BTN_newGame;
    private Button menu_BTN_topTen;
    private String winnerName;
    private Bitmap bitmap;
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
        menu_LBL_winnerName.setText(winnerName + " Wins!");
        menu_LBL_numOfSteps.setText("Number Of Steps: " + steps);
        menu_BTN_newGame.setOnClickListener(newGameBtn);
        menu_BTN_topTen.setOnClickListener(topTen);

        Glide
                .with(this)
                .load(bitmap)
                .centerCrop()
                .into(menu_IMG_winnerPic);
    }

    private void getMyIntent() {
        Intent intent = getIntent();
        winnerName = intent.getStringExtra(winnerPlayer);
        steps = intent.getIntExtra(numOfSteps,0);
        bitmap = (Bitmap) intent.getParcelableExtra(image);
    }

    private void findViews() {
        menu_IMG_winnerPic = findViewById(R.id.menu_IMG_winnerPic);
        menu_LBL_winnerName = findViewById(R.id.menu_LBL_winnerName);
        menu_LBL_numOfSteps = findViewById(R.id.menu_LBL_numOfSteps);
        menu_BTN_newGame = findViewById(R.id.menu_BTN_newGame);
        menu_BTN_topTen = findViewById(R.id.menu_BTN_topTen);
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