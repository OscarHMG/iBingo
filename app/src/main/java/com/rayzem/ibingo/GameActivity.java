package com.rayzem.ibingo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity implements BingoCard.BingoWinInterface{
    Handler handler = new Handler();
    private ArrayList<Integer> poolNumbers;
    private ArrayList<BingoCard> bingoCards;
    private LinearLayout contentPanel;
    BingoCard b1, b2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        poolNumbers = new ArrayList<>();
        poolNumbers.add(new Integer(0));
        bingoCards = new ArrayList<>();

        contentPanel = findViewById(R.id.contentPanel);

        b1 = new BingoCard(this, null, "HORIZONTAL");
        //b2 = new BingoCard(this, null);



        contentPanel.addView(b1);
        //contentPanel.addView(b2);

        bingoCards.add(b1);
        //bingoCards.add(b2);


        setLayoutParameters();
        b1.setBingoWinInterface(this);



    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.post(runnableCode);


    }

    private void setLayoutParameters(){
        for(BingoCard bc: bingoCards){
            LinearLayout.LayoutParams properties = (LinearLayout.LayoutParams) bc.getLayoutParams();
            properties.setMargins(15,25,5,10);

            properties.width = 0;
            properties.height = (int)(280 * (this.getResources().getDisplayMetrics().density));
            properties.weight = bingoCards.size() == 1 ? 30 : 50;
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
            int randomInt = 1 + ran.nextInt(75 - 1 + 1);
            Toast.makeText(GameActivity.this, "NUMERO: " + randomInt, Toast.LENGTH_LONG).show();

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
                Log.i("OSCAR", "Bingo!!!!!!!!!!!!!!!!!!!");

        }else{
            //False alarm
            Log.i("OSCAR", "No bingo!");
            
        }
    }



}
