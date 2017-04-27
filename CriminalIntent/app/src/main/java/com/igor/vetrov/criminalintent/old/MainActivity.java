package com.igor.vetrov.criminalintent.old;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.igor.vetrov.criminalintent.CrimeListActivity;
import com.igor.vetrov.criminalintent.R;

public class MainActivity extends Activity {

    private Button mShowListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShowListView = (Button) findViewById(R.id.login_with_username);
        mShowListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CrimeListActivity.class);
                startActivity(intent);
            }
        });
    }
}
