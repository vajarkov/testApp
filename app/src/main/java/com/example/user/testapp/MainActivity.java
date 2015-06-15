package com.example.user.testapp;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.logging.Level;
import java.util.logging.Logger;


public class MainActivity extends Activity {

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
        startActivity(intent);
    }

    public void LoadXML(View view) {
        /*OpenFileDialog fileDialog = new OpenFileDialog(this)
                .setFilter(".*\\.xml")
                .setOpenDialogListener(new OpenFileDialog.OpenDialogListener() {
                    @Override
                    public void OnSelectedFile(String fileName) {
                        Toast.makeText(getApplicationContext(),fileName, Toast.LENGTH_LONG).show();
                    }
                });
        fileDialog.show();*/

        Intent intent = new Intent(getBaseContext(), FileDialog.class);
        intent.putExtra(FileDialog.START_PATH, "/sdcard");
        //can user select directories or not
        intent.putExtra(FileDialog.CAN_SELECT_DIR, true);

        //alternatively you can set file filter
        intent.putExtra(FileDialog.FORMAT_FILTER, new String[] { "xml" });

        startActivityForResult(intent, RESULT_OK);
    }

    public synchronized void onActivityResult(final int requestCode,
                                              int resultCode, final Intent data) {
        System.out.println(requestCode);
        System.out.println(resultCode);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == RESULT_OK) {
                System.out.println("Loading...");
            }

            String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
        }

    }
}
