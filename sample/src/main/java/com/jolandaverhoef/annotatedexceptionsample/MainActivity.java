package com.jolandaverhoef.annotatedexceptionsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jolandaverhoef.annotatedexceptionsample.models.Class1;
import com.jolandaverhoef.annotatedexceptionsample.models.Class3;
import com.jolandaverhoef.annotatedexceptionsample.models.CustomExceptionFactory;
import com.jolandaverhoef.annotatedexceptionsample.models.ExceptionFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Exception e = new ExceptionFactory().create(Class1.class);
        assert (e != null);
        Exception e2 = new CustomExceptionFactory().create(Class3.class);
        assert (e2 != null);
    }
}
