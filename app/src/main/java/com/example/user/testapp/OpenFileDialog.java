package com.example.user.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by user on 14.06.2015.
 */
public class OpenFileDialog extends AlertDialog.Builder{

    private String currentPath = Environment.getExternalStorageDirectory().getPath();

    public OpenFileDialog(Context context){
        super(context);
        setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null)
                .setItems(getFiles(currentPath),null);

    }

    private String[] getFiles(String directoryPath){
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        String[] result = new String[files.length];
        for (int i = 0; i< files.length; i++){
            result[i] = files[i].getName();
        }
        return result;
    }
}
