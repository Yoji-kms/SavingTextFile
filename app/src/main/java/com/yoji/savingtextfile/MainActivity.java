package com.yoji.savingtextfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {

    private String[] content;
    private ItemDataAdapter listItemDataAdapter;
    private final int REQUEST_CODE = 10;

    private View.OnClickListener addItemFabOnClickListener = v -> {
        Intent intent = new Intent(MainActivity.this, AddItemPopup.class);
        Blurry.with(MainActivity.this)
                .sampling(100)
                .color(Color.DKGRAY)
                .onto(findViewById(R.id.mainLayoutId));
        startActivityForResult(intent, REQUEST_CODE);
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                String result = data.getStringExtra("result");
                listItemDataAdapter.appendStringToFile(result);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = prepareContent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.list);
        FloatingActionButton addItemFab = findViewById(R.id.addItemFabId);

        listItemDataAdapter = createItemDataAdapter();

        list.setAdapter(listItemDataAdapter);
        addItemFab.setOnClickListener(addItemFabOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Blurry.delete(findViewById(R.id.mainLayoutId));
    }

    private ItemDataAdapter createItemDataAdapter() {
        return new ItemDataAdapter(this, content);
    }

    @NonNull
    private String[] prepareContent() {
        return getString(R.string.large_text).split("\n\n");
    }
}