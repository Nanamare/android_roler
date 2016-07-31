package com.buttering.roler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    Button signup_btn_ok;
    EditText signup_edit_password;
    EditText signup_edit_email;
    EditText signup_edit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        signup_btn_ok = (Button) findViewById(R.id.signup_btn_ok);
        signup_edit_password = (EditText) findViewById(R.id.signup_edit_password);
        signup_edit_email = (EditText)findViewById(R.id.signup_edit_email);
        signup_edit_name = (EditText)findViewById(R.id.signup_edit_name);

        Button btn = (Button) findViewById(R.id.signup_btn_toplan);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // ?뚯뒪?몃? ?꾪빐 ?≫떚鍮꾪떚 ?대룞. planActivity濡?
                    Intent intentSubActivity = new Intent(SignUpActivity.this, PlanActivity.class);
                    startActivity(intentSubActivity);

                    // ?쒕쾭??id, pw 留욌뒗吏 ?뺤씤 ?붿껌

                    // 留욎쑝硫?濡쒓렇???깃났, PlanActivity
                    // ?由щ㈃ 濡쒓렇???ㅽ뙣, ?ㅽ뙣硫붿떆吏
                }
            });
        }

        signup_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw,email,name;
                pw = signup_edit_password.getText().toString();
                email = signup_edit_email.getText().toString();
                name = signup_edit_name.getText().toString();
                //?곗씠??踰좎씠?ㅼ뿉 媛믪쓣 ?ｊ퀬 濡쒓렇???≫떚鍮꾪떚濡?
                Intent intent = new Intent(getApplicationContext(),LogInActivity.class);
                startActivity(intent);
            }
        });

        signup_edit_email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable theWatchedText) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int start, int after, int count) {
                int inputSize = signup_edit_email.getText().length();
                if (inputSize <= 0) {
                    signup_btn_ok.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "?대찓?쇱쓣 ?낅젰", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputSize > 1) {
                    signup_btn_ok.setEnabled(true);
                    return;

                }
            }
        });

        signup_edit_password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable theWatchedText) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int start, int after, int count) {
                int inputSize = signup_edit_email.getText().length();
                if (inputSize <= 0) {
                    signup_btn_ok.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "鍮꾨?踰덊샇 ?낅젰", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputSize > 1) {
                    signup_btn_ok.setEnabled(true);
                    return;

                }
            }
        });

        signup_edit_name.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable theWatchedText) {

            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int start, int after, int count) {
                int inputSize = signup_edit_email.getText().length();
                if (inputSize <= 0) {
                    signup_btn_ok.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "?대쫫? ?낅젰", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputSize > 1) {
                    signup_btn_ok.setEnabled(true);
                    return;

                }
            }
        });

    }
}
