package by.superteam.kids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class CanvasView extends AppCompatImageView{
    private static String LOG_TAG = "findDiffLog";

    private Paint paint;

    private boolean isBoundsSet = false;
    private float[] offset = new float[2];

    private ArrayList<Figure> figures;
    private ArrayList<Figure> backgroundFigures;

    public CanvasView(Context context, int imgRes) {
        super(context);

        this.figures = new ArrayList<>();
        this.backgroundFigures = new ArrayList<>();
        this.paint = new Paint();


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgRes);
        this.setImageBitmap(bitmap);

        defaultParams();
        defaultPaintStyle();
    }

    public CanvasView(Context context) {
        super(context);
        this.figures = new ArrayList<>();
        this.backgroundFigures = new ArrayList<>();
        this.paint = new Paint();

        defaultParams();
        defaultPaintStyle();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.figures = new ArrayList<>();
        this.backgroundFigures = new ArrayList<>();
        this.paint = new Paint();

        defaultParams();
        defaultPaintStyle();
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.figures = new ArrayList<>();
        this.backgroundFigures = new ArrayList<>();
        this.paint = new Paint();

        defaultParams();
        defaultPaintStyle();
    }

    public void setFigures(ArrayList<Figure> figures) {
        this.figures = figures;
    }

    public void setBackgroundFigures(ArrayList<Figure> backgroundFigures) {
        this.backgroundFigures = backgroundFigures;
    }

    public void addFigure(Figure figure){
        figures.add(figure);
    }

    public void addFontFigure(Figure figure){
        figures.add(figure);
    }

    public void setImage(int imgRes) {
        this.setImageResource(imgRes);
    }

    public void setPaintStyle(Paint.Style style){
        paint.setStyle(style);
    }

    public void setPaintStyle(Paint.Style style, int strokeWidth){
        paint.setStyle(style);
        paint.setStrokeWidth(strokeWidth);
    }

    private void calculateOffset(){
        float[] values = new float[9];

        Matrix m = this.getImageMatrix();
        m.getValues(values);

//        String logMeassage = "Values in array: ";
//        for(int i = 0; i<values.length; i++){
//        }


        Log.d(LOG_TAG, "Values in array: " + values.toString());

        offset[0] = values[2];
        offset[1] = values[5];

        Log.d(LOG_TAG, "Another offset is " + Float.toString(values[5]) + " " + Float.toString(values[2]));
    }

    public void defaultPaintStyle(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    private void defaultParams(){
        //this.setBackgroundColor(Color.WHITE);
        this.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
    }

    public ArrayList<Figure> getFigures() {
        return figures;
    }

    public ArrayList<Figure> getBackgroundFigures() {
        return backgroundFigures;
    }

    public float[] getOffset() {
        if(!isBoundsSet){
            calculateOffset();
            isBoundsSet = true;
        }
        return offset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFigures(canvas);
    }

    public void drawRect(Canvas canvas, int i){
        this.paint.setColor(figures.get(i).getColor());
        canvas.drawRect(figures.get(i).getX(), figures.get(i).getY(), figures.get(i).getX() + ((Rect)figures.get(i)).getWidth(), figures.get(i).getY() + ((Rect)figures.get(i)).getHeight(), paint);
    }

    public void drawTrigon(Canvas canvas, int i){
        this.paint.setColor(figures.get(i).getColor());

        float coords[][] = ((Trigon)figures.get(i)).getCoords();
        float subcoords[] = new float[12];
        for (int j = 0; j<coords.length; j++){
            for (int k = 0; k<4; k++){
                if(k < 2) {
                    subcoords[j * 4 + k] = coords[j % coords.length][k % 2];
                }else{
                    subcoords[j * 4 + k] = coords[(j + 1) % coords.length][k % 2];
                }
            }
        }

        canvas.drawLines(subcoords, paint);
    }

    public void drawCircle(Canvas canvas, int i){
        paint.setColor(figures.get(i).getColor());
        canvas.drawCircle(figures.get(i).getX(), figures.get(i).getY(), ((Circle)figures.get(i)).getRadius(), paint);
    }

    public void drawFigures(Canvas canvas) {
        if(!isBoundsSet){
            calculateOffset();
            isBoundsSet = true;
        }

        for (int i = 0; i < backgroundFigures.size(); i++){
            switch (backgroundFigures.get(i).getFigureType()) {
                case CIRCLE:
                    drawCircle(canvas, i);
                    break;
                case TRIGON:
                    drawTrigon(canvas, i);
                    break;
                case RECTANGLE:
                    drawRect(canvas, i);
                    break;
            }
        }

        for (int i = 0; i < figures.size(); i++){
            /*figures.get(i).setX((float)((this.getWidth() - offset[0]) * figures.get(i).getRelativeX()));
            figures.get(i).setY((float)((this.getHeight() - offset[1]) * figures.get(i).getRelativeY()));*/
            switch (figures.get(i).getFigureType()) {
                case CIRCLE:
                    drawCircle(canvas, i);
                    break;
                case TRIGON:
                    drawTrigon(canvas, i);
                    break;
                case RECTANGLE:
                    drawRect(canvas, i);
                    break;
            }
        }
    }

    public float[] getRealCoords(float x, float y){

        if(!isBoundsSet){
            calculateOffset();
            isBoundsSet = true;
        }

//        float[] realCoords = new float[2];
//
//        realCoords[0] = (x - offset[0]);
//        realCoords[1] = (y - offset[1]);
//
//        return realCoords;

        return new float[]{x, y};
    }
}

abstract class Figure {
    private float x;
    private float y;
    private double relativeX;
    private double relativeY;
    private int color;
    private int rotateAngle;
    private boolean draggable;
    private boolean visible;

    enum FigureTypes{
        RECTANGLE,
        TRIGON,
        CIRCLE
    }

    public Figure(double relativeX, double relativeY){
        this.x = 0;
        this.y = 0;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.color = Color.GREEN;
        this.rotateAngle = 0;
    }


    public Figure(double relativeX, double relativeY, int rotateAngle){
        this.x = 0;
        this.y = 0;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.color = Color.GREEN;
        this.rotateAngle = rotateAngle;
    }

    public Figure(double relativeX, double relativeY, int rotateAngle, int color){
        this.x = 0;
        this.y = 0;
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.color = color;
        this.rotateAngle = rotateAngle;
    }

    public Figure(float x, float y){
        this.x = x;
        this.y = y;
        this.color = Color.GREEN;
        this.rotateAngle = 0;
    }


    public Figure(float x, float y, int rotateAngle){
        this.x = x;
        this.y = y;
        this.color = Color.GREEN;
        this.rotateAngle = rotateAngle;
    }

    public Figure(float x, float y, int rotateAngle, int color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.rotateAngle = rotateAngle;
    }
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setRelativeX(double relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(double relativeY) {
        this.relativeY = relativeY;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getRelativeX() {
        return relativeX;
    }

    public double getRelativeY() {
        return relativeY;
    }

    public int getColor() {
        return color;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getRotateAngle() {
        return rotateAngle;
    }

    public void increaseRotate(){
        this.rotateAngle++;
    }

    public void decreaseRotate(){
        this.rotateAngle--;
    }

    public abstract Figure.FigureTypes getFigureType();

    public abstract boolean containsPoint(float x, float y);

    public abstract String toString();
}

class Rect extends Figure {
    public static Figure.FigureTypes FigureType = Figure.FigureTypes.RECTANGLE;
    public static int RECT_ANGLE = 4;

    private float width;
    private float height;

    private float[][] coords;

    public Rect(float x, float y, float width, float height) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    public Rect(float x, float y, float width, float height, int rotateAngle) {
        super(x, y, rotateAngle);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    public Rect(float x, float y, float width, float height, int rotateAngle, int color) {
        super(x, y, rotateAngle, color);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    public Rect(double relativeX, double relativeY, float width, float height) {
        super(relativeX, relativeY);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    public Rect(double relativeX, double relativeY, float width, float height, int rotateAngle) {
        super(relativeX, relativeY, rotateAngle);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    public Rect(double relativeX, double relativeY, float width, float height, int rotateAngle, int color) {
        super(relativeX, relativeY, rotateAngle, color);
        this.width = width;
        this.height = height;
        this.coords = new float[RECT_ANGLE][2];

        calculateCoords();
    }

    @Override
    public void setX(float x){
        super.setX(x);
        calculateCoords();
    }

    @Override
    public void setY(float y){
        super.setY(y);
        calculateCoords();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float[][] getCoords() {
        return coords;
    }

    public void calculateCoords(){

        float diag = (float) Math.sqrt(this.width * this.width + this.height * this.height);
        double diagAngle = Math.atan(this.height / this.width);

        float angleX = (float) (this.getX()  - (diag / 2) * Math.cos(diagAngle + Math.PI / 6 * this.getRotateAngle()));
        float angleY = (float) (this.getY() - (diag / 2) * Math.sin(diagAngle + Math.PI / 6 * this.getRotateAngle()));

        coords[0][0] = angleX;
        coords[1][0] = (float) (angleX + this.width * Math.cos(Math.PI / 6 * this.getRotateAngle()));
        coords[2][0] = (float) (angleX + diag * Math.cos(diagAngle + Math.PI / 6 * this.getRotateAngle()));
        coords[3][0] = (float) (angleX - this.height * Math.cos(Math.PI / 2 - Math.PI / 6 * this.getRotateAngle()));

        coords[0][1] = angleY;
        coords[1][1] = (float) (angleY + this.width * Math.sin(Math.PI / 6 * this.getRotateAngle()));
        coords[2][1] = (float) (angleY + diag * Math.sin(diagAngle + Math.PI / 6 * this.getRotateAngle()));
        coords[3][1] = (float) (angleY + this.height * Math.sin(Math.PI / 2 - Math.PI / 6 * this.getRotateAngle()));
    }

    @Override
    public Figure.FigureTypes getFigureType() {
        return FigureType;
    }

    @Override
    public boolean containsPoint(float x, float y) {
        float compositions[] = new float[RECT_ANGLE];

        int resultCalculation = 0;

        for (int i = 0; i<RECT_ANGLE; i++){
            compositions[i] = (coords[i % RECT_ANGLE][0] - x) * (coords[(i + 1) % RECT_ANGLE][1] - coords[i % RECT_ANGLE][1]) - (coords[(i + 1) % RECT_ANGLE][0] - coords[i % RECT_ANGLE][0]) * (coords[i % RECT_ANGLE][1] - y);

            if(compositions[i] == 0){
                return true;
            }

            if(compositions[i] < 0){
                resultCalculation--;
            }else {
                resultCalculation++;
            }
        }
        //(x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0);

        /*for (int i = 0; i<RECT_ANGLE; i++){
            if(compositions[i] == 0){
                return true;
            }
        }


        for(int i = 0; i<RECT_ANGLE; i++){
            if(compositions[i] < 0){
                resultCalculation--;
            }else {
                resultCalculation++;
            }
        }*/

        if(resultCalculation == RECT_ANGLE || resultCalculation == -RECT_ANGLE){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        //String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"figureType\":\"rectangle\", \"width\":\"" + this.getWidth() + "\", \"height\":\"" + this.getHeight() + "\", \"rotate\":\"" + this.getRotateAngle() + "\"}";
        String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"relX\":\"" + this.getRelativeX() + "\", \"relY\":\"" + this.getRelativeY() + "\", \"figureType\":\"rectangle\", \"width\":\"" + this.getWidth() + "\", \"height\":\"" + this.getHeight() + "\", \"rotate\":\"" + this.getRotateAngle() + "\"}";
        return respponse;
    }
}

class Trigon extends Figure {
    public static Figure.FigureTypes FigureType = Figure.FigureTypes.TRIGON;
    public static int TRIGON_ANGLE = 3;

    private float base;
    private float height;

    private double baseAngle;

    private float[][] coords;

    public Trigon(float x, float y, float base, float height) {
        super(x, y);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];

        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    public Trigon(float x, float y, float base, float height, int rotateAngle) {
        super(x, y, rotateAngle);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];


        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    public Trigon(float x, float y, float base, float height, int rotateAngle, int color) {
        super(x, y, rotateAngle, color);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];


        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    public Trigon(double relativeX, double relativeY, float base, float height) {
        super(relativeX, relativeY);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];


        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    public Trigon(double relativeX, double relativeY, float base, float height, int rotateAngle) {
        super(relativeX, relativeY, rotateAngle);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];


        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    public Trigon(double relativeX, double relativeY, float base, float height, int rotateAngle, int color) {
        super(relativeX, relativeY, rotateAngle, color);
        this.base = base;
        this.height = height;
        this.coords = new float[TRIGON_ANGLE][2];


        baseAngle = Math.atan(height / base * 2);

        calculateCoords();
    }

    @Override
    public void setX(float x){
        super.setX(x);
        calculateCoords();
    }

    @Override
    public void setY(float y){
        super.setY(y);
        calculateCoords();
    }

    public float getBase() {
        return base;
    }

    public float getHeight() {
        return height;
    }

    public float[][] getCoords() {
        return coords;
    }

    public void calculateCoords(){
        float catheter = (float) (this.base * Math.sin(baseAngle));

        float angleX = (float) (this.getX() + (this.height / 2) * Math.cos(Math.PI / 2 - Math.PI / 6 * this.getRotateAngle()) - catheter * Math.cos(baseAngle - Math.PI / 6 * this.getRotateAngle()));
        float angleY = (float) (this.getY() - (this.height / 2) * Math.sin(Math.PI / 2 - Math.PI / 6 * this.getRotateAngle()) + catheter * Math.sin(baseAngle - Math.PI / 6 * this.getRotateAngle()));

        coords[0][0] = angleX;
        coords[1][0] = (float) (angleX + this.base * Math.cos(Math.PI / 6 * this.getRotateAngle()));
        coords[2][0] = (float) (angleX + catheter * Math.cos(baseAngle - Math.PI / 6 * this.getRotateAngle()));

        coords[0][1] = angleY;
        coords[1][1] = (float) (angleY + this.base * Math.sin(Math.PI / 6 * this.getRotateAngle()));
        coords[2][1] = (float) (angleY - catheter * Math.sin(baseAngle - Math.PI / 6 * this.getRotateAngle()));
    }

    @Override
    public Figure.FigureTypes getFigureType() {
        return FigureType;
    }

    @Override
    public boolean containsPoint(float x, float y) {
        float compositions[] = new float[TRIGON_ANGLE];

        int resultCalculation = 0;

        for (int i = 0; i<TRIGON_ANGLE; i++){
            compositions[i] = (coords[i % TRIGON_ANGLE][0] - x) * (coords[(i + 1) % TRIGON_ANGLE][1] - coords[i % TRIGON_ANGLE][1]) - (coords[(i + 1) % TRIGON_ANGLE][0] - coords[i % TRIGON_ANGLE][0]) * (coords[i % TRIGON_ANGLE][1] - y);

            if(compositions[i] == 0){
                return true;
            }

            if(compositions[i] < 0){
                resultCalculation--;
            }else {
                resultCalculation++;
            }
        }
        //(x1 - x0) * (y2 - y1) - (x2 - x1) * (y1 - y0);

        /*for (int i = 0; i<TRIGON_ANGLE; i++){
            if(compositions[i] == 0){
                return true;
            }
        }

        for(int i = 0; i<TRIGON_ANGLE; i++){
            if(compositions[i] < 0){
                resultCalculation--;
            }else {
                resultCalculation++;
            }
        }*/

        if(resultCalculation == TRIGON_ANGLE || resultCalculation == -TRIGON_ANGLE){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        //String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"figureType\":\"trigon\", \"base\":\"" + this.getBase() + "\", \"height\":\"" + this.getHeight() + "\", \"rotate\":\"" + this.getRotateAngle() + "\"}";
        String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"relX\":\"" + this.getRelativeX() + "\", \"relY\":\"" + this.getRelativeY() + "\", \"figureType\":\"trigon\", \"base\":\"" + this.getBase() + "\", \"height\":\"" + this.getHeight() + "\", \"rotate\":\"" + this.getRotateAngle() + "\"}";
        return respponse;
    }
}

class Circle extends Figure {
    public static Figure.FigureTypes FigureType = Figure.FigureTypes.CIRCLE;
    public static int CIRCLE_ANGLE = 0;

    private float radius;

    public Circle(float x, float y, float radius) {
        super(x, y);
        this.radius = radius;

    }

    public Circle(float x, float y, float radius, int color) {
        super(x, y, 0, color);
        this.radius = radius;

    }

    public Circle(double relativeX, double relativeY, float radius) {
        super(relativeX, relativeY);
        this.radius = radius;
    }

    public Circle(double relativeX, double relativeY, float radius, int color) {
        super(relativeX, relativeY, color);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public Figure.FigureTypes getFigureType() {
        return FigureType;
    }

    @Override
    public boolean containsPoint(float x, float y) {
        float distance = (x - this.getX()) * (x - this.getX()) + (y - this.getY()) * (y - this.getY());
        float radSquare = this.radius * this.radius;

        if(radSquare > distance){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        //String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"figureType\":\"circle\", \"radius\":\"" + this.getRadius() + "\"}";
        String respponse = "{" + "\"x\":\"" + this.getX() + "\", \"y\":\"" + this.getY() + "\", \"relX\":\"" + this.getRelativeX() + "\", \"relY\":\"" + this.getRelativeY() + "\", \"figureType\":\"circle\", \"radius\":\"" + this.getRadius() + "\"}";
        return respponse;
    }
}