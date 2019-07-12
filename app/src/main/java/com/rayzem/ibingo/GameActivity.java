package com.rayzem.ibingo;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements BingoCard.BingoWinInterface, View.OnClickListener, TextToSpeech.OnInitListener {
    Handler handler = new Handler();
    private ArrayList<Integer> poolNumbers;
    private ArrayList<PoolNumber> poolNumbersViews;
    private ArrayList<BingoCard> bingoCards;
    private LinearLayout contentPanel, column_1_pool_number, column_2_pool_number, column_3_pool_number,
            column_4_pool_number, column_5_pool_number, containerGeneralInfo, containerPoolNumbers;
    private TextView actualBingoNumber;
    private BingoCard b1, b2;
    private ImageButton buttonHome, buttonShowBingoNumbers;
    private String GAME_TYPE = "VERTICAL";
    private ImageView bingo_ball_image, pattern_game;
    private  TextView tv_pattern_game;
    private int[] bingoBallColors = {R.drawable.black_bingo_ball, R.drawable.blue_bingo_ball, R.drawable.dark_purple_bingo_ball,
                              R.drawable.gray_bingo_ball, R.drawable.light_blue_bingo_ball, R.drawable.orange_bingo_ball,
                              R.drawable.purple_bingo_ball, R.drawable.red_bingo_ball,R.drawable.turquoise_bingo_ball, R.drawable.yellow_bingo_ball };

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textToSpeech = new TextToSpeech(this, this);

        int numBingoCards = getIntent().getIntExtra("numBingoCards", 1);

        poolNumbers = new ArrayList<>();
        poolNumbersViews = new ArrayList<>();

        poolNumbers.add(new Integer(0));

        bingoCards = new ArrayList<>();

        contentPanel = findViewById(R.id.contentPanel);
        column_1_pool_number = findViewById(R.id.column_1_pool_number);
        column_2_pool_number = findViewById(R.id.column_2_pool_number);
        column_3_pool_number = findViewById(R.id.column_3_pool_number);
        column_4_pool_number = findViewById(R.id.column_4_pool_number);
        column_5_pool_number = findViewById(R.id.column_5_pool_number);
        containerGeneralInfo = findViewById(R.id.containerGeneralInfo);
        containerPoolNumbers = findViewById(R.id.containerPoolNumbers);

        actualBingoNumber = findViewById(R.id.actualBingoNumber);
        bingo_ball_image = findViewById(R.id.bingo_ball_image);
        pattern_game = findViewById(R.id.pattern_game);
        tv_pattern_game = findViewById(R.id.tv_pattern_game);
        buttonHome = findViewById(R.id.button_home);
        buttonShowBingoNumbers = findViewById(R.id.show_all_bingo_numbers);

        initBingoCards(numBingoCards);

    }

    @Override
    protected void onResume() {
        super.onResume();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if(handler != null){
            handler.removeCallbacks(runnableCode);
        }
    }

    public void initBingoCards(int numBingoCards){
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
        setPatternBingoGame();

        setPoolNumbers();
    }


    private void setPoolNumbers(){
        PoolNumber poolNumber;
        LinearLayout.LayoutParams params;
        for(int i=1; i<=75; i++){
            poolNumber = new PoolNumber(i, false, this);
            if(i<=15)
                column_1_pool_number.addView(poolNumber.getTextView());
            else if(i >= 16 && i<=30)
                column_2_pool_number.addView(poolNumber.getTextView());
            else if(i >= 31 && i<=45)
                column_3_pool_number.addView(poolNumber.getTextView());
            else if(i >= 46 && i <= 60)
                column_4_pool_number.addView(poolNumber.getTextView());
            else if(i>=61 && i<=75)
                column_5_pool_number.addView(poolNumber.getTextView());

            params = (LinearLayout.LayoutParams)poolNumber.getTextView().getLayoutParams();
            params.setMargins(5, 0, 5, -10); //substitute parameters for left, top, right, bottom
            poolNumber.getTextView().setLayoutParams(params);
            poolNumbersViews.add(poolNumber);
        }
    }

    private void setPatternBingoGame(){
        int idPatternImage = 0;
        String patternString = "";
        switch (GAME_TYPE){
            case "HORIZONTAL":
                idPatternImage = R.drawable.horizontal_pattern;
                patternString = "Horizontal";
                break;
            case "VERTICAL":
                idPatternImage = R.drawable.vertical_pattern;
                patternString = "Vertical";
                break;

            case "DIAGONAL":
                idPatternImage = R.drawable.diagonal_pattern;
                patternString = "Diagonales";
                break;
            case "CORNERS":
                idPatternImage = R.drawable.corner_pattern;
                patternString = "4 Esquinas";
                break;
        }

        pattern_game.setImageResource(idPatternImage);
        tv_pattern_game.setText(patternString);
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

            speechNumberBingo(""+randomInt);

            //Add the number, to the pool number.
            poolNumbers.add(new Integer(randomInt));

            for(PoolNumber p: poolNumbersViews){
                if(p.getNumber() == randomInt) {
                    p.getTextView().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red_2));
                    p.getTextView().setTypeface(p.getTextView().getTypeface(), Typeface.BOLD);
                }
            }

            handler.postDelayed(this, 8000);
        }
    };


    @Override
    public void verifyBingoNumbers(ArrayList<Integer> number) {

        if(!number.isEmpty()){
            if(poolNumbers.containsAll(number));
            handler.removeCallbacks(runnableCode);
            Toast.makeText(this, "BINGO!!", Toast.LENGTH_LONG).show();

        }else{
            //False alarm
            Toast.makeText(this, "Bingo no valido. Revisa tu tabla", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_home:
                confirmationAlertDialog(this);

                break;
            case R.id.show_all_bingo_numbers:
                containerGeneralInfo.setVisibility(View.GONE);
                containerPoolNumbers.setVisibility(View.VISIBLE);
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

    private void speechNumberBingo(String number){
        textToSpeech.speak(number, TextToSpeech.QUEUE_FLUSH, null);

        if(number.length() == 2) {
            for (int i = 0; i < number.length(); i++) {
                textToSpeech.speak(Character.toString(number.charAt(i)), TextToSpeech.QUEUE_ADD, null);
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.getDefault());
            //Load voice then start random numbers


            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("TextToSpeech", "Not working.");
            } else {
                Log.i("TextToSpeech", "Working.");

            }

        } else {
            Log.e("TextToSpeech", "Initilization Failed!");
        }

        handler.post(runnableCode);
    }
}
