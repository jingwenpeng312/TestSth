package my.teststh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import my.teststh.R;


public class MainActivity extends AppCompatActivity {
    Button testSurfaceView1, testSurfaceView2;
    Button shortCutView;
    Button tessView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        testSurfaceView1 = (Button) findViewById(R.id.test_surfaceview);
        testSurfaceView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SinViewActivty.class);
                startActivity(intent);
            }
        });
        testSurfaceView2 = (Button) findViewById(R.id.test_surfaceview2);
        testSurfaceView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TouchPathActivity.class);
                startActivity(intent);
            }
        });

        shortCutView = (Button) findViewById(R.id.test_shortcut);
        shortCutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShortCutBadgerActivity.class);
                startActivity(intent);
            }
        });


        tessView = (Button) findViewById(R.id.test_tess);
        tessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TessViewActivity2.class);
                startActivity(intent);
            }
        });
    }
}
