package com.yoji.savingtextfile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class AddItemPopup extends Activity {

    private EditText addItemEdtTxt;
    private Button addBtn;

    private View.OnClickListener cancelBtnOnClickListener = v -> {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    };

    private View.OnClickListener addBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String result = addItemEdtTxt.getText().toString();
            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String addingText = addItemEdtTxt.getText().toString().trim();
            addBtn.setEnabled(!addingText.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_element);

        setWidthAndHeight();

        Button cancelBtn = findViewById(R.id.cancelButtonId);
        cancelBtn.setOnClickListener(cancelBtnOnClickListener);

        addItemEdtTxt = findViewById(R.id.enterTextToAddEdtTxtId);
        addItemEdtTxt.addTextChangedListener(textWatcher);

        addBtn = findViewById(R.id.addButtonId);
        addBtn.setOnClickListener(addBtnOnClickListener);
    }

    public void setWidthAndHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

        getWindow().setLayout((int)(width*0.9), height);
    }
}
