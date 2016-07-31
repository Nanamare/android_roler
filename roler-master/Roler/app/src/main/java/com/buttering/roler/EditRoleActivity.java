package com.buttering.roler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by WonYoung on 16. 7. 31..
 */
public class EditRoleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_role);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
    }
}
