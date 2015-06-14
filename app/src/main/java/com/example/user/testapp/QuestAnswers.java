package com.example.user.testapp;

import java.util.HashMap;

/**
 * Created by user on 13.06.2015.
 */
public class QuestAnswers {
    public String question;
    public HashMap<String,Boolean> answers;
    public QuestAnswers(String q, HashMap<String,Boolean> ans){
        this.answers = ans;
        this.question = q;
    }
}
