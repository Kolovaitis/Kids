package by.superteam.kids;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FindDifference extends AppCompatActivity {

    private static String LOG_TAG = "findDiffLog";

    public View.OnTouchListener onTouchListener;

    private int level;
    private ArrayList<Integer> resImages;
    private ArrayList<Figure> mDifferences;
    private int attempts = 3;
    private boolean isGameAvailable = true;
    private boolean isLoaded = false;

    private LinearLayout images;
    private ImageView first;
    private ImageView second;

    private TextView tittle;
    private LinearLayout attemptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_difference);

        images = (LinearLayout)findViewById(R.id.images);
        tittle = (TextView)findViewById(R.id.tittle);
        attemptList = (LinearLayout)findViewById(R.id.attempts);

        Intent intent = getIntent();
        level = intent.getIntExtra("level", 0);

        mDifferences = new ArrayList<>();

        Log.d(LOG_TAG, Integer.toString(level));


        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(!isLoaded){
                    loadLevel();
                    isLoaded = true;
                }

                boolean wasDiffFound = false;
                float[] coords = ((CanvasView)view).getRealCoords(motionEvent.getX(), motionEvent.getY());

                Log.d(LOG_TAG, "Canvas coords: " + coords[0] + " " + coords[1]);

                for(int i = 0; i<mDifferences.size(); i++){
                    //Log.d(LOG_TAG, "Differences " + Integer.toString(i) + " element: " + mDifferences.get(i).toString());
                    if (mDifferences.get(i).containsPoint(coords[0], coords[1]) && !wasDiffFound){

                        for(int j = 0; j<images.getChildCount(); j++) {
                            if (images.getChildAt(j).getClass().equals(CanvasView.class)) {
                                ((CanvasView) images.getChildAt(j)).addFigure(mDifferences.get(i));
                            }
                        }

                        mDifferences.remove(i);
                        wasDiffFound = true;
                    }
                }

                for(int j = 0; j<images.getChildCount(); j++) {
                    if (images.getChildAt(j).getClass().equals(CanvasView.class)) {
                        images.getChildAt(j).invalidate();
                    }
                }

                showAttempt(wasDiffFound);
                checkWin();

                return false;
            }
        };

        loadImages();
    }

    public void loadImages() {
        /*for (int i = 0; i<resImages.size(); i++){
            CanvasView canvasView = new CanvasView(this, resImages.get(i));
            canvasView.setOnTouchListener(onTouchListener);
            images.addView(canvasView);
        }*/

        first = new CanvasView(this, R.drawable.image1);
        second = new CanvasView(this, R.drawable.image2);

        first.setOnTouchListener(onTouchListener);
        second.setOnTouchListener(onTouchListener);

        images.addView(first);
        images.addView(second);
    }

    private void checkWin(){
        if(attempts <= 0 && isGameAvailable){
            lose();
        }

        if (mDifferences.size() == 0 && isGameAvailable){
            win();
        }
    }

    public void win(){
        tittle.setText("You win");
    }

    public void lose(){
        tittle.setText("You loose");
    }

    public void showAttempt(boolean isFound){
        if (isFound){
            tittle.setText("Right");
        }else {
            attempts--;
            tittle.setText("It is not difff");
        }
    }

    private void loadLevel(){

        Log.d(LOG_TAG, "Loading level");

        DBHelper dbh = new DBHelper(this);
        SQLiteDatabase db = dbh.getReadableDatabase();

        Cursor c = db.query("findDiffs", null, null, null, null, null, null);

        if(c.moveToPosition(level)) {

            Log.d(LOG_TAG, "Position " + Integer.toString(level) + " exists in database");
            String s = c.getString(c.getColumnIndex("figures"));


            Log.d(LOG_TAG, "Figures data: " + s);
            try {
                JSONArray arr = new JSONArray(s);

                float viewWidth = first.getWidth();
                float viewHeight = first.getHeight();

                float[] offset = ((CanvasView)first).getOffset();

                for (int i = 0; i<arr.length(); i++){
                    JSONObject obj = new JSONObject(arr.getString(i));

                    Figure figure = null;

                    switch (obj.getString("figureType")){
                        case "rectangle":
                            figure = new Rect(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("width")), Float.parseFloat(obj.getString("height")), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "trigon":
                            figure = new Trigon(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("base")), Float.parseFloat(obj.getString("height")), Integer.parseInt(obj.getString("rotate")));
                            break;

                        case "circle":
                            figure = new Circle(Double.parseDouble(obj.getString("relX")), Double.parseDouble(obj.getString("relY")), Float.parseFloat(obj.getString("radius")));
                            break;
                    }

                    figure.setX((float)((viewWidth - 2 * offset[0]) * figure.getRelativeX() + offset[0]));
                    figure.setY((float)((viewHeight - 2 * offset[1]) * figure.getRelativeY()) + offset[1]);
                    mDifferences.add(figure);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i<mDifferences.size(); i++){
            Log.d(LOG_TAG, "Calculated coords are " + Float.toString(mDifferences.get(i).getX()) + " " + Float.toString(mDifferences.get(i).getY()));
        }
    }
}
