package com.example.moamen.ubiss;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class TaskActivity extends AppCompatActivity {
    private CanvasView canvasView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        canvasView = (CanvasView) findViewById(R.id.canvas);
    }


}
