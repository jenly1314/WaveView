package com.king.waveview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.king.view.waveview.WaveView;

public class MainActivity extends AppCompatActivity {

    WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
