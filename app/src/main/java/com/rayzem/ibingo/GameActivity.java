package com.rayzem.ibingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements BingoCard.BingoWinInterface, View.OnClickListener {
    Handler handler = new Handler();
    private ArrayList<Integer> poolNumbers;
    private ArrayList<BingoCard> bingoCards;
    private LinearLayout contentPanel;
    private TextView actualBingoNumber;
    BingoCard b1, b2;
    private ImageButton buttonHome, buttonShowBingoNumbers;
    String GAME_TYPE = "VERTICAL";
    private ImageView bingo_ball_image, pattern_game;
    private int[] bingoBallColors = {R.drawable.black_bingo_ball, R.drawable.blue_bingo_ball, R.drawable.dark_purple_bingo_ball,
                              R.drawable.gray_bingo_ball, R.drawable.light_blue_bingo_ball, R.drawable.orange_bingo_ball,
                              R.drawable.purple_bingo_ball, R.drawable.red_bingo_ball,R.drawable.turquoise_bingo_ball, R.drawable.yellow_bingo_ball };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //On SomeActivity :

        int numBingoCards = getIntent().getIntExtra("numBingoCards", 1);

        poolNumbers = new ArrayList<>();
        poolNumbers.add(new Integer(0));
        bingoCards = new ArrayList<>();

        contentPanel = findViewById(R.id.contentPanel);
        actualBingoNumber = findViewById(R.id.actualBingoNumber);
        bingo_ball_image = findViewById(R.id.bingo_ball_image);
        pattern_game = findViewById(R.id.pattern_game);
        buttonHome = findViewById(R.id.button_home);
        buttonShowBingoNumbers = findViewById(R.id.show_all_bingo_numbers);



        b1 = new BingoCard(this, null, GAME_TYPE);

        contentPanel.addView(b1,0);


        bingoCards.add(b1);
        b1.setBingoWinInterface(this);


        if(numBingoCards == 2){
            b2 = new BingoCard(this, null, "HORIZONTAL");
            contentPanel.addView(b2, 0);
            bingoCards.add(b2);
            b2.setBingoWinInterface(this);
        }

        setLayoutParameters();

        buttonHome.setOnClickListener(this);
        buttonShowBingoNumbers.setOnClickListener(this);
        setPattern();

    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.post(runnableCode);


    }


    private void setPattern(){
        int idPatternImage = 0;
        switch (GAME_TYPE){
            case "HORIZONTAL":
                idPatternImage = R.drawable.horizontal_pattern;
                break;
            case "VERTICAL":
                idPatternImage = R.drawable.vertical_pattern;
                break;

            case "DIAGONAL":
                idPatternImage = R.drawable.diagonal_pattern;

                break;
            case "CORNERS":
                idPatternImage = R.drawable.corner_pattern;
                break;
        }

        pattern_game.setImageResource(idPatternImage);

    }
    private void setLayoutParameters(){


        for(BingoCard bc: bingoCards){
            LinearLayout.LayoutParams properties = (LinearLayout.LayoutParams) bc.getLayoutParams();
            properties.setMargins(0,0,5,0);

            properties.width = 0;
            properties.height = (int)(280 * (this.getResources().getDisplayMetrics().density));
            properties.weight = bingoCards.size() == 1 ? 50 : 25;
            properties.gravity = Gravity.CENTER_HORIZONTAL;


            bc.setLayoutParams(properties);
        }

    }


    /**
     * FAKE - Server numbers to play bingo
     */
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            Random ran = new Random();
            Random randomColor = new Random();
            //Random number
            int randomInt = 1 + ran.nextInt(75 - 1 + 1);

            //Random color
            int randomIntColor = randomColor.nextInt(10);
            bingo_ball_image.setImageResource(bingoBallColors[randomIntColor]);
            actualBingoNumber.setText(""+randomInt);

            //Add the number, to the pool number.
            poolNumbers.add(new Integer(randomInt));


            handler.postDelayed(this, 8000);
        }
    };


    @Override
    public void verifyBingoNumbers(ArrayList<Integer> number) {
        handler.removeCallbacks(runnableCode);
        if(!number.isEmpty()){
            if(poolNumbers.containsAll(number));
            Toast.makeText(this, "BINGO!!", Toast.LENGTH_LONG).show();

        }else{
            //False alarm
            Toast.makeText(this, "No es bingo. Revisa tu tabla", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_home:
                confirmationAlertDialog(this);

                break;
            case R.id.show_all_bingo_numbers:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        confirmationAlertDialog(this);
    }

    private void confirmationAlertDialog(final GameActivity gameActivity){
        new AlertDialog.Builder(this)
                .setTitle("iBingo")
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Deseas salir de la partida actual?")
                .setCancelable(false)
                .setPositiveButton("Si, deseo salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(GameActivity.this, SplashScreenActivity.class);
                        startActivity(intent);
                        gameActivity.finish();


                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}
