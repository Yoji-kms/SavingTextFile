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
    private final LayoutInflater inflater;
    private final static String SEPARATOR = ";;;;;";
    private final File dataTxtFile;

    private final View.OnClickListener deleteButtonOnClickListener = v -> removeItem((Integer) v.getTag());

    private final View.OnLongClickListener viewOnLongClickListener = new View.OnLongClickListener() {
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
        dataTxtFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
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
            //noinspection ResultOfMethodCallIgnored
            dataTxtFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeContentToFile();
    }

    private void writeContentToFile(){
        try (FileWriter dataTxtFileWriter = new FileWriter(dataTxtFile)) {
           for (String string : itemList) {
                dataTxtFileWriter.append(string).append(SEPARATOR).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readContentFromFile() {
        String[] contentFromFile;
        StringBuilder text = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataTxtFile))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentFromFile = text.toString().split(SEPARATOR);
        return new ArrayList<>(Arrays.asList(contentFromFile));
    }

    public void appendStringToFile(String addingString) {
        try (FileWriter dataTxtFileWriter = new FileWriter(dataTxtFile, true)) {
            dataTxtFileWriter.append(addingString).append(SEPARATOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        itemList = readContentFromFile();
        notifyDataSetChanged();
    }
}
