package com.rayzem.ibingo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class BingoCard extends LinearLayout implements Cell.OnToggledListener {
    private Cell[][] cells;
    private GridLayout gridLayout;
    private Context context;
    //int poolNumbers [];
    ArrayList<Integer> poolNumbers;


    public BingoCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        View v = LayoutInflater.from(context).inflate(R.layout.bingo_card_view, this, true);
        gridLayout = v.findViewById(R.id.bingoCard);

        initBingoCard(gridLayout.getColumnCount(),gridLayout.getRowCount());


    }

    /**
     * Function to init the all the bingo card
     *
     * @param numCol
     * @param numRow
     */
    public void initBingoCard(int numCol, int numRow) {
        cells = new Cell[numRow][numCol];

        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                Cell cell = new Cell(context, i, j, -1);
                cell.setToogledListener(this);
                cells[i][j] = cell;
                gridLayout.addView(cell);
            }
        }

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        final int MARGIN = 5;


                        int gridLayoutWidth = gridLayout.getWidth();
                        int gridLayoutHeight = gridLayout.getHeight();
                        int numOfCol = gridLayout.getColumnCount();
                        int numOfRow = gridLayout.getRowCount();
                        int cellWidth = gridLayoutWidth / numOfCol;
                        int cellHeight = gridLayoutHeight / numOfRow;


                        for (int i = 0; i < numOfRow; i++) {
                            for (int j = 0; j < numOfCol; j++) {
                                GridLayout.LayoutParams params = (GridLayout.LayoutParams) (cells[i][j]).getLayoutParams();
                                params.width = cellWidth - 2 * MARGIN;
                                params.height = cellHeight - 2 * MARGIN;
                                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                (cells[i][j]).setLayoutParams(params);
                            }
                        }


                        gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );

        generateBingoCardNumbers();
    }




    /**
     * B = 1 al 15
     * I = 16 al 30
     * N = 31 al 45
     * G = 46 al 60
     * O = 61 al 75
     * @return
     */
    private void generateBingoCardNumbers(){
        for(int i = 0; i < gridLayout.getColumnCount();i++){
            switch (i){
                case 0:
                    generateNumber(1, 15, i);
                    break;
                case 1:
                    generateNumber(16, 30, i);
                    break;
                case 2:
                    generateNumber(31, 45, i);
                    break;
                case 3:
                    generateNumber(46, 60, i);
                    break;
                case 4:
                    generateNumber(61, 75, i);
                    break;
                default:
                    break;
            }

        }
    }


    private void generateNumber(int min, int max, int colIndex){


        boolean isValid = false;

        for (int j = 1; j < gridLayout.getRowCount(); j++) {
            //double randomNumber = Math.random();
            isValid = false;
            while (!isValid) {
                Random ran = new Random();
                int randomInt = min + ran.nextInt(max - min + 1);
                //randomNumber = (randomNumber * max) + min;
                if (!checkGeneratedNumber(colIndex, randomInt)) {
                    cells[j][colIndex].setContent(randomInt);
                    isValid = true;
                } else {
                    isValid = false;
                }

            }


        }

    }



    public boolean checkGeneratedNumber(int y, int number){
        boolean found = false;
        for(int i = 1; i < gridLayout.getRowCount(); i++){
            if (cells[i][y].getContent() == number)
                found = true;

        }
        return found;

    }

    @Override
    public void OnToogled(Cell c, boolean selected) {
        if(c.getPositionX() != 0 ){
            String positionClicked = ""+c.getContent()+" - ("+c.getPositionX() +", " + c.getPositionY()+")";
            Toast.makeText(context, ""+positionClicked, Toast.LENGTH_LONG).show();
        }


    }

}
