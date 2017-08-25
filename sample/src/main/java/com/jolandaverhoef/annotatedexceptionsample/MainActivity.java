package com.jolandaverhoef.annotatedexceptionsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dummy dummy = new ExceptionFactory().create(Dummy.class);
        assert (dummy != null);
    }
}
