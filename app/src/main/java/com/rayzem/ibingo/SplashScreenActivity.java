package com.rayzem.ibingo;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private Animation animationFadeIn;
    private ImageView logo_iBingo;
    private Handler handler;
    private Button button_play;
    private LinearLayout contentHome, contentNumTables, one_table_container, two_table_container;
    AnimationSet animation;
    Animation fadeIn,fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_splash_screen);
        //logo_iBingo = findViewById(R.id.logo_iBingo);
        button_play = findViewById(R.id.btn_play);

        contentHome = findViewById(R.id.containerHome);
        contentNumTables = findViewById(R.id.containerNumTables);

        one_table_container = findViewById(R.id.one_table_match);
        two_table_container = findViewById(R.id.two_table_match);



        button_play.setOnClickListener(this);
        one_table_container.setOnClickListener(this);
        two_table_container.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, GameActivity.class);



        switch (view.getId()){
            case R.id.btn_play:
                contentNumTables.setVisibility(View.VISIBLE);
                contentHome.setVisibility(View.GONE);
                /*intent.putExtra("numBingoCards", 2);
                startActivity(intent);*/

                break;
            case R.id.one_table_match:
                intent.putExtra("numBingoCards", 1);
                startActivity(intent);
                break;

            case R.id.two_table_match:
                intent.putExtra("numBingoCards", 2);
                startActivity(intent);
                break;


        }
    }


}
