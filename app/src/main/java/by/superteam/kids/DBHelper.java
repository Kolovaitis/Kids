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
        sqLiteDatabase.execSQL("create table findDiffs (_id integer primary key autoincrement, figures, imagesResources, wasPassed);");

        String figs[] = {
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.454545\\\", \\\"relY\\\":\\\"0.530035\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"45.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.815508\\\", \\\"relY\\\":\\\"0.40636\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"65.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.949197\\\", \\\"relY\\\":\\\"0.795053\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.133689\\\", \\\"relY\\\":\\\"0.40636\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"45.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.133689\\\", \\\"relY\\\":\\\"0.67137809\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"90.0\\\"}\"]",
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.456464\\\", \\\"relY\\\":\\\"0.124113\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.461741\\\", \\\"relY\\\":\\\"0.44326\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"65.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.303430\\\", \\\"relY\\\":\\\"0.868794\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.646437\\\", \\\"relY\\\":\\\"0.85106\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"45.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.633245\\\", \\\"relY\\\":\\\"0.14184397\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\"]",
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.291005\\\", \\\"relY\\\":\\\"0.266903\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.396825\\\", \\\"relY\\\":\\\"0.69395\\\", \\\"figureType\\\":\\\"rectangle\\\", \\\"width\\\":\\\"200.0\\\", \\\"height\\\":\\\"300.0\\\", \\\"rotate\\\":\\\"0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.026455\\\", \\\"relY\\\":\\\"0.960854\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"55.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.793650\\\", \\\"relY\\\":\\\"0.94306\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"80.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.820105\\\", \\\"relY\\\":\\\"0.56939501\\\", \\\"figureType\\\":\\\"rectangle\\\", \\\"width\\\":\\\"300.0\\\", \\\"height\\\":\\\"400.0\\\", \\\"rotate\\\":\\\"0\\\"}\"]",
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.212201\\\", \\\"relY\\\":\\\"0.169491\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"50.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.291777\\\", \\\"relY\\\":\\\"0.55932\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"50.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.761273\\\", \\\"relY\\\":\\\"0.203389\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"50.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.251989\\\", \\\"relY\\\":\\\"0.84745\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"65.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.782493\\\", \\\"relY\\\":\\\"0.83050847\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"65.0\\\"}\"]",

        };
        String[] imageData = {
                "[2130837602,2130837603]",
                "[" + Integer.toString(R.drawable.image21) + "," + Integer.toString(R.drawable.image22) + "]",
                "[" + Integer.toString(R.drawable.image31) + "," + Integer.toString(R.drawable.image32) + "]",
                "[" + Integer.toString(R.drawable.image41) + "," + Integer.toString(R.drawable.image42) + "]",

        };

        for (int i = 0; i<figs.length; i++) {
            ContentValues CV = new ContentValues();
            CV.put("figures", figs[i]);
            CV.put("imagesResources", imageData[i]);
            CV.put("wasPassed", false);

            sqLiteDatabase.insert("findDiffs", null, CV);
        }



        sqLiteDatabase.execSQL("create table tangrammComponents (_id integer primary key autoincrement, figures, fontFigures, wasPassed);");

        String[] figures = {
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.454545\\\", \\\"relY\\\":\\\"0.530035\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"100.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.815508\\\", \\\"relY\\\":\\\"0.40636\\\", \\\"figureType\\\":\\\"trigon\\\", \\\"base\\\":\\\"370.0\\\", \\\"height\\\":\\\"290.0\\\", \\\"rotate\\\":\\\"-2\\\"}\"]",
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.85\\\", \\\"relY\\\":\\\"0.37\\\", \\\"figureType\\\":\\\"rectangle\\\", \\\"width\\\":\\\"150.0\\\", \\\"height\\\":\\\"110.0\\\", \\\"rotate\\\":\\\"1\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.77\\\", \\\"relY\\\":\\\"0.8\\\", \\\"figureType\\\":\\\"trigon\\\", \\\"base\\\":\\\"220.0\\\", \\\"height\\\":\\\"170.0\\\", \\\"rotate\\\":\\\"0\\\"}\"]",

        };

        String[] fontFigures = {
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.133689\\\", \\\"relY\\\":\\\"0.40636\\\", \\\"figureType\\\":\\\"circle\\\", \\\"radius\\\":\\\"100.0\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.133689\\\", \\\"relY\\\":\\\"0.67137809\\\", \\\"figureType\\\":\\\"trigon\\\", \\\"base\\\":\\\"370.0\\\", \\\"height\\\":\\\"290.0\\\", \\\"rotate\\\":\\\"3\\\"}\"]",
                "[\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.25\\\", \\\"relY\\\":\\\"0.77\\\", \\\"figureType\\\":\\\"rectangle\\\", \\\"width\\\":\\\"150.0\\\", \\\"height\\\":\\\"110.0\\\", \\\"rotate\\\":\\\"3\\\"}\",\"{\\\"x\\\":\\\"0.0\\\", \\\"y\\\":\\\"0.0\\\", \\\"relX\\\":\\\"0.5\\\", \\\"relY\\\":\\\"0.19\\\", \\\"figureType\\\":\\\"trigon\\\", \\\"base\\\":\\\"220.0\\\", \\\"height\\\":\\\"170.0\\\", \\\"rotate\\\":\\\"0\\\"}\"]",

        };

        for (int i = 0; i<figures.length; i++) {
            ContentValues CV2 = new ContentValues();
            CV2.put("figures", figures[i]);
            CV2.put("fontFigures", fontFigures[i]);
            CV2.put("wasPassed", false);

            sqLiteDatabase.insert("tangrammComponents", null, CV2);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
//Figures data: ["{\"x\":\"0.0\", \"y\":\"0.0\", \"relX\":\"0.454545\", \"relY\":\"0.530035\", \"figureType\":\"circle\", \"radius\":\"100.0\"}","{\"x\":\"0.0\", \"y\":\"0.0\", \"relX\":\"0.815508\", \"relY\":\"0.40636\", \"figureType\":\"trigon\", \"base\":\"370.0\", \"height\":\"290.0\", \"rotate\":\"0\"}"] .... fontFigures data: ["{\"x\":\"0.0\", \"y\":\"0.0\", \"relX\":\"0.133689\", \"relY\":\"0.40636\", \"figureType\":\"circle\", \"radius\":\"100.0\"}","{\"x\":\"0.0\", \"y\":\"0.0\", \"relX\":\"0.133689\", \"relY\":\"0.67137809\", \"figureType\":\"trigon\", \"base\":\"370.0\", \"height\":\"290.0\", \"rotate\":\"0\"}"]
