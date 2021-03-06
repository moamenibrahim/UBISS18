package com.example.moamen.ubiss;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.abs;

public class CanvasView extends View {

    public static int ourX;
    public static int ourY;
    public static float userError;
    private Paint mPaint;
    public int width, height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    Context context;
    public static double result;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y) {
        float dx = abs(x - mX);
        float dy = abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(y > ourY) {
            Log.println(Log.INFO, "userErr", Float.toString(userError));
            ResultActivity.userResult = abs((int) (((22000 - userError)/22000)*100));
            Intent intent = new Intent(context, ResultActivity.class);
            disconnectmyoGetResult();
            if (ResultActivity.myoResult > 0){context.startActivity(intent);}
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                userError = userError + abs((float) ourX - x);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                break;
        }
        return true;
    }


    private void disconnectmyoGetResult(){
        Connectmyo connectmyoinstance = new Connectmyo();
        //connectmyoinstance.disconnectMyo();
        result=connectmyoinstance.Total/connectmyoinstance.count;
        ResultActivity.myoResult = abs((int) ( 100 * result));
        Log.println(Log.INFO, "myoTest", Double.toString(result));
        Log.println(Log.INFO, "myoTest", Integer.toString(ResultActivity.myoResult));
        connectmyoinstance.Total = 0;
        connectmyoinstance.count = 0;
    }
}
