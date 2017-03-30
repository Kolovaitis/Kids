package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class PositionActivity extends AppCompatActivity implements View.OnClickListener {
    static int[][] levels = {
            {1, 2},
            {2, 1},
            {2, 2},
            {2, 3}
    };
    int x;
    int y;
    ImageView[][] imgViews;
    int[][] touched;
    LinearLayout humanSide;
    LinearLayout computerSide;
    boolean waserror = false;
    int counter;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);
        x = levels[level][0];
        y = levels[level][1];
        counter = x * y;
        touched = new int[y][x];
        imgViews = new ImageView[y][x];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                touched[i][j] = 0;
            }
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        humanSide = (LinearLayout) findViewById(R.id.human_side_first);
        computerSide = (LinearLayout) findViewById(R.id.computer_side_first);
        drawMatrix(x, y, humanSide, width / 2, height, R.drawable.human_button, true);
        drawMatrix(x, y, computerSide, width / 2, height, R.drawable.human_button_touched, false);
        choose();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.position_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(PositionActivity.this);
                builder.setTitle("Справка")
                        .setMessage("Нажимай на левой части экрана кнопки, соответствующие кнопкам, показанным справа")
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

    public void drawMatrix(int x, int y, LinearLayout mainLayout, int width, int height, int res, boolean ishuman) {
        for (int i = 0; i < y; i++) {
            LinearLayout tr = new LinearLayout(this);
            mainLayout.addView(tr);
            tr.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < x; j++) {
                TextView textView = new TextView(this);
                textView.setBackgroundResource(res);

                int a = 100;
                if ((height / y) > (width / x)) {
                    a = (int) (width*0.9 / x);

                } else {
                    a = (int) (height * 0.7 / y);

                }
                textView.setHeight(a);
                textView.setWidth(a);
                textView.setPadding(a / 20, a / 20, a / 20, a / 20);
                tr.addView(textView);
                if (ishuman) {
                    textView.setOnClickListener(this);

                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int i = humanSide.indexOfChild((LinearLayout) view.getParent());
        int j = ((LinearLayout) view.getParent()).indexOfChild(view);
        ((LinearLayout)computerSide.getChildAt(i)).getChildAt(j).setBackgroundResource(R.drawable.human_button_touched);
        if (touched[i][j] == 1) {
            view.setBackgroundResource(R.drawable.human_button_touched);
            touched[i][j] = 2;
            counter--;
            if (counter != 0) {
                choose();
            } else {
                level++;
                AlertDialog.Builder builder = new AlertDialog.Builder(PositionActivity.this);
                builder.setTitle("Ты победил")
                        .setMessage("Уровень " + level + " пройден!")
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
                if(level<levels.length){
                    builder.setPositiveButton("Следующий", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = getIntent();
                            intent.putExtra("level",level);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else{
                    builder.setMessage("Ты прошел раздел \"Позиция\"");
                }
                AlertDialog alert = builder.create();
                alert.show();

            }
        } else {
            view.setBackgroundResource(R.mipmap.error);
            if (waserror) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PositionActivity.this);
                builder.setTitle("Ты проиграл")
                        .setMessage("Попробуй еще!")
                        .setIcon(R.mipmap.error)
                        .setCancelable(false)
                        .setNegativeButton("ОК",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = getIntent();
                                        startActivity(intent);
                                        dialog.cancel();
                                        finish();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            waserror = true;
        }
    }

    public void choose() {
        final Random random = new Random();
        int i = random.nextInt(y);
        int j = random.nextInt(x);
        if (touched[i][j] == 0) {
            ((TextView) ((LinearLayout) computerSide.getChildAt(i)).getChildAt(j)).setBackgroundResource(R.mipmap.computer_choose);
            touched[i][j] = 1;
        } else {
            choose();
        }
    }
}
