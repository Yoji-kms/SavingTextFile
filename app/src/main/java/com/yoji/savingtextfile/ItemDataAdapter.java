package com.yoji.savingtextfile;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemDataAdapter extends BaseAdapter {

    private List<String> itemList;
    private LayoutInflater inflater;
    private final static String SEPARATOR = ";;;;;";
    private File dataTxtFile;
    private FileWriter dataTxtFileWriter;

    private View.OnClickListener deleteButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeItem((Integer) v.getTag());
        }
    };

    private View.OnLongClickListener viewOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            Toast.makeText(v.getContext(), itemList.get((Integer) v.getTag()), Toast.LENGTH_LONG).show();
            return true;
        }
    };

    private void removeItem(int position) {
        itemList.remove(position);
        writeContentToFile();
        notifyDataSetChanged();
    }

    public ItemDataAdapter(Context context, String[] itemList) {
        dataTxtFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "data.txt");
        if (dataTxtFile.exists()) {
            this.itemList = readContentFromFile();
        } else {
            this.itemList = new ArrayList<>(Arrays.asList(itemList));
            createDataTxtFile();
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.item_to_display, parent, false);
        }

        String item = itemList.get(position);

        TextView mainTxtView = view.findViewById(R.id.mainTextTxtViewId);
        TextView numOfSymbolsTxtView = view.findViewById(R.id.numOfSymbolTxtViewId);
        Button deleteButton = view.findViewById(R.id.deleteButtonId);

        mainTxtView.setText(item);
        numOfSymbolsTxtView.setText(String.valueOf(item.length()));
        deleteButton.setOnClickListener(deleteButtonOnClickListener);
        deleteButton.setOnLongClickListener(viewOnLongClickListener);
        deleteButton.setTag(position);
        mainTxtView.setOnLongClickListener(viewOnLongClickListener);
        mainTxtView.setTag(position);
        numOfSymbolsTxtView.setOnLongClickListener(viewOnLongClickListener);
        numOfSymbolsTxtView.setTag(position);

        return view;
    }

    private void createDataTxtFile() {
        try {
            dataTxtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeContentToFile();
    }

    private void writeContentToFile(){
        try {
            clearTextFile();
            dataTxtFileWriter = new FileWriter(dataTxtFile, true);
            for (String string : itemList) {
                dataTxtFileWriter.append(string).append(SEPARATOR).append("\n");
            }
            dataTxtFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readContentFromFile() {
        String[] contentFromFile;
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(dataTxtFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentFromFile = text.toString().split(SEPARATOR);
        return new ArrayList<>(Arrays.asList(contentFromFile));
    }

    public void appendStringToFile(String addingString) {
        try {
            dataTxtFileWriter = new FileWriter(dataTxtFile, true);
            dataTxtFileWriter.append(addingString).append(SEPARATOR);
            dataTxtFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemList = readContentFromFile();
        notifyDataSetChanged();
    }

    private void clearTextFile(){
        try {
            dataTxtFileWriter = new FileWriter(dataTxtFile);
            dataTxtFileWriter.write("");
            dataTxtFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
