package com.brennan_hzl.photovine;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class WelcomeActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup the ImageView buttons
        //initButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    // the ImageView buttons in activity_main.xml are initialized as buttons
    private void initButtons() {
        ImageView btn = (ImageView) findViewById(R.id.ibChoosePhoto);

        // setup the listener for the large "Create Slideshow" button.
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,
                        ChoosePhotoActivity.class));
            }
        });
        btn = (ImageView) findViewById(R.id.ibViewSlideShow);

        // setup the listener for the smaller "View Slideshows" button.
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,
                        ViewSlideShowActivity.class));
            }
        });
    }
}