package com.yoji.savingtextfile;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({Code.REQUEST_CODE, Code.READ_STORAGE})
public @interface Code {
    int REQUEST_CODE = 90;
    int READ_STORAGE = 10;
}