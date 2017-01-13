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
import de.hdodenhof.circleimageview.CircleImageView;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by WonYoung on 16. 7. 30..
 */
public class RoleActivity extends AppCompatActivity implements IRoleView {

	Button bt_editProfile;
	TextView tv_name;
	@BindView(R.id.iv_picture)
	CircleImageView iv_picture;
	@BindView(R.id.tv_editRoleInfo)
	TextView tv_editRoleInfo;
	@BindView(R.id.bt_editRoleInfo)
	Button bt_editRoleInfo;

	List<Role> allRoleList = null;
	RoleActivityAdapter adapter = null;

	FeatureCoverFlow vp_roleDetail = null;

	final int REQ_CODE_SELECT_IMAGE = 100;

	private IRolePresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role);
		ButterKnife.bind(this);

		presenter = new RolePresenter(RoleActivity.this, this);

		bt_editProfile = (Button) findViewById(R.id.bt_editProfile);
		tv_name = (TextView) findViewById(R.id.tv_name);
		vp_roleDetail = (FeatureCoverFlow) findViewById(R.id.vp_roleDetail);

		allRoleList = receiveRoles();
		adapter = new RoleActivityAdapter(this, presenter, allRoleList);

		vp_roleDetail.setAdapter(adapter);

		presenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));

		vp_roleDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intentSubActivity = new Intent(RoleActivity.this, EditRoleActivity.class);
				intentSubActivity.putExtra("Role", allRoleList.get(position));
				startActivity(intentSubActivity);
			}
		});

		getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

		Glide.with(this)
				.load(MyInfoDAO.getInstance().getPicUrl())
				.into(iv_picture);

		iv_picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
				intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

			}
		});

		Intent intent = getIntent();
		Role role = (Role) intent.getSerializableExtra("Role");
		if (role != null) {
			for (int i = 0; i < allRoleList.size(); i++) {
				if (allRoleList.get(i).getId() == role.getId()) {
					allRoleList.remove(i);
					allRoleList.add(i, role);
					adapter.notifyDataSetChanged();
				}

			}
		}


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
		role.setRoleContent("역할에 대한 설명을 적어 보세요");
		role.setRoleName("역할 정하기");
		role.setRolePrimary(0);
		role.setUser_id(1);
		roles.add(role);
//
//		//테스트용 for문 START
//		Role role2 = null;
//		role2 = new Role();
//		role2.setId(0);
//		role2.setRoleContent("역할에 대한 설명을 적어 보세요");
//		role2.setRoleName("역할 정하기");
//		role2.setRolePrimary(0);
//		role2.setUser_id(1);
//		roles.add(role2);
//
//		//테스트용 for문 START
//		Role role3 = null;
//		role3 = new Role();
//		role3.setId(0);
//		role3.setRoleContent("역할에 대한 설명을 적어 보세요");
//		role3.setRoleName("역할 정하기");
//		role3.setRolePrimary(0);
//		role3.setUser_id(1);
//		roles.add(role3);
//
//		//테스트용 for문 START
//		Role role4 = null;
//		role4 = new Role();
//		role4.setId(0);
//		role4.setRoleContent("역할에 대한 설명을 적어 보세요");
//		role4.setRoleName("역할 정하기");
//		role4.setRolePrimary(0);
//		role4.setUser_id(1);
//		roles.add(role4);
//
//		//테스트용 for문 START
//		Role role5 = null;
//		role5 = new Role();
//		role5.setId(0);
//		role5.setRoleContent("역할에 대한 설명을 적어 보세요");
//		role5.setRoleName("역할 정하기");
//		role5.setRolePrimary(0);
//		role5.setUser_id(1);
//		roles.add(role5);



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


	@Override
	public void setRoleContent(List<Role> roleList) {
		if (adapter != null) {
			adapter.setCommentList(roleList);
			adapter.notifyDataSetChanged();
		}
	}


}
