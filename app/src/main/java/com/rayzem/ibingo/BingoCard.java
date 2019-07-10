package com.rayzem.ibingo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class BingoCard extends LinearLayout implements Cell.OnToggledListener, View.OnClickListener {
    private Cell[][] cells;
    private GridLayout gridLayout;
    private Button bingoButton;
    private LinearLayout contentPanel;
    private Context context;
    //int poolNumbers [];

    private BingoWinInterface bingoWinInterface;

    private ArrayList<Integer> poolNumbers;

    //Simulate bingo



    //This information come from the SOCKET - HOW TO WIN : HORIZONTAL, VERTICAL, DIAGONAL, CORNERS.
    private String PATRON_TO_WIN;


    public BingoCard(Context context, AttributeSet attrs, String patron) {
        super(context, attrs);
        this.context = context;

        poolNumbers = new ArrayList<>();
        this.PATRON_TO_WIN = patron;

        View v = LayoutInflater.from(context).inflate(R.layout.bingo_card_view, this, true);

        gridLayout = v.findViewById(R.id.bingoCard);
        bingoButton = v.findViewById(R.id.bingoButton);

        initBingoCard(gridLayout.getColumnCount(),gridLayout.getRowCount());


        bingoButton.setOnClickListener(this);




        /*bingoButton.setOnClickListener(this);
        LayoutParams layoutParams = (LayoutParams) bingoButton.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        bingoButton.setLayoutParams(layoutParams);*/

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

                        int MARGIN = 5;

                        //Here draw the grid dinamycally. IMPORTANT: In height, i have to substract 100 dp to
                        // make the button appear in the bottom.
                        int gridLayoutWidth = gridLayout.getWidth();
                        int gridLayoutHeight = gridLayout.getHeight() - 150;
                        int numOfCol = gridLayout.getColumnCount();
                        int numOfRow = gridLayout.getRowCount();
                        int cellWidth = gridLayoutWidth / numOfCol;
                        int cellHeight = gridLayoutHeight / numOfRow;


                        for (int i = 0; i < numOfRow; i++) {

                            MARGIN =  i == 0 ? 0: 5;

                            for (int j = 0; j < numOfCol; j++) {
                                GridLayout.LayoutParams params = (GridLayout.LayoutParams) (cells[i][j]).getLayoutParams();
                                //params.width = cellWidth - 2 * MARGIN;
                                params.width = cellHeight - 2*MARGIN;
                                params.height = cellHeight - 2*MARGIN;


                                params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                                (cells[i][j]).setLayoutParams(params);
                                cells[i][j].setBackgroundColor(getResources().getColor(R.color.transparent_gray));
                            }
                        }


                        gridLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);



                        //gridLayout.setBackground(getResources().getDrawable(R.drawable.wood_texture));


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

        //In the center set -1, and set selected.
        cells[3][2].setSelected(true);
        cells[3][2].setContent(0);
    }


    private void generateNumber(int min, int max, int colIndex){
        boolean isValid;
        for (int j = 1; j < gridLayout.getRowCount(); j++) {

            isValid = false;
            while (!isValid) {


                Random ran = new Random();
                int randomInt = min + ran.nextInt(max - min + 1);
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


        }


    }


    public ArrayList<Integer> sendToVerifyWinnerNumbers(){
        ArrayList<Integer> winnerNumbers = new ArrayList<>();
        switch (PATRON_TO_WIN){
            case "HORIZONTAL":
                winnerNumbers = verifyHorizontalBingo();
                break;
            case "VERTICAL":
                winnerNumbers = verifyVerticalHorizontal();
                break;

            case "DIAGONAL":
                //check 1st, 1 diagonal
                winnerNumbers = verifyAscendentDiagonal();
                //If this is empty, check the other
                if(winnerNumbers.isEmpty())
                    winnerNumbers = verifyDescendentDiagonal();

                break;
            case "CORNERS":
                verifyCornersBingo();
                break;
        }

        return winnerNumbers;
    }

    public ArrayList<Integer> verifyHorizontalBingo(){
        ArrayList<Integer> winnerNumbers = new ArrayList<>();
        int cont = 0;
        boolean isBingo = false;
        for(int i = 1; i< gridLayout.getRowCount(); i++){
            for(int j = 0; j < gridLayout.getColumnCount(); j++){
                if(cells[i][j].isSelected()){
                    cont ++;
                    //Log.i("OSCAR", "REVISANDO: "+cells[i][j].getContent());
                    winnerNumbers.add(new Integer(cells[i][j].getContent()));
                }else{
                    cont = 0;
                    winnerNumbers.clear();
                    break;
                }
            }

            if(cont == 5) {
                Log.i("OSCAR", "REVISANDO: BINGO!" );
                return winnerNumbers;
            }else{
                Log.i("OSCAR", "REVISANDO: LINEA SIGUIENTE!" );
            }

        }
        Log.i("OSCAR", "REVISANDO: NO HUBO BINGO" );
        return winnerNumbers;
    }

    public ArrayList<Integer> verifyVerticalHorizontal(){
        int cont = 0;
        ArrayList<Integer> winnerNumbers = new ArrayList<>();

        for(int j = 0; j< gridLayout.getColumnCount(); j++){
            for(int i = 1; i < gridLayout.getRowCount(); i++){

                if(cells[i][j].isSelected()){
                    cont ++;
                    //Log.i("OSCAR", "REVISANDO: "+cells[i][j].getContent());
                    winnerNumbers.add(new Integer(cells[i][j].getContent()));
                }else{
                    cont = 0;
                    winnerNumbers.clear();
                    break;
                }

                if(cont == 5) {
                    Log.i("OSCAR", "VERTICAL REVISANDO: linea completa vertical!" );
                    return winnerNumbers;
                }else{
                    Log.i("OSCAR", "REVISANDO: LINEA SIGUIENTE! no vertical" );
                }
            }
        }

        return winnerNumbers;
    }

    public ArrayList<Integer> verifyCornersBingo(){
        ArrayList<Integer> winnerNumbers = new ArrayList<>();

        if(cells[1][0].isSelected() && cells[5][0].isSelected() && cells[1][4].isSelected() && cells[5][4].isSelected()){
            winnerNumbers.add(cells[1][0].getContent());
            winnerNumbers.add(cells[5][0].getContent());
            winnerNumbers.add(cells[1][4].getContent());
            winnerNumbers.add(cells[5][4].getContent());
            Log.i("OSCAR","CORNERS BINGO!");
        }

        return winnerNumbers;
    }


    public ArrayList<Integer> verifyDescendentDiagonal(){
        ArrayList<Integer> winnerNumbers = new ArrayList<>();

        if(cells[1][0].isSelected() && cells[2][1].isSelected() && cells[3][2].isSelected() && cells[4][3].isSelected() && cells[5][4].isSelected() ){
            winnerNumbers.add(cells[1][0].getContent());
            winnerNumbers.add(cells[2][1].getContent());
            //winnerNumbers.add(cells[3][2].getContent());
            winnerNumbers.add(cells[4][3].getContent());
            winnerNumbers.add(cells[5][4].getContent());
            Log.i("OSCAR","DIAGONAL BINGO!");

        }

        return winnerNumbers;
    }


    public ArrayList<Integer> verifyAscendentDiagonal(){
        ArrayList<Integer> winnerNumbers = new ArrayList<>();

        if(cells[5][0].isSelected() && cells[4][1].isSelected() && cells[3][2].isSelected() && cells[2][3].isSelected() && cells[1][4].isSelected() ){
            winnerNumbers.add(cells[1][0].getContent());
            winnerNumbers.add(cells[2][1].getContent());
            //winnerNumbers.add(cells[3][2].getContent());
            winnerNumbers.add(cells[4][3].getContent());
            winnerNumbers.add(cells[5][4].getContent());
            Log.i("OSCAR","DIAGONAL ASCENDENTE BINGO!");

        }

        return winnerNumbers;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bingoButton:
                Toast.makeText(context, "BINGO!", Toast.LENGTH_SHORT).show();

                bingoWinInterface.verifyBingoNumbers(sendToVerifyWinnerNumbers());
                break;
        }
    }

    public void setBingoWinInterface(BingoWinInterface bingoWinInterface) {
        this.bingoWinInterface = bingoWinInterface;
    }

    public interface BingoWinInterface {
        void verifyBingoNumbers(ArrayList<Integer> number);
    }
}
