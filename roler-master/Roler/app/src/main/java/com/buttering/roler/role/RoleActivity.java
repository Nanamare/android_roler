package com.buttering.roler.role;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.EditRoleActivity;
import com.buttering.roler.R;
import com.buttering.roler.VO.Role;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by WonYoung on 16. 7. 30..
 */
public class RoleActivity extends AppCompatActivity {

    Button bt_editProfile;
    Button bt_editRoleInfo;
    TextView tv_name;
    @BindView(R.id.iv_picture)
    CircleImageView iv_picture;

    List<Role> allRoleList = null;
    RoleActivityAdapter adapter = null;

    FeatureCoverFlow vp_roleDetail = null;

    final int REQ_CODE_SELECT_IMAGE = 100;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        ButterKnife.bind(this);

        bt_editProfile = (Button) findViewById(R.id.bt_editProfile);
        bt_editRoleInfo = (Button) findViewById(R.id.bt_editRoleInfo);
        tv_name = (TextView) findViewById(R.id.tv_name);
        vp_roleDetail = (FeatureCoverFlow) findViewById(R.id.vp_roleDetail);

        allRoleList = receiveRoles();
        adapter = new RoleActivityAdapter(this, allRoleList);
        vp_roleDetail.setAdapter(adapter);

        vp_roleDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentSubActivity = new Intent(RoleActivity.this, EditRoleActivity.class);
                startActivity(intentSubActivity);
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        iv_picture.setOnClickListener(new View.OnClickListener() {
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
    protected void onResume() {
        super.onResume();
        allRoleList = receiveRoles();
        adapter = new RoleActivityAdapter(this, allRoleList);
        vp_roleDetail.setAdapter(adapter);
    }

    public List<Role> receiveRoles() {
        Log.d("RoleActivity", "receiveRoles START");
        List<Role> roles = new ArrayList<>();

        //테스트용 for문 START
        Role role = null;
        role = new Role();
        role.setId(0);
        role.setRoleContent("사랑하는 이를 아끼는 사람이 된다. 상대방을 탓하지 않고 평가하지 않으며, 연인으로서 이해하고 공감한다." );
        role.setRoleName("사랑하는 사람");
        role.setRolePrimary(0);
        role.setUser_id(1);
        roles.add(role);

        role = new Role();
        role.setId(1);
        role.setRoleContent("경영학적인 도전을 게을리하지 않는다. 수익과 니즈, 시장을 항상 살피며, 생각하고, 공부한다,");
        role.setRoleName("경영학도로서의 나");
        role.setRolePrimary(1);
        role.setUser_id(1);
        roles.add(role);


        //테스트용 for문 END
        //서버에서 받아오거나 혹은 SharedPreference에 있는 정보를 넣을 것(협의 안됨)

        Log.d("RoleActivity", "role list 정보: " + roles);
        Log.d("RoleActivity", "receiveRoles END");
        return roles;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri?먯꽌 ?대?吏 ?대쫫???살뼱?⑤떎.
                    //String name_Str = getImageNameToUri(data.getData());

                    //?대?吏 ?곗씠?곕? 鍮꾪듃留듭쑝濡?諛쏆븘?⑤떎.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int resizeX = 312;
                    int resizeY = 312;
                    Bitmap resize = Bitmap.createScaledBitmap(image_bitmap, resizeX, resizeY, true);
                    //諛곗튂?대넃? ImageView??set
                    iv_picture.setImageBitmap(resize);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();


                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
