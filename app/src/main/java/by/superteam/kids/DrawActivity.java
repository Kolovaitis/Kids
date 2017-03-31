package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
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

public class DrawActivity extends AppCompatActivity implements View.OnTouchListener {
    int currentColor=Color.DKGRAY;
    int a;
    public static Object[][][][] levels = {
            {
                    {
                            {Color.WHITE}, {Color.WHITE}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.WHITE}, {Color.WHITE}
                    },
                    {
                            {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}
                    },
                    {
                            {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}
                    },
                    {
                            {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.DKGRAY}
                    },
                    {
                            {Color.WHITE}, {Color.WHITE}, {Color.DKGRAY}, {Color.DKGRAY}, {Color.WHITE}, {Color.WHITE}
                    }
            }
    };
    int level = 0;
    int x;
    int y;

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
        int width = size.x;
        int height = size.y;
        LinearLayout humanSide = (LinearLayout) findViewById(R.id.human_side_second);
        LinearLayout computerSide = (LinearLayout) findViewById(R.id.computer_side_second);
        drawMatrix(x, y, humanSide, width / 2, height, true);
        drawMatrix(x, y, computerSide, width / 2, height, false);

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
                    TriangleView view = new TriangleView(this, (boolean)levels[level][i][j][0],(int)levels[level][i][j][1],(int)levels[level][i][j][2], a, a);

                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor(true,Color.WHITE);
                        view.changeColor(false,Color.WHITE);
                        view.setOnTouchListener(this);

                    }
                }else{
                    RectView view = new RectView(this,(int)levels[level][i][j][0],a,a);
                    view.setPadding(a / 20, a / 20, a / 20, a / 20);
                    tr.addView(view, new ActionBar.LayoutParams(a, a));
                    if (ishuman) {
                        view.changeColor(Color.WHITE);
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
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
            if(view.getClass()==TriangleView.class){
                if(((TriangleView)view).isLeftTop){
                   ((TriangleView) view).changeColor(motionEvent.getX()<motionEvent.getY(),currentColor);
                }else{
                    ((TriangleView) view).changeColor(a-motionEvent.getX()>motionEvent.getY(),currentColor);
                }
            }else{
                ((RectView)view).changeColor(currentColor);
            }
        }
        return false;
    }
}
