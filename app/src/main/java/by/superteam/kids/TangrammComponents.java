package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TangrammComponents extends AppCompatActivity {
    private static String LOG_TAG = "tangrammComponentsLog";

    private boolean isLoaded = false;
    private int level;
    private int levelsNumber;
    private boolean useMovingOffset = true;

    private CanvasView tangrammPlace;

    private View.OnTouchListener listener;
    private float[] movingOffset = new float[2];
    private boolean multitouch = false;
    private float[][] touchPoints = new float[4][2];
    private int initialAngle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tangramm_components);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);

        listener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Log.d(LOG_TAG, "Canvas coords: " + motionEvent.getX() + " " + motionEvent.getY());

                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:

                        Log.d(LOG_TAG, "Down");
                        for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
                            if (tangrammPlace.getFigures().get(i).containsPoint(motionEvent.getX(), motionEvent.getY()) &&  tangrammPlace.getFigures().get(i).isDraggable()){
                                Log.d(LOG_TAG, "Contains point");
                                tangrammPlace.getFigures().get(i).setMoving(true);
                                if (useMovingOffset) {
                                    movingOffset[0] = motionEvent.getX() - tangrammPlace.getFigures().get(i).getX();
                                    movingOffset[1] = motionEvent.getY() - tangrammPlace.getFigures().get(i).getY();
                                }
                                break;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if(!multitouch) {
                            Log.d(LOG_TAG, "Multi Down");
                            MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
                            for (int i = 0; i<motionEvent.getPointerCount(); i++){
                                motionEvent.getPointerCoords(i, coords);
                                touchPoints[i] = new float[]{coords.x, coords.y};
                                touchPoints[i + 2] = new float[]{coords.x, coords.y};
                            }

                            for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
                                if (tangrammPlace.getFigures().get(i).isMoving()){
                                    initialAngle = tangrammPlace.getFigures().get(i).getRotateAngle();
                                }
                            }
                            multitouch = true;
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (motionEvent.getActionIndex() == 0){
                            for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
                                if (tangrammPlace.getFigures().get(i).isMoving()){
                                    if (useMovingOffset) {
                                        tangrammPlace.getFigures().get(i).setX(motionEvent.getX() - movingOffset[0]);
                                        tangrammPlace.getFigures().get(i).setY(motionEvent.getY() - movingOffset[1]);
                                    }else {
                                        tangrammPlace.getFigures().get(i).setX(motionEvent.getX());
                                        tangrammPlace.getFigures().get(i).setY(motionEvent.getY());
                                    }
                                }
                            }
                        }
                        if (multitouch) {
                            boolean hasNew = false;

                            MotionEvent.PointerCoords coords = new MotionEvent.PointerCoords();
                            for (int i = 0; i<2; i++){
                                motionEvent.getPointerCoords(i, coords);
                                if(touchPoints[i+2][0] != coords.x || touchPoints[i+2][1] != coords.y){
                                    Log.d(LOG_TAG, "new coordinates");
                                    touchPoints[i + 2] = new float[]{coords.x, coords.y};
                                    hasNew = true;
                                }
                            }

                            if (hasNew) {
                                double firstAngle = Math.atan((touchPoints[1][1] - touchPoints[0][1]) / (touchPoints[1][0] - touchPoints[0][0]));
                                double secondAngle = Math.atan((touchPoints[3][1] - touchPoints[2][1]) / (touchPoints[3][0] - touchPoints[2][0]));

                                if((touchPoints[1][0] - touchPoints[0][0]) * (touchPoints[3][0] - touchPoints[2][0]) < 0){
                                    if (secondAngle > 0){
                                        secondAngle = -Math.PI + secondAngle;
                                    }else {
                                        secondAngle = Math.PI + secondAngle;
                                    }
                                }

                                double preTotalAngle = secondAngle - firstAngle;

                                int totalAngle = (int) (preTotalAngle / Math.PI * 6);

                                Log.d(LOG_TAG, Integer.toString(totalAngle) + " " + Double.toString(firstAngle) + " " + Double.toString(secondAngle) + " " + Double.toString(preTotalAngle));
                                for (int i = 0; i < tangrammPlace.getFigures().size(); i++) {
                                    if (tangrammPlace.getFigures().get(i).isMoving()) {
                                        while (tangrammPlace.getFigures().get(i).getRotateAngle() != totalAngle + initialAngle) {
                                            if (tangrammPlace.getFigures().get(i).getRotateAngle() > totalAngle + initialAngle) {
                                                tangrammPlace.getFigures().get(i).decreaseRotate();
                                            } else {
                                                tangrammPlace.getFigures().get(i).increaseRotate();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:

                        Log.d(LOG_TAG, "Pointer Up");
                        Log.d(LOG_TAG, Integer.toString(motionEvent.getPointerCount()));
                        if (motionEvent.getPointerCount() == 2){
                            Log.d(LOG_TAG, "==1");
                            multitouch = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(LOG_TAG, "Up");
                        for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
                            if (tangrammPlace.getFigures().get(i).isMoving()){
                                tangrammPlace.getFigures().get(i).setMoving(false);
                                for (int j = 0; j<tangrammPlace.getBackgroundFigures().size(); j++){
                                    if (tangrammPlace.getFigures().get(i).gravitatableTo(tangrammPlace.getBackgroundFigures().get(j))){
                                        double dist = Math.sqrt((tangrammPlace.getFigures().get(i).getX() - tangrammPlace.getBackgroundFigures().get(j).getX()) * (tangrammPlace.getFigures().get(i).getX() - tangrammPlace.getBackgroundFigures().get(j).getX()) + (tangrammPlace.getFigures().get(i).getY() - tangrammPlace.getBackgroundFigures().get(j).getY()) * (tangrammPlace.getFigures().get(i).getY() - tangrammPlace.getBackgroundFigures().get(j).getY()));
                                        if (dist <= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())){
                                            tangrammPlace.getFigures().get(i).setX(tangrammPlace.getBackgroundFigures().get(j).getX());
                                            tangrammPlace.getFigures().get(i).setY(tangrammPlace.getBackgroundFigures().get(j).getY());
                                            tangrammPlace.getFigures().get(i).setDraggable(false);
                                        }
                                    }
                                }
                            }
                        }

                        if (checkWin()){
                            win();
                        }

                        break;
                    default:
                        Log.d(LOG_TAG, "Default");
                        break;
                }

                tangrammPlace.invalidate();
                return true;
            }
        };

        loadImage();
    }

    private boolean checkWin(){
        for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
            if(tangrammPlace.getFigures().get(i).isDraggable()){
                return false;
            }
        }
        return true;
    }

    private void win(){
        //((TextView)findViewById(R.id.tittle)).setText("You win!");


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
            builder.setMessage("Вы прошли раздел \"Tangramm\"");
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loadImage(){
        tangrammPlace = new CanvasView(this) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                if(!isLoaded){
                    loadLevel();
                    isLoaded = true;
                }
            }
        };

        tangrammPlace.setOnTouchListener(listener);
        tangrammPlace.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ((LinearLayout)this.findViewById(R.id.imageLayout)).addView(tangrammPlace);
    }

    private void loadLevel(){
        Log.d(LOG_TAG, "Loading level");

        ArrayList<Figure> figures = new ArrayList<>();
        ArrayList<Figure> fontFigures = new ArrayList<>();

        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.query("tangrammComponents", null, null, null, null, null, null);

        levelsNumber = c.getCount();
        Log.d(LOG_TAG, Integer.toString(levelsNumber));

        if(c.moveToPosition(level)) {

            Log.d(LOG_TAG, "Position " + Integer.toString(level) + " exists in database");
            Log.d(LOG_TAG, "Figures data: " + c.getString(c.getColumnIndex("figures")) + " .... fontFigures data: " + c.getString(c.getColumnIndex("fontFigures")));

            try {
                JSONArray figuresJson = new JSONArray(c.getString(c.getColumnIndex("figures")));
                JSONArray fontFiguresJson = new JSONArray(c.getString(c.getColumnIndex("fontFigures")));

                float viewWidth = tangrammPlace.getWidth();
                float viewHeight = tangrammPlace.getHeight();

                float[] offset = tangrammPlace.getOffset();

                for (int i = 0; i<figuresJson.length(); i++){
                    JSONObject obj = new JSONObject(figuresJson.getString(i));

                    Figure figure = null;

                    switch (obj.getString("figureType")){
                        case "rectangle":
                            figure = new Rect(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("width")), getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("height")), getResources().getDisplayMetrics()), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "trigon":
                            figure = new Trigon(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("base")), getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("height")), getResources().getDisplayMetrics()), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "circle":
                            figure = new Circle(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("radius")), getResources().getDisplayMetrics()));
                            break;
                    }

                    figure.setX((float)((viewWidth - 2 * offset[0]) * figure.getRelativeX() + offset[0]));
                    figure.setY((float)((viewHeight - 2 * offset[1]) * figure.getRelativeY()) + offset[1]);
                    figure.setDraggable(true);
                    figures.add(figure);
                }

                for (int i = 0; i<fontFiguresJson.length(); i++){
                    JSONObject obj = new JSONObject(fontFiguresJson.getString(i));

                    Figure figure = null;

                    switch (obj.getString("figureType")){
                        case "rectangle":
                            figure = new Rect(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("width")), getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("height")), getResources().getDisplayMetrics()), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "trigon":
                            figure = new Trigon(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("base")), getResources().getDisplayMetrics()), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("height")), getResources().getDisplayMetrics()), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "circle":
                            figure = new Circle(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(obj.getString("radius")), getResources().getDisplayMetrics()));
                            break;
                    }

                    figure.setX((float)((viewWidth - 2 * offset[0]) * figure.getRelativeX() + offset[0]));
                    figure.setY((float)((viewHeight - 2 * offset[1]) * figure.getRelativeY()) + offset[1]);
                    figure.setColor(Color.DKGRAY);
                    figure.setDraggable(false);
                    fontFigures.add(figure);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        c.close();

        tangrammPlace.setFigures(figures);
        tangrammPlace.setBackgroundFigures(fontFigures);

        tangrammPlace.setPaintStyle(Paint.Style.FILL_AND_STROKE);

        for (int i = 0; i<tangrammPlace.getFigures().size(); i++){
            Log.d(LOG_TAG, "Calculated coords are " + Float.toString(tangrammPlace.getFigures().get(i).getX()) + " " + Float.toString(tangrammPlace.getFigures().get(i).getY()));
        }
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
                        .setMessage("Переместите все цветные фигуры на места для них. Вращайте фигуры двумя пальцами.")
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
