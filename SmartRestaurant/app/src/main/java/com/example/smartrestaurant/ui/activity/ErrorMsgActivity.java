package com.example.smartrestaurant.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smartrestaurant.R;

/**
 * Created by Dell 5521 on 4/10/2018.
 */

public class ErrorMsgActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error_msg_activity);
        String txt = getIntent().getStringExtra("Ex");
        ((TextView)findViewById(R.id.previewTV)).setMovementMethod(new ScrollingMovementMethod());
        ((TextView)findViewById(R.id.previewTV)).setText(""+txt);

        ((Button)findViewById(R.id.closeNow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
