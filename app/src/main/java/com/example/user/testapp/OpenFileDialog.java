package com.example.user.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by user on 14.06.2015.
 */
public class OpenFileDialog extends AlertDialog.Builder{

    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files = new ArrayList<File>();
    private TextView title;

    private static Display getDefaultDisplay(Context context){
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context){
        Point screenSize = new Point();
        getDefaultDisplay(context).getSize(screenSize);
        return screenSize;
    }

    private static int getLinearLayoutMinHeight(Context context){
        return getScreenSize(context).y;
    }

    public OpenFileDialog(Context context){
        super(context);
        title = createTitle(context);
        LinearLayout linearLayout = createMainLayout(context);
        files.addAll(getFiles(currentPath));
        ListView listView = createListView(context);
        listView.setAdapter(new FileAdapter(context, files));
        linearLayout.addView(listView);
        setCustomTitle(title)
                .setView(listView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);

    }

    private int getItemHeight(Context context){
        TypedValue value = new TypedValue();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getTheme().resolveAttribute(android.R.attr.rowHeight, value, true);
        getDefaultDisplay(context).getMetrics(metrics);
        return (int)TypedValue.complexToDimension(value.data, metrics);
    }

    private TextView createTitle(Context context){
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, android.R.style.TextAppearance_DeviceDefault_DialogWindowTitle);
        int itemHeight = getItemHeight(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        textView.setMinHeight(itemHeight);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15, 0, 0, 0);
        textView.setText(currentPath);
        return textView;
    }

    private LinearLayout createMainLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setMinimumHeight(getLinearLayoutMinHeight(context));
        return linearLayout;
    }

    private void RebuildFiles(ArrayAdapter<File> adapter){
        files.clear();
        files.addAll(getFiles(currentPath));
        adapter.notifyDataSetChanged();
        title.setText(currentPath);
    }

    private ListView createListView(Context context) {
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(i);
                if(file.isDirectory()){
                    currentPath = file.getPath();
                    RebuildFiles(adapter);
                }
            }
        });
        return listView;
    }

    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.asList(directory.listFiles());
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                if (file.isDirectory() && file2.isFile())
                    return -1;
                else if (file.isFile() && file2.isDirectory())
                    return 1;
                else return file.getPath().compareTo(file2.getPath());
            }
        });
        return fileList;

    }

    private class FileAdapter extends ArrayAdapter<File>{

        public FileAdapter(Context context, List<File> files){
            super(context, android.R.layout.simple_list_item_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView view  = (TextView) super.getView(position,convertView,parent);
            File file = getItem(position);
            view.setText(file.getName());
            return view;
        }
    }
}
