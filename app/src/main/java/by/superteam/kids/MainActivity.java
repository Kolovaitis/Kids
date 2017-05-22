package by.superteam.kids;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void position(View v){
        Intent intent=new Intent(this,PositionActivity.class);
        intent.putExtra("level",0);
        startActivity(intent);
    }
    public void draw(View v){
        Intent intent=new Intent(this,DrawActivity.class);
        intent.putExtra("level",0);
        startActivity(intent);
    }
    public void puzzle(View v){
        Intent intent=new Intent(this,PuzzleActivity.class);
        startActivity(intent);
    }
    public void find(View v){
        Intent intent=new Intent(this,FindSimilarActivity.class);
        startActivity(intent);
    }
    public void findDiffs(View v){
        Intent intent=new Intent(this,FindDifference.class);
        intent.putExtra("level",0);
        startActivity(intent);
    }

}
