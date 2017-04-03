package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener {
    int currentColor = Color.WHITE;
    int a;
    MenuItem pastTouched;

    Object[][] colors = {
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
                            {true, 2, 0}, {false, 0, 2}, {8}, {8}, {0}, {0}
                    }
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
                    view.setAlpha(newAlpha);

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
    int level = 0;
    int x;
    int y;
    int width;
    int height;
    LinearLayout humanSide;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
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
        LinearLayout computerSide = (LinearLayout) findViewById(R.id.computer_side_second);
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
                    TriangleView view = new TriangleView(this, (boolean) levels[level][i][j][0], (Integer) colors[(int) levels[level][i][j][1]][1], (Integer) colors[(int) levels[level][i][j][2]][1], a, a);

                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor(true, (Integer) colors[0][1]);
                        view.changeColor(false, (Integer) colors[0][1]);
                        view.setOnTouchListener(this);

                    }
                } else {
                    RectView view = new RectView(this, (Integer) colors[(int) levels[level][i][j][0]][1], a, a);
                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor((Integer) colors[0][1]);
                        view.setOnTouchListener(this);

                    }
                }

            }
        }
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
                drawMatrix(x, y, humanSide, width / 2, height, true);
                return true;
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(DrawActivity.this);
                builder.setTitle("Справка")
                        .setMessage("Раскрась левую часть экрана так, как раскрашена правая")
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
            if (view.getClass() == TriangleView.class) {
                for (int k = 0; k < errorsForTriangle.size(); k++) {
                    if (errorsForTriangle.get(k).get(0).equals(view)) {
                        errorsForTriangle.remove(k);

                        break;
                    }
                }
                if (!((TriangleView) view).isLeftTop) {
                    ((TriangleView) view).setAlpha(255, motionEvent.getX() < motionEvent.getY());
                    ((TriangleView) view).changeColor(motionEvent.getX() < motionEvent.getY(), currentColor);
                    if (currentColor != (int) colors[(int) levels[level][i][j][(motionEvent.getX() < motionEvent.getY()) ? 1 : 2]][1]) {
                        //((TriangleView) view).changeColor(motionEvent.getX() < motionEvent.getY(), (int) colors[0][1]);
                        ArrayList<Object> error = new ArrayList<>();
                        error.add(view);
                        error.add(255);
                        error.add(motionEvent.getX() < motionEvent.getY());
                        errorsForTriangle.add(error);
                    }
                } else {
                    ((TriangleView) view).setAlpha(255, a - motionEvent.getX() > motionEvent.getY());
                    ((TriangleView) view).changeColor(a - motionEvent.getX() > motionEvent.getY(), currentColor);
                    if (currentColor != (int) colors[(int) levels[level][i][j][(motionEvent.getX() > motionEvent.getY()) ? 1 : 2]][1]) {
                        ArrayList<Object> error = new ArrayList<>();
                        error.add(view);
                        error.add(255);
                        error.add(motionEvent.getX() > motionEvent.getY());
                        errorsForTriangle.add(error);
                    }
                }
            } else {
                for (int k = 0; k < errors.size(); k++) {
                    if ((int) errors.get(k).get(0) == i && (int) errors.get(k).get(1) == j) {
                        errors.remove(k);
                        view.setAlpha(1f);
                        break;
                    }
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
        }
        return false;
    }
}
