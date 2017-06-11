package by.superteam.kids;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener {
    static int currentColor = Color.WHITE;
    int a;
    static int errCounter;
    static DrawActivity drawActivity;
    MenuItem pastTouched;
Bitmap rightBitmap;
    static Object[][] colors = {
            {"white", Color.WHITE, R.drawable.white_color_touched},
            {"red", Color.RED, R.drawable.red_color_touched},
            {"green", Color.GREEN, R.drawable.green_color_touched},
            {"blue", Color.BLUE, R.drawable.blue_color_touched},
            {"yellow", Color.YELLOW, R.drawable.yellow_color_touched},
            {"cyan", Color.CYAN, R.drawable.cyan_color_touched},
            {"pink", Color.MAGENTA, R.drawable.pink_color_touched},
            {"brown", Color.rgb(101, 67, 33), R.drawable.brown_color_touched},
            {"grey", Color.DKGRAY, R.drawable.grey_color_touched}
    };
    Drawable pastBackgroundOfItem;
    public static Object[][][][] levels = {
            {
                    {
                            {0}, {0}, {8}, {8}, {0}, {0}
                    },
                    {
                            {8}, {8}, {8}, {8}, {8}, {8}
                    },
                    {
                            {8}, {8}, {8}, {8}, {8}, {8}
                    },
                    {
                            {8}, {8}, {8}, {8}, {8}, {8}
                    },
                    {
                            {0}, {0}, {8}, {8}, {0}, {0}
                    }
            },
            {
                    {
                            {0}, {0}, {8}, {8}, {0}, {0}
                    },
                    {
                            {true,0,8}, {8}, {8}, {8}, {8}, {false,8,0}
                    },
                    {
                            {8}, {8}, {8}, {8}, {8}, {8}
                    },
                    {
                            {false,0,8}, {8}, {8}, {8}, {8}, {true,8,0}
                    },
                    {
                            {0}, {8}, {8}, {8}, {8}, {0}
                    }
            },
            {
                    {
                            {8}, {0}, {8}, {0}, {8}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {8}, {0}, {0}, {0}, {8}
                    }
            },
            {
                    {
                            {8}, {0}, {8}, {0}, {8}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {0}, {8}, {8}, {8}, {0}
                    },
                    {
                            {8}, {8}, {8}, {8}, {8}
                    }
            },
            {
                    {
                            {4}, {1}, {4}
                    },
                    {
                            {1}, {4}, {1}
                    },
                    {
                            {4}, {1}, {4}
                    },

            },
            {
                    {
                            {7}, {8}, {1}
                    },
                    {
                            {2}, {3}, {4}
                    },
                    {
                            {5}, {6}, {7}
                    },

            },
            {
                    {
                            {8}, {8}, {1},{1}, {5}, {5}
                    },
                    {
                            {5}, {5}, {8},{8}, {1}, {1}
                    },
                    {
                            {1}, {1}, {5},{5}, {8}, {8}
                    },

            }
    };
    ArrayList<ArrayList<Object>> errorsForTriangle = new ArrayList<>();
    ArrayList<ArrayList<Object>> errors = new ArrayList<>();
    Thread errorThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (DrawActivity.this.isFinishing() == false) {
                for (int i = 0; i < errors.size(); i++) {
                    final RectView view = (RectView) ((LinearLayout) DrawActivity.this.humanSide.getChildAt((int) errors.get(i).get(0))).getChildAt((int) errors.get(i).get(1));
                    float newAlpha = (1f - (float) errors.get(i).get(2) - 0.02f);
try{                    view.setAlpha(newAlpha);}catch (Exception e){}

                    errors.get(i).set(2, 1f - newAlpha);
                    if (newAlpha < 0.02f) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.changeColor((int) colors[0][1]);
                                view.setAlpha(1);
                            }
                        });

                        errors.remove(i);
                        i--;
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    static int level = 0;
    int x;
    int y;
    int width;
    int height;
    static LinearLayout humanSide;
    Thread errorTriangleThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (DrawActivity.this.isFinishing() == false) {
                for (int i = 0; i < errorsForTriangle.size(); i++) {
                    final TriangleView view = (TriangleView) errorsForTriangle.get(i).get(0);
                    final int newAlpha = (int) errorsForTriangle.get(i).get(1) - 1;


                    errorsForTriangle.get(i).set(1, newAlpha);

                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (errorsForTriangle.size() != 0) {
                                view.setAlpha(newAlpha, (boolean) errorsForTriangle.get(finalI).get(2));
                            }

                        }
                    });
                    if (newAlpha == 1) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                view.changeColor((boolean) errorsForTriangle.get(finalI).get(2), (int) colors[0][1]);
                                view.setAlpha(255, (boolean) errorsForTriangle.get(finalI).get(2));
                                errorsForTriangle.remove(finalI);

                            }
                        });

                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
    LinearLayout computerSide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        errCounter = 0;
        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);
        x = levels[level][0].length;
        y = levels[level].length;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        humanSide = (LinearLayout) findViewById(R.id.human_side_second);
       computerSide = (LinearLayout) findViewById(R.id.computer_side_second);
        drawMatrix(x, y, humanSide, width / 2, height, true);
        drawMatrix(x, y, computerSide, width / 2, height, false);
        errorThread.start();
        errorTriangleThread.start();
    }

    public void drawMatrix(int x, int y, LinearLayout mainLayout, int width, int height, boolean ishuman) {

        for (int i = 0; i < y; i++) {
            LinearLayout tr = new LinearLayout(this);
            mainLayout.addView(tr);
            tr.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < x; j++) {
                a = 100;
                if ((height / y) > (width / x)) {
                    a = (int) (width * 0.9 / x);

                } else {
                    a = (int) (height * 0.7 / y);

                }
                if (levels[level][i][j].length > 1) {
                    TriangleView view = new TriangleView(this, (boolean) levels[level][i][j][0], (Integer) colors[(int) levels[level][i][j][1]][1], (Integer) colors[(int) levels[level][i][j][2]][1], a, a, ishuman);

                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor(true, (Integer) colors[0][1]);
                        view.changeColor(false, (Integer) colors[0][1]);
                      //  if ((int) levels[level][i][j][1] != 0) errCounter++;
                       // if ((int) levels[level][i][j][2] != 0) errCounter++;


                    }
                } else {
                    RectView view = new RectView(this, (Integer) colors[(int) levels[level][i][j][0]][1], a, a);
                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor((Integer) colors[0][1]);
                        view.setOnTouchListener(this);
                        if((int) levels[level][i][j][0]!=0)errCounter++;

                    }
                }

            }
        }
        computerSide.setDrawingCacheEnabled(true);

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.draw_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.removeAll:
                humanSide.removeAllViews();
                errCounter=0;
                drawMatrix(x, y, humanSide, width / 2, height, true);
                return true;
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);
                builder.setTitle("Справка")
                        .setMessage("Раскрасьте левую часть экрана так, как раскрашена правая")
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;


            default:
                if (pastTouched != null && !pastTouched.equals(item)) {
                    pastTouched.setIcon(pastBackgroundOfItem);
                    pastBackgroundOfItem = item.getIcon();


                }
                for (int i = 0; i < colors.length; i++) {
                    if (item.getTitle().equals(colors[i][0])) {
                        if (pastBackgroundOfItem == null) {
                            pastBackgroundOfItem = item.getIcon();
                        }
                        currentColor = (int) colors[i][1];
                        item.setIcon(getResources().getDrawable((int) colors[i][2]));

                    }
                }
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.

                pastTouched = item;
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            int i = humanSide.indexOfChild((LinearLayout) view.getParent());
            int j = ((LinearLayout) view.getParent()).indexOfChild(view);

            for (int k = 0; k < errors.size(); k++) {
                if ((int) errors.get(k).get(0) == i && (int) errors.get(k).get(1) == j) {
                    errors.remove(k);
                    view.setAlpha(1f);
                    break;
                }
            }
            if((int)colors[(int) levels[level][i][j][0]][1]==currentColor&&((RectView)view).color!=currentColor)errCounter--;
            if((int)colors[(int) levels[level][i][j][0]][1]!=currentColor&&((RectView)view).color==(int)colors[(int) levels[level][i][j][0]][1]&&((RectView)view).color!=Color.WHITE)errCounter++;
            if(errCounter==0){

win();




            }
            ((RectView) view).changeColor(currentColor);

            if (currentColor != (int) colors[(int) levels[level][i][j][0]][1]) {

                ArrayList<Object> error = new ArrayList<>();
                error.add(i);
                error.add(j);
                error.add(0.1f);
                errors.add(error);
            }




        }
        Log.d("rect view",DrawActivity.errCounter+"");



        return false;
    }
    public void win(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);
        builder.setTitle("Вы победили")
                .setMessage("Уровень " + (level+1) + " пройден!")
                .setIcon(R.drawable.human_button_touched)
                .setCancelable(false)
                .setNeutralButton("Заново", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("В меню",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });
        if(level+1<levels.length){
            builder.setPositiveButton("Следующий", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    intent.putExtra("level",level+1);
                    startActivity(intent);
                    finish();
                }
            });
        }else{
            builder.setMessage("Вы прошли раздел \"Раскрась по образцу\"");
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

class TriangleView extends View implements View.OnTouchListener {
    public int needColor1;
    public int needColor2;
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
        needColor1=color1;
        needColor2=color2;
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

            if((color!=needColor1&&needColor1!=Color.WHITE)&&this.color1==needColor1){
                DrawActivity.errCounter++;
            }
            if((color==needColor1)&&this.color1!=needColor1&&leftAlpha==255){
                DrawActivity.errCounter--;
            }
            this.color1 = color;
        } else {
            if((color!=needColor2&&needColor2!=Color.WHITE)&&this.color2==needColor2){
                DrawActivity.errCounter++;
            }
            if((color==needColor2)&&this.color2!=needColor2&&rightAlpha==255){
                DrawActivity.errCounter--;
            }
            this.color2 = color;
        }
        Log.d("Triangle view",errCounter+"");
        this.invalidate();
        if(DrawActivity.errCounter<=0){
            DrawActivity.this.win();
        }
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


}}
