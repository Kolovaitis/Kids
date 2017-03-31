package by.superteam.kids;

/**
 * Created by Никита on 31.03.17.
 */

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

public class RectView extends View {
    public int color;
    public int height;
    public int width;
    Paint p;
    Path path;


    public RectView(Context context,int color, int width, int height) {
        super(context);
        this.color = color;
        this.height = height;
        this.width = width;
        p = new Paint();
        p.setStrokeWidth(3);


        // path, квадрат


            path = new Path();
            path.moveTo(0, 0);
            path.lineTo(0, height);
            path.lineTo(width, height);
            path.lineTo(width, 0);
            path.lineTo(0, 0);
            path.close();

/*
        // регион из прямоугольника обрезки
        rect = new Rect(100, 100, 150, 150);
        clipRegion = new Region(rect);

        // итоговый регион
        region = new Region();
        // отсекаем от path область clipRegion
        region.setPath(path, clipRegion);
        // получаем path из региона
        pathDst = region.getBoundaryPath();*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(color);


        p.setStyle(Paint.Style.FILL);
        p.setColor(color);
        canvas.drawPath(path, p);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, p);

    }

    public void changeColor( int color) {
       this.color=color;
        this.invalidate();
    }
}

