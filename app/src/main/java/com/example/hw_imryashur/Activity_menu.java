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

public class Activity_menu extends AppCompatActivity {
    private ImageView menu_IMG_winnerPic;
    private TextView menu_LBL_winnerName;
    private Button menu_BTN_newGame;
    private String winnerName;
    private Bitmap bitmap;
    public static final String winnerPlayer = "winnerPlayer";
    public static final String image = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        getMyIntent();
        menu_LBL_winnerName.setText(winnerName);
        menu_BTN_newGame.setOnClickListener(newGameBtn);

        Glide
                .with(this)
                .load(bitmap)
                .centerCrop()
                .into(menu_IMG_winnerPic);
    }

    private void getMyIntent() {
        Intent intent = getIntent();
        winnerName = intent.getStringExtra(winnerPlayer);
        bitmap = (Bitmap) intent.getParcelableExtra(image);
    }

    private void findViews() {
        menu_IMG_winnerPic = findViewById(R.id.menu_IMG_winnerPic);
        menu_LBL_winnerName = findViewById(R.id.menu_LBL_winnerName);
        menu_BTN_newGame = findViewById(R.id.menu_BTN_newGame);
    }

    private View.OnClickListener newGameBtn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(Activity_menu.this, Activity_main.class);
            Activity_menu.this.startActivity(myIntent);
            finish();
        }
    };
}