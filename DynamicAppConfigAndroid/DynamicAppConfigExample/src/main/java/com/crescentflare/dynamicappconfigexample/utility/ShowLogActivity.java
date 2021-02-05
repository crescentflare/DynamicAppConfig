package com.crescentflare.dynamicappconfigexample.utility;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.crescentflare.dynamicappconfigexample.R;

/**
 * The show log activity allows you to scroll through the log
 */
public class ShowLogActivity extends AppCompatActivity {

    // --
    // Initialization
    // --

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_log);
    }


    // --
    // Activity state handling
    // --

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView)findViewById(R.id.activity_show_log_text)).setText(Logger.logString());
    }
}
