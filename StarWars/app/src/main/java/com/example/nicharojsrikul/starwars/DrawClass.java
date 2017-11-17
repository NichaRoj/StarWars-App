package com.example.nicharojsrikul.starwars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Nicha Rojsrikul on 8/11/2560. 5931259621
 */

public class DrawClass extends View {
    public int viewWidth;
    public int viewHeight;
    //text variables
    private final String TEXT1 = "A long time ago,";
    private final String TEXT2 = "In a galaxy far, far away...";
    public float textSize;
    public float dTextSize;
    public float textY;
    public float dTextY;
    public boolean textRunning;
    //ship variables
    public float ship1X;
    public float ship1Y;
    public float ship2X;
    public float dShip2X;
    public float ship2Y;
    public float bulletX;
    public float dBulletX;
    public float bulletY;
    public boolean outOfView;
    public boolean ended;

    public DrawClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewTreeObserver viewTO = getViewTreeObserver();
        if (viewTO.isAlive()) {
            viewTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    viewWidth = getWidth();
                    viewHeight = getHeight();
                }
            });
        }
        dTextSize = 0;
        dTextY = 0;
        dShip2X = 0;
        dBulletX = 0;

        textRunning = true;
        ended = false;
    }

    @Override
    public Parcelable onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        //save variables using outState.put<>(String name, Object value)
        bundle.putFloat("dTextSize",this.dTextSize);
        bundle.putFloat("dTextY", this.dTextY);
        bundle.putBoolean("textRunning", this.textRunning);
        bundle.putFloat("dShip2X", this.dShip2X);
        bundle.putFloat("dBulletX", this.dBulletX);
        bundle.putBoolean("ended", this.ended);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state){
        if (state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            this.dTextSize = bundle.getFloat("dTextSize");
            this.dTextY = bundle.getFloat("dTextY");
            this.textRunning = bundle.getBoolean("textRunning");
            this.dShip2X = bundle.getFloat("dShip2X");
            this.dBulletX = bundle.getFloat("dBulletX");
            this.ended = bundle.getBoolean("ended");
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        textY = viewHeight-dTextY;
        textSize = 80-dTextSize;
        ship2X = viewWidth-dShip2X;
        ship2Y = -ship2X +viewWidth;
        ship1X = ship2X +500;
        ship1Y = ship2Y-500;
        bulletX = ship2X+550-dBulletX;
        bulletY = -bulletX+viewWidth+50;
        Paint white = new Paint();
        white.setARGB(255, 255, 255, 255);
        white.setStyle(Paint.Style.FILL_AND_STROKE);
        white.setTextSize(60);
        Paint red = new Paint();
        red.setARGB(255, 200, 0, 0);
        red.setStyle(Paint.Style.FILL_AND_STROKE);
        Paint yellow = new Paint();
        yellow.setARGB(255, 255, 255, 0);
        yellow.setTextSize(textSize);
        c.drawARGB(255, 10, 10, 100);
        c.drawText("Nicha Rojsrikul", 0, 60, white);

        if (textRunning) {
            //draw text
            c.drawText(TEXT1, viewWidth / 2 - yellow.measureText(TEXT1) / 2, textY, yellow);
            c.drawText(TEXT2, viewWidth / 2 - yellow.measureText(TEXT2) / 2, textY + textSize, yellow);
            if(textY < -80){
                textRunning = false;
            }
        } else {
            //draw ship
            Path ship1 = new Path();
            ship1.moveTo(ship1X, ship1Y);
            ship1.lineTo(ship1X+120, ship1Y+120);
            ship1.lineTo(ship1X-120, ship1Y+240);
            ship1.lineTo(ship1X, ship1Y);


            Path ship2 = new Path();
            ship2.moveTo(ship2X, ship2Y);
            ship2.lineTo(ship2X +60, ship2Y+60);
            ship2.lineTo(ship2X -60, ship2Y+120);
            ship2.lineTo(ship2X, ship2Y);

            Path bullet = new Path();
            bullet.moveTo(bulletX, bulletY);
            bullet.lineTo(bulletX+20, bulletY+20);
            bullet.lineTo(bulletX-20, bulletY+40);
            bullet.lineTo(bulletX, bulletY);

            c.drawPath(ship1, red);
            c.drawPath(ship2, white);
            c.drawPath(bullet, yellow);
        }

        if(ended){
            c.drawText("The End", viewWidth / 2 - yellow.measureText("The End") / 2, viewHeight/2, yellow);
        }

    }
}
