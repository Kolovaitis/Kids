package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindDifference extends AppCompatActivity {

    private static String LOG_TAG = "findDiffLog";

    public View.OnTouchListener onTouchListener;

    private int level;
    private int levelsNumber;
    private ArrayList<Integer> resImages;
    private int attempts = 3;
    private boolean isGameAvailable = true;

    private LinearLayout images;

    private LinearLayout attemptList;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_difference);

        images = (LinearLayout)findViewById(R.id.images);
        attemptList = (LinearLayout)findViewById(R.id.attempts);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);

        inflater = this.getLayoutInflater();
        resImages = new ArrayList<>();

        Log.d(LOG_TAG, Integer.toString(level));


        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                Log.d(LOG_TAG, view.getClass().toString());
                if (isGameAvailable) {
                    boolean wasDiffFound = false;
                    //float[] coords = ((CanvasView)view).getRealCoords(motionEvent.getX(), motionEvent.getY());

                    Log.d(LOG_TAG, "Canvas coords: " + motionEvent.getX() + " " + motionEvent.getY());

                    //for (int i = 0; i < mDifferences.size(); i++) {
                    for (int i = 0; i < ((CanvasView)view).getFigures().size(); i++) {
                        //Log.d(LOG_TAG, "Differences " + Integer.toString(i) + " element: " + mDifferences.get(i).toString());
                        //
                        if (((CanvasView)view).getFigures().get(i).containsPoint(motionEvent.getX(), motionEvent.getY()) && !wasDiffFound) {

                            for (int j = 0; j < images.getChildCount(); j++) {

                                //Log.d(LOG_TAG, images.getChildAt(j).getClass().toString());
                                //if (images.getChildAt(j).getClass().equals(CanvasView.class)) {
                                    //Log.d(LOG_TAG, "It is CanvasView");
                                    //((CanvasView) images.getChildAt(j)).addFigure(mDifferences.get(i));
                                    ((CanvasView) images.getChildAt(j)).getFigures().get(i).setVisible(true);

                                    //Log.d(LOG_TAG, Boolean.toString(((CanvasView) images.getChildAt(j)).getFigures().get(i).isVisible()));
                                //}
                            }

                            //mDifferences.remove(i);
                            wasDiffFound = true;
                        }
                    }

                    for (int j = 0; j < images.getChildCount(); j++) {
                        //if (images.getChildAt(j).getClass().equals(CanvasView.class)) {
                            images.getChildAt(j).invalidate();
                        //}
                    }

                    showAttempt(wasDiffFound);
                    checkWin();
                }

                return false;
            }
        };

        loadLevel();
    }

    private void checkWin(){
        if (isGameAvailable) {
            if (attempts <= 0) {
                lose();
                isGameAvailable = false;
            } else {
                boolean isWin = true;
                for (int i = 0; i < images.getChildCount(); i++) {
                    if (isWin) {
                        for (int j = 0; j < ((CanvasView) images.getChildAt(i)).getFigures().size(); j++) {
                            if (!((CanvasView) images.getChildAt(i)).getFigures().get(j).isVisible()) {
                                isWin = false;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }

                if (isWin) {
                    win();
                    isGameAvailable = false;
                }
            }
        }
    }

    public void win(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы победили")
                .setMessage("Уровень " + Integer.toString(level + 1) + " пройден!")
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
        if(level<levelsNumber - 1){
            builder.setPositiveButton("Следующий", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    intent.putExtra("level",level + 1);
                    startActivity(intent);
                    finish();
                    //Was passed = true;
                }
            });
        }else{
            builder.setMessage("Вы прошли раздел \"Найди отличия\"");
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void lose(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы проиграли")
                .setMessage("Попробуйте еще раз!")
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
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showAttempt(boolean isFound){
        ImageView imageAttempt = (ImageView) inflater.inflate(R.layout.attempt_view, attemptList, false);
        if (isFound){
            //imageAttempt.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tick));
            imageAttempt.setImageResource(R.drawable.tick);
        }else {
            attempts--;
            //imageAttempt.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cross));
            imageAttempt.setImageResource(R.drawable.cross);
        }

        attemptList.addView(imageAttempt);
    }

    private void loadLevel(){

        Log.d(LOG_TAG, "Loading level");

        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.query("findDiffs", null, null, null, null, null, null);


        levelsNumber = c.getCount();
        Log.d(LOG_TAG, Integer.toString(levelsNumber));

        String figuresData;
        String imagesData;

        if(c.moveToPosition(level)) {

            Log.d(LOG_TAG, "Position " + Integer.toString(level) + " exists in database");

            figuresData = c.getString(c.getColumnIndex("figures"));
            imagesData = c.getString(c.getColumnIndex("imagesResources"));

            Log.d(LOG_TAG, "Figures data: " + figuresData);
            Log.d(LOG_TAG, "Images data: " + imagesData);
        }else {
            imagesData = "";
            figuresData = "";
        }
        c.close();

        parseImagesData(imagesData);

        final ArrayList<Figure> figures = parseFiguresData(figuresData);

        for (int i = 0; i<resImages.size(); i++){
            CanvasView canvasView = new CanvasView(this, resImages.get(i)){
                @Override
                protected void onLayout(boolean changed, int l, int t, int r, int b) {
                    super.onLayout(changed, l, t, r, b);
                    Log.d(LOG_TAG, "onLayout " + Integer.toString(this.getFigures().size()));
                    if(getFigures().size() == 0) {
                        setFiguresForCanvas(this, figures);

                        Log.d(LOG_TAG, this.getClass().toString());
                    }
                }
            };
            canvasView.setOnTouchListener(onTouchListener);
            images.addView(canvasView);
        }
    }

    public void parseImagesData(String data){
        try {
            JSONArray imagesData = new JSONArray(data);
            for (int i = 0; i < imagesData.length(); i++) {
                resImages.add(imagesData.getInt(i));
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Figure> parseFiguresData(String data){
        ArrayList<Figure> figures = new ArrayList<>();

        try {
            JSONArray figuresData = new JSONArray(data);
            for (int i = 0; i < figuresData.length(); i++) {
                JSONObject obj = new JSONObject(figuresData.getString(i));

                Figure figure;

                switch (obj.getString("figureType")) {
                    case "rectangle":
                        figure = new Rect(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("width")), Float.parseFloat(obj.getString("height")), Integer.parseInt(obj.getString("rotate")));
                        break;

                    case "trigon":
                        figure = new Trigon(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("base")), Float.parseFloat(obj.getString("height")), Integer.parseInt(obj.getString("rotate")));
                        break;

                    case "circle":
                        figure = new Circle(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("radius")));
                        break;
                    default:
                        figure = new Circle(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("radius")));
                        break;
                }

                figure.setVisible(false);
                figures.add(figure);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return figures;
    }

    public void setFiguresForCanvas(CanvasView canvas, ArrayList<Figure> figures){
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        float[] offset = canvas.getOffset();

        for (int i = 0; i<figures.size(); i++){
            figures.get(i).setX((float)((width - 2 * offset[0]) * figures.get(i).getRelativeX() + offset[0]));
            figures.get(i).setY((float)((height - 2 * offset[1]) * figures.get(i).getRelativeY()) + offset[1]);

            Log.d(LOG_TAG, "Calculated coords are " + Float.toString(figures.get(i).getX()) + " " + Float.toString(figures.get(i).getY()));
        }

        canvas.setFigures(figures);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Справка")
                        .setMessage("Нажимайте на отличающиеся части изображений. Найдите все отличия.")
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
}
