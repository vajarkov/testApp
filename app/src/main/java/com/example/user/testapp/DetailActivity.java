package com.example.user.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;



public class DetailActivity extends Activity {

    private ArrayList<QuestAnswers> questAnswerses = new ArrayList<QuestAnswers>();
    private ArrayList<Button> Buttons = new ArrayList<Button>();
    private int rightAnswer =0;
    private int nQuest = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView score = (TextView)findViewById(R.id.score);
        score.setText("Правильные ответы : "+rightAnswer);

        fillButtonArray();
        fillQusetions();
        Collections.shuffle(questAnswerses);
        fillView();
    }

    private void fillButtonArray(){
        LinearLayout layout  = (LinearLayout)findViewById(R.id.layout);
        for(int i=0; i<layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof Button){
                Buttons.add((Button)child);
            }
        }
    }

    private void fillQusetions(){
        String parseStr = "";
        HashMap <String,Boolean> parseDict = new HashMap<String, Boolean>();
        try {
            XmlPullParser parser = getResources().getXml(R.xml.questions);
            while(parser.getEventType() != XmlPullParser.END_DOCUMENT){
                if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("question")){

                    parseStr = parser.getAttributeValue(0);
                }
                if(parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("answer")){
                    parseDict.put(parser.getAttributeValue(1), Boolean.parseBoolean(parser.getAttributeValue(0)));
                }
                if(parser.getEventType() == XmlPullParser.END_TAG && parser.getName().equals("question")){
                    questAnswerses.add(new QuestAnswers(parseStr, new HashMap<String, Boolean>(parseDict)))   ;
                    parseDict.clear();
                }
                parser.next();

            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillView() {
        TextView Question = (TextView) findViewById(R.id.questView);
        Question.setText(questAnswerses.get(nQuest).question);
        int i=0;
        for (HashMap.Entry<String,Boolean> answer: questAnswerses.get(nQuest).answers.entrySet()) {
            Buttons.get(i).setText(answer.getKey());
            i++;
        }
    }

    public void OnClick(View view){
        Button button = (Button) view;
        TextView score = (TextView)findViewById(R.id.score);
        if (questAnswerses.get(nQuest).answers.get(button.getText().toString())){
            rightAnswer++;
            score.setText("Правильные ответы : "+rightAnswer);
        }
        if (nQuest<questAnswerses.size()-1){
                nQuest++;}
        else
        {
            AlertDialog msgbox = new AlertDialog.Builder(this).create();
            msgbox.setTitle("Результат");
            if (((rightAnswer*100)/questAnswerses.size())>50){
                msgbox.setMessage("Вы прошли тест");
            }
            else{
                msgbox.setMessage("Вы не прошли тест");
            }
            msgbox.setButton("ОК", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            msgbox.show();

        }
        //else result screen
        fillView();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (R.id.action_settings == id) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }
}
