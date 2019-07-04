package com.rayzem.ibingo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class Cell extends View {
    private int content;
    private boolean selected;
    private boolean mDownTouch = false;
    private int positionX, positionY;
    Paint paint = new Paint();

    //Listeners
    private OnToggledListener toogledListener;

    public Cell(Context context, int x, int y, int content) {
        super(context);
        positionX = x;
        positionY = y;
        selected = false;
        this.content = content;
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
        //setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        int xPos = (canvas.getWidth() / 2);
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        //First, need to paint the background
        if (selected) {
            canvas.drawColor(Color.RED);
        } else {
            canvas.drawColor(Color.LTGRAY);
        }


        if (positionX == 0) {
            paint.setColor(Color.RED);
            paint.setTextSize(30);
            switch (positionY) {
                case 0:
                    canvas.drawText("B", xPos, yPos, paint);
                    break;
                case 1:
                    canvas.drawText("I", xPos, yPos, paint);
                    break;
                case 2:
                    canvas.drawText("N", xPos, yPos, paint);
                    break;
                case 3:
                    canvas.drawText("G", xPos, yPos, paint);
                    break;
                case 4:
                    canvas.drawText("O", xPos, yPos, paint);
                    break;

            }
        }else if(positionY == 2 && positionX == 3){
            paint.setColor(Color.BLACK);
            paint.setTextSize(40);
            canvas.drawText("X",  xPos, yPos, paint);
        }else{
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            canvas.drawText("" + content,  xPos, yPos, paint);
        }



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                selected = !selected;
                invalidate(); //Call onDraw event, to redraw the canvas

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
    }

    //Interface to handle click events.
    public interface OnToggledListener {
        void OnToogled(Cell c, boolean selected);
    }

}
