package by.superteam.kids;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by Влад on 15.04.2017.
 */

public class FindSimilarActivity extends AppCompatActivity {
    int fail;
    int right;
    int level = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

    }
    public void level2(View v){
       Float a;
       a= v.getAlpha();
       if(a.toString().equals("1.0")){
           ((ImageView)v).setImageResource(R.mipmap.error);
           v.setClickable(false);
            fail++;
        }
       else{
           ((ImageView)v).setImageResource(R.mipmap.tick);
           v.setClickable(false);
           right++;
       }

      if(fail==3){
          fail=0;
          right=0;
          AlertDialog.Builder builder = new AlertDialog.Builder(FindSimilarActivity.this);
          builder.setTitle("Ты проиграл")
                  .setMessage("Попробуй еще!")
                  .setIcon(R.mipmap.error)
                  .setCancelable(false)
                  .setNegativeButton("ОК",
                          new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  levelChooser();

                              }
                          });
          AlertDialog alert = builder.create();
          alert.show();
      }
      if(right==2){
          fail=0;
          right=0;
          AlertDialog.Builder builder = new AlertDialog.Builder(FindSimilarActivity.this);
          builder.setTitle("Ты победил")
                  .setMessage("Уровень " + level + " пройден!")
                  .setIcon(R.drawable.human_button_touched)
                  .setCancelable(false)
                  .setNeutralButton("Заново", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                         levelChooser();
                      }
                  })
                  .setNegativeButton("В меню",
                          new DialogInterface.OnClickListener() {
                              public void onClick(DialogInterface dialog, int id) {
                                  finish();
                              }
                          });
          if(level<5){
              builder.setPositiveButton("Следующий", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                      level++;
                      levelChooser();
                  }
              });
          }else{
              builder.setMessage("Ты прошел раздел \"Найди похожие\"");
          }
          AlertDialog alert = builder.create();
          alert.show();

      }
    }
    public void levelChooser(){
        switch(level){
            case 1:
                setContentView(R.layout.activity_find);
                    break;
            case 2:
                setContentView(R.layout.activity_find2);
                break;
            case 3:
                setContentView(R.layout.activity_find3);
                break;
            case 4:
                setContentView(R.layout.activity_find4);
                break;
            case 5:
                setContentView(R.layout.activity_find5);
                break;

        }

    }

}
