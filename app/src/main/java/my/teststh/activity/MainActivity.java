package my.teststh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import my.teststh.R;


public class MainActivity extends AppCompatActivity {
    Button testSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        testSurfaceView = (Button) findViewById(R.id.test_surfaceview);
        testSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SinViewActivty.class);
                startActivity(intent);
            }
        });
    }
}
