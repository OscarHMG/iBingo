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

public class BingoCard extends LinearLayout implements Cell.OnToggledListener {
    private Cell[] cells;
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
        cells = new Cell[numCol * numRow];
        int cont = 0;


        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numCol; j++) {
                double randomNumber = Math.random();
                randomNumber = randomNumber * 75 + 1;
                Cell cell = new Cell(context, i, j, (int) randomNumber);
                cell.setToogledListener(this);
                cells[cont] = cell;

                cont++;

                gridLayout.addView(cell);
            }
        }

        gridLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        final int MARGIN = 5;
                        int numCell = 0;

                        int gridLayoutWidth = gridLayout.getWidth();
                        int gridLayoutHeight = gridLayout.getHeight();
                        int numOfCol = gridLayout.getColumnCount();
                        int numOfRow = gridLayout.getRowCount();
                        int cellWidth = gridLayoutWidth / numOfCol;
                        int cellHeight = gridLayoutHeight / numOfRow;


                        for(int i = 0 ; i < numOfCol * numOfRow; i++){
                            GridLayout.LayoutParams params = (GridLayout.LayoutParams) cells[numCell].getLayoutParams();
                            params.width = cellWidth - 2 * MARGIN;
                            params.height = cellHeight - 2 * MARGIN;
                            params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                            cells[numCell].setLayoutParams(params);
                            numCell++;
                        }


                        gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
        );
    }


    /**
     * B = 1 al 15
     * I = 16 al 30
     * N = 31 al 45
     * G = 46 al 60
     * O = 61 al 75
     * @param letter
     * @return
     */
    private int generateBingoNumber(String letter){
        int min = 0, max = 0;

        switch (letter){
            case "B":
                generateNumber(1, 15);
                break;
            case "I":
                generateNumber(16, 30);
                break;
            case "N":
                
                break;
            case "G":
                break;
            case "O":
                break;
            default:
                break;
        }

        return 0;
    }


    private int generateNumber(int min, int max){
        boolean isValid = false;
        double randomNumber = Math.random();
        int numGenerated = -1;

        while(!isValid){
            numGenerated = (int) randomNumber * min + max;
            for(int col = 0; col< cells.length; col++){
                if(cells[col].getContent() == numGenerated){
                    break;
                }else{
                    isValid = true;
                }
            }
        }
        return numGenerated;
    }
    /**/


    @Override
    public void OnToogled(Cell c, boolean selected) {
        String positionClicked = c.getPositionX() +", " + c.getPositionY();
        Toast.makeText(context, "Position: "+positionClicked, Toast.LENGTH_LONG).show();

    }

}
