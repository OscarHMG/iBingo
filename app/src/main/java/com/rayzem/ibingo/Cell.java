package com.rayzem.ibingo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Cell extends FrameLayout {
    private int content;
    private boolean selected;
    private boolean mDownTouch = false;
    private int positionX, positionY;
    Paint paint = new Paint();
    TextView bingoNUmber;
    View circleSelected;


    //Listeners
    private OnToggledListener toogledListener;

    public Cell(Context context, int x, int y, int content) {
        super(context);

        positionX = x;
        positionY = y;
        selected = false;
        this.content = content;
        //initUI(positionX, positionY, content, v);


        //Log.i("Oscar", v.toString());
    }


    public void initUI(int x, int y, int content, View v){
        bingoNUmber = v.findViewById(R.id.bingoNumber);
        circleSelected = v.findViewById(R.id.view_circle_selected);

        if(x == 0){
            switch (x) {
                case 0:
                    bingoNUmber.setText("B");

                    break;
                case 1:
                    bingoNUmber.setText("I");
                    break;
                case 2:
                    bingoNUmber.setText("N");
                    break;
                case 3:
                    bingoNUmber.setText("G");
                    break;
                case 4:
                    bingoNUmber.setText("O");
                    break;

            }
        }else if(positionY == 2 && positionX == 3){
            bingoNUmber.setText("X");
        }else{
            bingoNUmber.setText(""+content);
        }
    }


    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
        selected = false;
    }

    public Cell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        selected = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        /*int xPos = (int)((canvas.getWidth() / 2) - ((paint.descent() - paint.ascent()) / 2));
        *//*int xPos = (int) (canvas.getWidth() / 2) - 20; *//*
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));*/

        int xPos = (int)(canvas.getWidth()/2);
        int yPos = (int)((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));



        if (positionX == 0) {
            canvas.drawColor(Color.TRANSPARENT);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            switch (positionY) {
                case 0:

                    canvas.drawColor(ContextCompat.getColor(getContext(),R.color.black));
                    canvas.drawText("B", xPos, yPos, paint);
                    break;
                case 1:
                    canvas.drawColor(ContextCompat.getColor(getContext(),R.color.yellow));
                    canvas.drawText("I", xPos, yPos, paint);
                    break;
                case 2:
                    canvas.drawColor(ContextCompat.getColor(getContext(),R.color.darkGreyBlack));
                    canvas.drawText("N", xPos, yPos, paint);
                    break;
                case 3:
                    canvas.drawColor(ContextCompat.getColor(getContext(),R.color.darkBrown));
                    canvas.drawText("G", xPos, yPos, paint);
                    break;
                case 4:
                    canvas.drawColor(ContextCompat.getColor(getContext(),R.color.blue));
                    canvas.drawText("O", xPos, yPos, paint);
                    break;

            }
        }else if(positionY == 2 && positionX == 3){

            drawStart(canvas);

        }else{
            if (selected && positionX != 0) {
                //canvas.drawColor(ContextCompat.getColor(getContext(), R.color.transparent_red));
                selectBingonumber(canvas);
            } else {
                canvas.drawColor(ContextCompat.getColor(getContext(), R.color.transparent_lightGray));

            }
            paint.setColor(Color.BLACK);
            paint.setTextSize(50);
            canvas.drawText("" + content,  xPos, yPos, paint);
        }
    }


    private void drawStart(Canvas canvas){
        paint.setColor(ContextCompat.getColor(getContext(),R.color.red));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        float mid = canvas.getWidth() / 2;
        float min = Math.min(canvas.getWidth(), canvas.getHeight());
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;

        paint.setStrokeWidth(fat);
        paint.setStyle(Paint.Style.STROKE);

        canvas.drawCircle(mid + half, half, rad, paint);
        Path path = new Path();

        path.reset();

        paint.setStyle(Paint.Style.FILL);


        // top left
        path.moveTo(mid + half * 0.5f, half * 0.84f);
        // top right
        path.lineTo(mid + half * 1.5f, half * 0.84f);
        // bottom left
        path.lineTo(mid + half * 0.68f, half * 1.45f);
        // top tip
        path.lineTo(mid + half * 1.0f, half * 0.5f);
        // bottom right
        path.lineTo(mid + half * 1.32f, half * 1.45f);
        // top left
        path.lineTo(mid + half * 0.5f, half * 0.84f);

        path.close();
        canvas.drawPath(path, paint);

    }


    private void selectBingonumber(Canvas canvas){
        /*
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);*/

        float mid = canvas.getWidth() / 2;
        float min = Math.min(canvas.getWidth(), canvas.getHeight());
        float fat = min / 17;
        float half = min / 2;
        float rad = half - fat;
        mid = mid - half;

        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(ContextCompat.getColor(getContext(),R.color.transparent_red));

        canvas.drawCircle(mid + half, half, rad, paint);
        //canvas.drawColor(ContextCompat.getColor(getContext(),R.color.transparent_red));
        paint.setStrokeWidth(0);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                selected = !selected;
                invalidate();
               /* if(selected)
                    circleSelected.setVisibility(View.VISIBLE);
                else
                    circleSelected.setVisibility(View.GONE);*/

                //invalidate(); //Call onDraw event, to redraw the canvas

                if(toogledListener != null)
                    toogledListener.OnToogled(this, selected);

                mDownTouch = true;
                return true;

            case MotionEvent.ACTION_UP:
                if(mDownTouch){
                    mDownTouch = false;
                    performClick();
                    return true;

                }
        }
        return false;
    }


    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }


    // GETTERS AND SETTERS

    public void setToogledListener(OnToggledListener toogledListener) {
        this.toogledListener = toogledListener;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
        this.invalidate();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //Interface to handle click events.
    public interface OnToggledListener {
        void OnToogled(Cell c, boolean selected);
    }

}
