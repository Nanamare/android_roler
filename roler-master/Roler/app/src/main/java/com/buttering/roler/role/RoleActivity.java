package com.buttering.roler.role;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import de.hdodenhof.circleimageview.CircleImageView;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;
import rx.Subscriber;

import com.buttering.roler.signup.ISignUpProfilePresenter;
import com.buttering.roler.signup.SignUpProfilePresenter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by WonYoung on 16. 7. 30..
 */
public class RoleActivity extends AppCompatActivity implements IRoleView {

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int RUQUEST_IMAGE_FROM_ALBUM = 5;

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
	@BindView(R.id.activity_role_email)
	TextView activity_role_email;

	public List<Role> allRoleList = null;
	public RoleActivityAdapter adapter = null;
	public ISignUpProfilePresenter signUpPresenter;

	private ACProgressFlower dialog;
	private IRolePresenter presenter;
	private Bitmap bp;
	private File imgfile;
	private String mCurrentPhotoPath;


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_WRITE_STORAGE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					//reload my activity with permission granted or use the features what required the permission
				} else {
					Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
				}
			}
		}


	}

	private void checkThePemission() {
		if (Build.VERSION.SDK_INT > 22) {
			boolean hasPermission = (ContextCompat.checkSelfPermission(this,
					android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
			if (!hasPermission) {
				ActivityCompat.requestPermissions(this,
						new String[]{
								android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_WRITE_STORAGE);
			}
		}
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_role);
		ButterKnife.bind(this);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		presenter = new RolePresenter(this, this);
		signUpPresenter = new SignUpProfilePresenter();


		//mock data
		allRoleList = receiveRoles();

		//set a adapter
		adapter = new RoleActivityAdapter(this, allRoleList);
		vp_roleDetail.setAdapter(adapter);

		vp_roleDetail.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				AlertDialog.Builder alert = new AlertDialog.Builder(RoleActivity.this);
				alert.setMessage("역할을 삭제 하시겠습니까?").setCancelable(false)
						.setPositiveButton("확인", (dialog, which) -> {
							if (allRoleList.size() != 1) {
								presenter.deleteRole(((Role) adapter.getItem(vp_roleDetail.getScrollPosition())).getRole_id());
							} else {
								Toast.makeText(RoleActivity.this, "더 이상 삭제할수 없습니다."+"\n"+"수정해서 사용하세요", Toast.LENGTH_SHORT).show();
							}
						})
						.setNegativeButton("취소", (dialog, which) -> {

						});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();

				return false;
			}
		});


		//load RoleContent
		presenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));

		editRole();


		setProfileItem();

		setProfileImage();

		getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);

		editImage();

	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  // prefix
				".jpg",         // suffix
				storageDir      // directory
		);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void setProfileItem() {
		tv_name.setText(MyInfoDAO.getInstance().getNickName());
		activity_role_email.setText(MyInfoDAO.getInstance().getEmail());

	}


	private void editImage() {
		iv_picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

				AlertDialog.Builder builder = new AlertDialog.Builder(RoleActivity.this);
				builder.setTitle("Choose Option")

						.setItems(items, (dialog, index) -> {
							if (index == 0) {
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								if (intent.resolveActivity(getPackageManager()) != null) {
									// Create the File where the photo should go
									imgfile = null;
									try {
										imgfile = createImageFile();
									} catch (IOException ex) {
										// Error occurred while creating the File
										ex.printStackTrace();
									}
									// Continue only if the File was successfully created
									if (imgfile != null) {
										intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imgfile));
										startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
									}
								}
							} else if (index == 1) {
								imgfile = null;
								try {
									imgfile = createImageFile();
								} catch (IOException ex) {
									// Error occurred while creating the File
									ex.printStackTrace();
								}
								if (imgfile != null) {
									Intent intent = new Intent(Intent.ACTION_PICK);
									intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
									startActivityForResult(intent, RUQUEST_IMAGE_FROM_ALBUM);
								}
							} else {
								Toast.makeText(getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
							}
						});


				AlertDialog dialog = builder.create();
				dialog.show();


			}
		});
	}

	private void setProfileImage() {
//		Glide.with(this)
//				.load(MyInfoDAO.getInstance().getPicUrl())
//				.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
//				.into(iv_picture);

		Glide.with(this)
				.load(MyInfoDAO.getInstance().getPicUrl())
				.diskCacheStrategy(DiskCacheStrategy.RESULT)
				.override(300,300)
				.skipMemoryCache(true)
				.into(iv_picture);
	}

	private void editRole() {
		vp_roleDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intentSubActivity = new Intent(RoleActivity.this, EditRoleActivity.class);
				intentSubActivity.putExtra("Role", (Role) adapter.getItem(vp_roleDetail.getScrollPosition()));
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
	public void setRoleContent(List<Role> roleList) {
		if (adapter != null) {
			allRoleList = roleList;
			adapter = new RoleActivityAdapter(this, allRoleList);
			vp_roleDetail.setAdapter(adapter);
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
					} else if (i == (allRoleList.size() - 1)) {
						allRoleList.add(role);
					}
				}
				adapter = new RoleActivityAdapter(this, allRoleList);
				vp_roleDetail.setAdapter(adapter);
			}
		}
	}

	@Override
	public void refreshRoleContent() {
		presenter.getRoleContent(Integer.valueOf(MyInfoDAO.getInstance().getUserId()));

	}


	private Bitmap resizeBitmap(Bitmap src, int maxRes) {
		int iWidth = src.getWidth();      //비트맵이미지의 넓이
		int iHeight = src.getHeight();     //비트맵이미지의 높이
		int newWidth = iWidth;
		int newHeight = iHeight;
		float rate = 0.0f;

		//이미지의 가로 세로 비율에 맞게 조절
		if (iWidth > iHeight) {
			if (maxRes < iWidth) {
				rate = maxRes / (float) iWidth;
				newHeight = (int) (iHeight * rate);
				newWidth = maxRes;
			}
		} else {
			if (maxRes < iHeight) {
				rate = maxRes / (float) iHeight;
				newWidth = (int) (iWidth * rate);
				newHeight = maxRes;
			}
		}


		return Bitmap.createScaledBitmap(
				src, newWidth, newHeight, true);
	}

	private void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

		imgfile = new File(strFilePath);
		OutputStream out = null;

		try {
			imgfile.createNewFile();
			out = new FileOutputStream(imgfile);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case RUQUEST_IMAGE_FROM_ALBUM: {
				if (resultCode == Activity.RESULT_OK) {
					Uri imageUri = data.getData();
					try {
						bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
					} catch (IOException e) {
						e.printStackTrace();
					}

					SaveBitmapToFileCache(bp, imgfile.getAbsolutePath());
					iv_picture.setImageBitmap(resizeBitmap(bp, 2048));

					break;
				} else {
					finish();
				}
			}
			case REQUEST_IMAGE_CAPTURE: {
				if (resultCode == Activity.RESULT_OK) {

					try {
						bp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
					} catch (IOException e) {
						e.printStackTrace();
					}

					Matrix matrix = new Matrix();
					ExifInterface ei = null;
					try {
						ei = new ExifInterface(imgfile.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}

					int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);

					switch (orientation) {
						case 1:
							break;
						case 6:
							matrix.postRotate(90);
							break;
						case 8:
							matrix.postRotate(-90);
							break;
						default:
							matrix.postRotate(90);
							break;
					}

					if (bp != null) {
						Bitmap rotatedBitmap = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
						iv_picture.setImageBitmap(resizeBitmap(rotatedBitmap, 2048));
						SaveBitmapToFileCache(bp, imgfile.getAbsolutePath());

					}
				} else {
					finish();
				}
			}
		}

		if (imgfile != null) {
			signUpPresenter.uploadProfileImg(imgfile)
					.subscribe(new Subscriber<String>() {
						@Override
						public void onCompleted() {
							unsubscribe();
						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
						}

						@Override
						public void onNext(String s) {
							onCompleted();
						}
					});
		}

	}


}
