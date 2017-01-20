package com.buttering.roler.role;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.bumptech.glide.Glide;
import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by WonYoung on 16. 7. 30..
 */
public class RoleActivity extends AppCompatActivity implements IRoleView {


    @BindView(R.id.iv_picture)
    CircleImageView iv_picture;
    @BindView(R.id.tv_editRoleInfo)
    TextView tv_editRoleInfo;
    @BindView(R.id.bt_editRoleInfo)
    Button bt_editRoleInfo;
    @BindView(R.id.bt_editProfile)
    Button bt_editProfile;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.vp_roleDetail)
    FeatureCoverFlow vp_roleDetail;

    List<Role> allRoleList = null;
    RoleActivityAdapter adapter = null;


    final int REQ_CODE_SELECT_IMAGE = 100;

    private ACProgressFlower dialog;
    private IRolePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
        ButterKnife.bind(this);

        presenter = new RolePresenter(this);

        //mock data
        allRoleList = receiveRoles();

        //set a adapter
        adapter = new RoleActivityAdapter(this, allRoleList);
        vp_roleDetail.setAdapter(adapter);

        //load RoleContent
        presenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));


        editRole();

        setProfileImage();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

        editImage();

    }


    private void editImage() {
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

    private void setProfileImage() {
        Glide.with(this)
                .load(MyInfoDAO.getInstance().getPicUrl())
                .into(iv_picture);
    }

    private void editRole() {
        vp_roleDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentSubActivity = new Intent(RoleActivity.this, EditRoleActivity.class);
                intentSubActivity.putExtra("Role", (Role) adapter.getItem(position));
                startActivity(intentSubActivity);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @OnClick({R.id.tv_editRoleInfo, R.id.bt_editRoleInfo})
    public void Onclick() {
        Intent intent = new Intent(RoleActivity.this, EditRoleActivity.class);
        startActivity(intent);
    }

    public List<Role> receiveRoles() {
        Log.d("RoleActivity", "receiveRoles START");
        List<Role> roles = new ArrayList<>();

        //테스트용 for문 START
        Role role = null;
        role = new Role();
        role.setId(0);
        role.setRoleContent("역할에 대한 설명을 적어 보세요!!");
        role.setRoleName("안녕하세요!");
        role.setRolePrimary(0);
        role.setUser_id(1);
        roles.add(role);


        return roles;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_SHORT).show();

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int resizeX = 312;
                    int resizeY = 312;
                    Bitmap resize = Bitmap.createScaledBitmap(image_bitmap, resizeX, resizeY, true);

                    iv_picture.setImageBitmap(resize);


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


    @Override
    public void setRoleContent(List<Role> roleList) {
        if (adapter != null) {
            allRoleList = roleList;
            adapter = new RoleActivityAdapter(this, allRoleList);
            vp_roleDetail.setAdapter(adapter);
//            adapter.setCommentList(roleList);
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoadingBar() {
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
    }

    @Override
    public void hideLoadingBar() {
        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void addRole() {
        Intent intent = getIntent();
        if (intent != null) {
            Role role = (Role) intent.getSerializableExtra("Role");
            if (role != null) {
                for (int i = 0; i < allRoleList.size(); i++) {
                    //기존의것 수정
                    if (allRoleList.get(i).getRole_id() == role.getRole_id()) {
                        allRoleList.remove(i);
                        allRoleList.add(i, role);
                        adapter = new RoleActivityAdapter(this, allRoleList);
                        vp_roleDetail.setAdapter(adapter);
                    } else {
                        //새로추가
                        if (i == allRoleList.size() - 1) {
                            allRoleList.add(role);
                            adapter = new RoleActivityAdapter(this, allRoleList);
                            vp_roleDetail.setAdapter(adapter);
                        }
                    }
                }
            }
        }
    }


}
