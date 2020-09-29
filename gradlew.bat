package com.example.test.shundemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by DreamLost on 2020/7/10 at 15:49
 * Description:
 */
public class ProcessDirectionLine extends View implements ViewNode{
    public interface LineCallBack {
        public void onClickLine(ProcessDirectionLine line, float x, float y);
    }

    LineCallBack lineCallBack = null;

    private Context context;
    private Matrix matrixPoint, matrixArrow ;
    private Bitmap point, arrow;
    private float pointX,pointY,arrowX, arrowY;
    private RectF titleRect, srcRect1, dstRect1, srcRect2, dstRect2, srcRect3, dstRect3;
    private final int LINE_HALF_WIDTH = 20;  //线的三个折线的半宽
    float extra = 60f;
    private int arrowHeight;
    private Paint paint, rectPaint, linePaint, textPaint;
    private Point centerP, assistP1, assistP2, assistP3, assistP4;
    private Path path;
    private String title;
    private String key;
    private ProcessBox nextBox,preBox;
    private int drawStyle = LINE_REAL;


    /* 划线的类型 */
    public static final int LINE_NONE = 0;    //默认
    public static final int LINE_TRY = 1;     //试划线
    public static final int LINE_REAL = 2;    //最终划线

    /* 线的起点 终点 */
    public static final int LINE_START = 0;     //起点
    public static final int LINE_END = 1;       //终点

    public ProcessDirectionLine(Context context) {
        super(context);
        this.context = context;
        initData();
    }

    public ProcessDirectionLine(Context context, float pointX, float pointY, float arrowX, float arrowY) {
        super(context);
        this.context = context;
        initData();
        this.pointX = pointX;
        this.pointY = pointY;
        thi