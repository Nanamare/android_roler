package com.buttering.roler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ModifyActivity extends AppCompatActivity {
    Button pw_check_btn;
    EditText pw_btn;
    EditText pw_btn2;
    ImageView profile_iv;
    final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        pw_check_btn = (Button)findViewById(R.id.pw_check_btn);
        pw_btn = (EditText)findViewById(R.id.pw_btn);
        pw_btn2 = (EditText)findViewById(R.id.pw_btn2);
        profile_iv = (ImageView)findViewById(R.id.profile_iv);



        pw_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pw, pw2;
                pw = pw_btn.getText().toString();
                pw2 = pw_btn2.getText().toString();
                if(pw.equals(pw2)){
                    Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profile_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int resizeX = 312;
                    int resizeY = 312;
                    Bitmap resize = Bitmap.createScaledBitmap(image_bitmap, resizeX, resizeY, true);
                    //배치해놓은 ImageView에 set
                    profile_iv.setImageBitmap(resize);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
