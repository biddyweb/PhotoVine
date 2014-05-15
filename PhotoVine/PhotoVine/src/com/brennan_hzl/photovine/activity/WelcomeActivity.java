package com.brennan_hzl.photovine.activity;


import com.brennan_hzl.photovine.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // setup the ImageView buttons
        initButtons();
    }

    // the ImageView buttons in activity_main.xml are initialized as buttons
    private void initButtons() {
        ImageView btn = (ImageView) findViewById(R.id.ibChoosePhoto);

        // setup the listener for the large "Create Slideshow" button.
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,
                        ChooseImageActivity.class));
            }
        });
        btn = (ImageView) findViewById(R.id.ibViewSlideShow);

        // setup the listener for the smaller "View Slideshows" button.
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(WelcomeActivity.this,
//                        ViewSlideShowActivity.class));
            }
        });
    }
}