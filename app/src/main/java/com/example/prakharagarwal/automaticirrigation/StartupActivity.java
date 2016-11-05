package com.example.prakharagarwal.automaticirrigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by prakharagarwal on 01/11/16.
 */
public class StartupActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_startup);

        // Remove title bar
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                    final Intent mainIntent = new Intent(StartupActivity.this, MainActivity.class);
                    StartupActivity.this.startActivity(mainIntent);
                   StartupActivity.this.finish();

            }
        }, 3000);
    }
}
