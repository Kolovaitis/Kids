package by.superteam.kids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.view.View;

/**
 * Created by Никита on 31.03.17.
 */

public class TriangleView extends View {
    public int color1;
    public int color2;
    public int leftAlpha;
    public int rightAlpha;
    public boolean isLeftTop;
    public int height;
    public int width;
    private ShapeDrawable mDrawable;
    Paint p;
    Region region;
    Region clipRegion;
    Path path;
    Path pathLeft;
    Rect rect;
    private Canvas canvas;
    public TriangleView(Context context, boolean isLeftTop, int color1, int color2, int width, int height) {
        super(context);
        this.isLeftTop = isLeftTop;
        this.color1 = color1;
        this.color2 = color2;
        this.height = height;
        this.width = width;
        leftAlpha=255;
        rightAlpha=255;
        p = new Paint();
        p.setStrokeWidth(3);


        // path, треугольник
      if(isLeftTop) {
          path = new Path();
          path.moveTo(0, 0);
          path.lineTo(width, 0);
          path.lineTo(0, height);
          path.lineTo(0,0);
          path.close();
          pathLeft = new Path();
          pathLeft.moveTo(width, 0);
          pathLeft.lineTo(0, 0);
          pathLeft.lineTo(0, height);
          pathLeft.lineTo(width, height);
          pathLeft.lineTo(width, 0);
          pathLeft.lineTo(0, height);
          pathLeft.close();
      }else{
          path = new Path();
          path.moveTo(0, 0);
          path.lineTo(0, height);
          path.lineTo(width, height);
          path.lineTo(0,0);
          path.close();
          pathLeft = new Path();
          pathLeft.moveTo(0, 0);
          pathLeft.lineTo(width, 0);
          pathLeft.lineTo(width, height);
          pathLeft.lineTo(0, height);
          pathLeft.lineTo(0, 0);
          pathLeft.lineTo(width, height);
          pathLeft.close();
      }

    }

    @Override
    protected void onDraw(Canvas canva) {
        this.canvas=canva;
        super.onDraw(canvas);
    //    canvas.drawColor(color2);



            p.setStyle(Paint.Style.FILL);
        p.setColor(color2);
        p.setAlpha(rightAlpha);
        canvas.drawPath(pathLeft, p);
            p.setColor(color1);
        p.setAlpha(leftAlpha);
            canvas.drawPath(path, p);

            p.setColor(Color.BLACK);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawPath(pathLeft, p);

    }
    public void changeColor(boolean isPart1,int color){
        if(isPart1){
            this.color1=color;
        }else{
            this.color2=color;
        }
        this.invalidate();
    }
    public void setAlpha(int alpha,boolean isLeft){
        if(isLeft){
            leftAlpha=alpha;
        }else{
            rightAlpha=alpha;
        }
        invalidate();
    }

    }

