package by.superteam.kids;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "IAmItKidsDB";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //sqLiteDatabase.execSQL();
        sqLiteDatabase.execSQL("create table findDiffs (_id integer primary key autoincrement, figures, wasPassed);");


        ArrayList<Figure> data= new ArrayList<>();

        //data.add(
        data.add(new Circle(0.454545, 0.530035, 45));
        data.add(new Circle(0.815508, 0.40636, 65));//new Rect(0.815508, 0.40636, 200, 100);
        data.add(new Circle(0.949197, 0.795053, 55));
        data.add(new Circle(0.133689, 0.40636, 45));
        data.add(new Circle(0.133689, 0.67137809, 90));//new Trigon(0.949197, 0.795053, 200, 100);

        String figs[] = new String[data.size()];

        for (int i = 0; i<figs.length; i++){
            figs[i] = data.get(i).toString();
        }
        //{circ.toString(), rect.toString(), trigon.toString()};

        JSONArray figuresArr;
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                figuresArr = new JSONArray(figs);
            }else {
                figuresArr = new JSONArray();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            figuresArr = new JSONArray();
        }

        ContentValues CV = new ContentValues();
        CV.put("figures", figuresArr.toString());
        CV.put("wasPassed", false);

        sqLiteDatabase.insert("findDiffs", null, CV);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
