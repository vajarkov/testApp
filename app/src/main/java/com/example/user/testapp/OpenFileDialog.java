package com.example.user.testapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
/**
 * Created by user on 14.06.2015.
 */
public class OpenFileDialog extends AlertDialog.Builder{

    private String currentPath = Environment.getExternalStorageDirectory().getPath();
    private List<File> files = new ArrayList<File>();
    private TextView title;
    private ListView listView;
    private FilenameFilter filenameFilter;
    private int selectedIndex = -1;
    private OpenDialogListener listener;
    private Drawable folderIcon;
    private Drawable fileIcon;
    private String accessDeniedMessage;
    private Context context;


    public interface OpenDialogListener {
        public void OnSelectedFile(String fileName);
    }

    public OpenFileDialog setOpenDialogListener(OpenDialogListener listener){
        this.listener = listener;
        return this;
    }



    private class FileAdapter extends ArrayAdapter<File>{

        public FileAdapter(Context context, List<File> files){
            super(context, android.R.layout.simple_list_item_1, files);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            TextView view  = (TextView) super.getView(position,convertView,parent);
            File file = getItem(position);
            if (view != null) {
                view.setText(file.getName());
                if (file.isDirectory()) {
                    setDrawable(view, folderIcon);
                } else {
                    setDrawable(view, fileIcon);
                    if (selectedIndex == position)
                        view.setBackgroundColor(getContext().getResources().getColor(android.R.color.background_dark));
                    else
                        view.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
                }
            }
            return view;
        }
        private void setDrawable(TextView view, Drawable drawable) {
            if (view != null) {
                if (drawable != null) {
                    drawable.setBounds(0, 0, 60, 60);
                    view.setCompoundDrawables(drawable, null, null, null);
                } else {
                    view.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }
    private static Display getDefaultDisplay(Context context){
        return ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    private static Point getScreenSize(Context context){
        Point screenSize = new Point();
        //try {
        //    getDefaultDisplay(context).getSize(screenSize);
        //}catch(NoSuchMethodError ignore){
            screenSize.x = getDefaultDisplay(context).getWidth();
            screenSize.y = getDefaultDisplay(context).getHeight();
        //}
        return screenSize;
    }

    private static int getLinearLayoutMinHeight(Context context){
        return getScreenSize(context).y;
    }

    public OpenFileDialog(Context context){
        super(context);
        title = createTitle(context);
        changeTitle(context);
        LinearLayout linearLayout = createMainLayout(context);
        linearLayout.addView(createBackItem(context));
        //files.addAll(getFiles(currentPath));
        listView = createListView(context);
        listView.setAdapter(new FileAdapter(context, files));
        linearLayout.addView(listView);
        setCustomTitle(title)
                .setView(linearLayout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if (selectedIndex> -1 && listener != null){
                            listener.OnSelectedFile(listView.getItemAtPosition(selectedIndex).toString());
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null);


    }

    @Override
    public AlertDialog show() {
        files.addAll(getFiles(currentPath));
        listView.setAdapter(new FileAdapter(context, files));
        return super.show();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) context.getView(position, convertView, parent);
        File file = context.getItem(position);
        view.setText(file.getName());
        if (selectedIndex == position)
            view.setBackgroundColor(context.getResources().getColor(android.R.color.background_light));
        else
            view.setBackgroundColor(context.getResources().getColor(android.R.color.background_dark));
        return view;
    }

    private int getItemHeight(Context context){
        TypedValue value = new TypedValue();
        DisplayMetrics metrics = new DisplayMetrics();
        context.getTheme().resolveAttribute(android.R.attr.rowHeight, value, true);
        getDefaultDisplay(context).getMetrics(metrics);
        return (int)TypedValue.complexToDimension(value.data, metrics);
    }

    private TextView createTextView(Context context, int style){
        TextView textView = new TextView(context);
        textView.setTextAppearance(context, style);
        int itemHeight = getItemHeight(context);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight));
        textView.setMinHeight(itemHeight);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(15, 0, 0, 0);
        return textView;
    }

    private TextView createTitle(Context context){
        TextView textView = createTextView(context,android.R.style.TextAppearance_DialogWindowTitle);
        return textView
    }

    private TextView createBackItem(final Context context){
        TextView textView = createTextView(context,android.R.style.TextAppearance_Small);
        Drawable drawable = context.getResources().getDrawable(android.R.drawable.ic_menu_directions);
        drawable.setBounds(0,0,60,60);
        textView.setCompoundDrawables(drawable,null,null,null);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(currentPath);
                File parentDirectory = file.getParentFile();
                if(parentDirectory!=null){
                    currentPath = parentDirectory.getPath();
                    RebuildFiles(((FileAdapter) listView.getAdapter()), context);
                }
            }
        });
        return textView;
    }

    private LinearLayout createMainLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setMinimumHeight(getLinearLayoutMinHeight(context));
        return linearLayout;
    }

    private void RebuildFiles(ArrayAdapter<File> adapter, Context context){
        try{
            List<File> fileList = getFiles(currentPath);
            files.clear();
            selectedIndex = -1;
            files.addAll(fileList);
            adapter.notifyDataSetChanged();
            changeTitle(context);
        }
        catch (NullPointerException e){
            Toast.makeText(context, android.R.string.unknownName, Toast.LENGTH_SHORT).show();
        }
    }

    private ListView createListView(final Context context) {
        ListView listView = new ListView(context);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ArrayAdapter<File> adapter = (FileAdapter) adapterView.getAdapter();
                File file = adapter.getItem(i);
                if(file.isDirectory()){
                    currentPath = file.getPath();
                    RebuildFiles(adapter, context);
                }else{
                    if (i != selectedIndex)
                        selectedIndex = i;
                    else
                        selectedIndex = -1;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return listView;
    }

    private List<File> getFiles(String directoryPath){
        File directory = new File(directoryPath);
        List<File> fileList = Arrays.asList(directory.listFiles(filenameFilter));
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



    public int getTextWidth(String text,Paint paint){
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.left + bounds.width() + 80;
    }

    private void changeTitle(Context context){
        String titleText = currentPath;

        int screenWidth = getScreenSize(context).x;
        int maxWidth = (int) (screenWidth * 0.99);
        if (getTextWidth(titleText, title.getPaint()) > maxWidth){
            while (getTextWidth("..." + titleText, title.getPaint())>maxWidth){
                int start = titleText.indexOf("/",2);
                if (start > 0)
                    titleText = titleText.substring(start);
                else
                    titleText = titleText.substring(2);

            }
            title.setText("..." + titleText);
        }
        else {
            title.setText(titleText);
        }
    }

    public OpenFileDialog setFilter(final String filter){
        filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String filename) {
                File tempFile = new File(String.format("%s/%s", file.getPath(), filename));
                if (tempFile.isFile())
                    return tempFile.getName().matches(filter);
                return true;
            }
        }
    }
}
