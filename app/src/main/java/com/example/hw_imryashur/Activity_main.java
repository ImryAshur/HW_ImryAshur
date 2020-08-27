package com.example.hw_imryashur;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.Random;

public class Activity_main extends AppCompatActivity {

    public static final Random RANDOM = new Random();
    private Button main_BTN_rollDices;
    private ImageView main_IMG_cube1, main_IMG_cube2, main_IMG_ronaldo, main_IMG_messi;
    int ronaldoScore , messiScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initviews();
        main_BTN_rollDices.setOnClickListener(cubeClickListener);

    }

    private void initviews() {
        glide(R.drawable.ronaldo,main_IMG_ronaldo);
        glide(R.drawable.messi,main_IMG_messi);
        glide(R.drawable.dice_6,main_IMG_cube1);
        glide(R.drawable.dice_6,main_IMG_cube2);
    }

    private void findViews() {
        main_BTN_rollDices = findViewById(R.id.main_BTN_rollDices);
        main_IMG_cube1 = findViewById(R.id.main_IMG_cube1);
        main_IMG_cube2 = findViewById(R.id.main_IMG_cube2);
        main_IMG_ronaldo = findViewById(R.id.main_IMG_ronaldo);
        main_IMG_messi = findViewById(R.id.main_IMG_messi);
    }

    private void glide(int img, ImageView into){
        Glide
                .with(Activity_main.this)
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
            final Animation anim1 = AnimationUtils.loadAnimation(Activity_main.this, R.anim.shake);
            final Animation anim2 = AnimationUtils.loadAnimation(Activity_main.this, R.anim.shake);
            final Animation.AnimationListener animationListener = new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    int value = randomDiceValue();
                    int res = getResources().getIdentifier("dice_" + value, "drawable", "com.example.hw_imryashur");

                    if (animation == anim1) {
                        glide(res,main_IMG_cube1);
                        ronaldoScore = value;
                        Log.d("pttt", "ronaldo Score : " + ronaldoScore);
                        //main_IMG_cube1.setImageResource(res);
                    } else if (animation == anim2) {
                        glide(res,main_IMG_cube2);
                        messiScore = value;
                        Log.d("pttt", "messi Score : " + messiScore);
                        whoStart();
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
        if(ronaldoScore > messiScore) startGame(ronaldoScore + " - " + messiScore+ " -> Ronaldo Start!" , true);
        else if(ronaldoScore < messiScore) startGame(ronaldoScore + " - " + messiScore+ " -> Messi Start!" , false);
        else MySignalV2.getInstance().showToast("It's A Tie Roll Again!");
    }

    private void startGame(String name, boolean ronaldoTurn) {
        MySignalV2.getInstance().showToast(name);
        Intent intent = new Intent(Activity_main.this,Activity_game.class);
        intent.putExtra(Activity_game.EXTRA_KEY_TURN,ronaldoTurn);
        startActivity(intent);
        finish();
    }
}