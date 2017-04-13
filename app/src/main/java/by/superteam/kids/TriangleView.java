package by.superteam.kids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Никита on 31.03.17.
 */

public class TriangleView extends View implements View.OnTouchListener {

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
    Canvas canvas;
Path pathRight;
    public TriangleView(Context context, boolean isLeftTop, int color1, int color2, int width, int height, boolean human) {
        super(context);
        this.isLeftTop = isLeftTop;
        this.color1 = color1;
        this.color2 = color2;
        this.height = height;
        this.width = width;
        leftAlpha = 255;
        rightAlpha = 255;
        p = new Paint();
        p.setStrokeWidth(3);


        // path, треугольник
        if (isLeftTop) {
            path = new Path();
            path.moveTo(width, 0);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            path.close();
            pathRight = new Path();
            pathRight.moveTo(width, 0);
            pathRight.lineTo(0, height);
            pathRight.lineTo(width, height);
            pathRight.close();
            pathLeft = new Path();
            pathLeft.moveTo(width, 0);
            pathLeft.lineTo(0, 0);
            pathLeft.lineTo(0, height);
            pathLeft.lineTo(width, height);
            pathLeft.lineTo(width, 0);
            pathLeft.lineTo(0, height);
            pathLeft.close();
        } else {
            path = new Path();
            path.moveTo(0, height);
            path.lineTo(width, height);
            path.lineTo(0, 0);
            path.close();
            pathLeft = new Path();
            pathLeft.moveTo(0, 0);
            pathLeft.lineTo(width, 0);
            pathLeft.lineTo(width, height);
            pathLeft.lineTo(0, height);
            pathLeft.lineTo(0, 0);
            pathLeft.lineTo(width, height);
            pathLeft.close();
            pathRight = new Path();
            pathRight.moveTo(0, 0);
            pathRight.lineTo(width, height);
            pathRight.lineTo(width, 0);
            pathRight.close();
        }
        if (human) this.setOnTouchListener(this);

    }

    @Override
    protected void onDraw(Canvas canva) {
        this.canvas = canva;
        super.onDraw(canvas);
        //    canvas.drawColor(color2);


        p.setStyle(Paint.Style.FILL);
        p.setColor(color2);
        p.setAlpha(rightAlpha);
        canvas.drawPath(pathRight, p);
        p.setStyle(Paint.Style.FILL);
        p.setColor(color1);
        p.setAlpha(leftAlpha);
        canvas.drawPath(path, p);

        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawPath(pathLeft, p);

    }

    public void changeColor(boolean isPart1, int color) {
        if (isPart1) {
            this.color1 = color;
        } else {
            this.color2 = color;
        }
        this.invalidate();
    }

    public void setAlpha(int alpha, boolean isLeft) {
        if (isLeft) {
            leftAlpha = alpha;
        } else {
            rightAlpha = alpha;
        }
        invalidate();
    }

    public void setAlphaMinus(int minus, boolean isLeft) {
        if (isLeft) {
            leftAlpha -= minus;
        } else {
            rightAlpha -= minus;
        }
        this.invalidate();
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (this.isLeftTop) {
                this.changeColor(width - x > y, DrawActivity.currentColor);
                int i = DrawActivity.humanSide.indexOfChild((LinearLayout) view.getParent());
                int j = ((LinearLayout) view.getParent()).indexOfChild(view);
                if ((int) DrawActivity.colors[(Integer) (DrawActivity.levels[DrawActivity.level][i][j][(width - x > y) ? 1 : 2])][1] != DrawActivity.currentColor) {
                    final TriangleView triangleView = this;
                    final boolean isLeft = width - x > y;
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while ((isLeft && triangleView.leftAlpha > 3) || (isLeft == false && triangleView.rightAlpha > 3)) {
                                ((DrawActivity) triangleView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        triangleView.setAlphaMinus(1, isLeft);
                                    }
                                });
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            ((DrawActivity) triangleView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    triangleView.changeColor(isLeft, Color.WHITE);
                                    triangleView.setAlpha(255, isLeft);
                                }
                            });
                        }
                    });
                    thread.start();
                }
            } else {
                this.changeColor(x < y, DrawActivity.currentColor);

                int i = DrawActivity.humanSide.indexOfChild((LinearLayout) view.getParent());
                int j = ((LinearLayout) view.getParent()).indexOfChild(view);
                if ((int) DrawActivity.colors[(Integer) (DrawActivity.levels[DrawActivity.level][i][j][(x < y) ? 1 : 2])][1] != DrawActivity.currentColor) {
                    final TriangleView triangleView = this;
                    final boolean isLeft = x < y;
                    final Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while ((isLeft && triangleView.leftAlpha > 1) || (!isLeft && triangleView.rightAlpha > 1)) {
                                ((DrawActivity) triangleView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        triangleView.setAlphaMinus(1, isLeft);
                                    }
                                });
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            ((DrawActivity) triangleView.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    triangleView.changeColor(isLeft, Color.WHITE);
                                    triangleView.setAlpha(255, isLeft);
                                }
                            });
                        }
                    });
                    thread.start();
                }
            }
        }
        return false;
    }


}