package com.example.user.testapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.*;


public class MainActivity extends Activity{
    private final int REQUEST_LOAD = 1;
    private String pathXML = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void exitClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void startClicked(View view) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("xml_path",pathXML);
        startActivity(intent);
    }

    public void LoadXML(View view) {

        Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");

        intent.putExtra(FileDialog.CAN_SELECT_DIR, false);
        intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);

        intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "xml" });

        startActivityForResult(intent, REQUEST_LOAD);
    }



    @Override
    public synchronized void onActivityResult(final int requestCode,
                                              int resultCode, final Intent data) {
        System.out.println(requestCode);
        System.out.println(resultCode);
        if (resultCode == Activity.RESULT_OK) {
            pathXML = data.getStringExtra(FileDialog.RESULT_PATH);
            TextView tvXML = (TextView) findViewById(R.id.tvXMLpath);
            tvXML.setText(pathXML);
            //AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            //alert.setMessage(filePath).setNegativeButton("OK",null);
            //alert.show();
        }

    }
}
