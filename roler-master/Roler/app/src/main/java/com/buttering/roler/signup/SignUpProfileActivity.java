package com.buttering.roler.signup;

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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.*;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.util.SharePrefUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import rx.Subscriber;

public class SignUpProfileActivity extends AppCompatActivity implements ISignUpProfileView {

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int RUQUEST_IMAGE_FROM_ALBUM = 5;

	@BindView(R.id.activity_signup_circleview)
	de.hdodenhof.circleimageview.CircleImageView circleImageView;

	@BindView(R.id.activity_signup_btn_next)
	Button activity_signup_btn_next;

	@BindView(R.id.activity_signup_profile_firstName)
	EditText activity_signup_profile_firstName;
	@BindView(R.id.activity_signup_profile_lastName)
	EditText activity_signup_profile_lastName;

	@BindView(R.id.activity_signup_profile_btn_male)
	Button activity_signup_profile_btn_male;
	@BindView(R.id.activity_signup_profile_btn_female)
	Button activity_signup_profile_btn_female;

	public String email;
	public String passwd;

	public ISignUpProfilePresenter presenter;

	public boolean isMaleCheck;
	public boolean isFemaleCheck;

	private Bitmap bp;
	private File imgfile;
	private ACProgressFlower dialog;
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_profile);
		ButterKnife.bind(this);
		checkThePemission();

		setToolbar();

		circleImageView.setOnClickListener(v -> takepicture());

		Intent intent = getIntent();
		email = intent.getExtras().getString("email");
		passwd = intent.getExtras().getString("pwd");

		setLayoutInit();

		presenter = new SignUpProfilePresenter(SignUpProfileActivity.this, this);

		setGender();

	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		textView.setTextColor(Color.BLACK);
		textView.setText("Sign Up Profile");
		setSupportActionBar(toolbar);

		imageView.setOnClickListener(view -> {
					this.finish();
					hideKeyboard();
				}
		);

	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}


	private void setGender() {
		activity_signup_profile_btn_male.setOnClickListener(view -> {
			if (isMaleCheck) {
				isMaleCheck = false;
				activity_signup_profile_btn_male.setBackgroundColor(Color.parseColor("#508b999b"));
			} else {
				isMaleCheck = true;
				activity_signup_profile_btn_male.setTextColor(Color.WHITE);
				activity_signup_profile_btn_male.setBackgroundColor(Color.BLACK);
				isFemaleCheck = false;
				activity_signup_profile_btn_female.setBackgroundColor(Color.parseColor("#508b999b"));

			}

		});
		activity_signup_profile_btn_female.setOnClickListener(view -> {

			if (isFemaleCheck) {
				isFemaleCheck = false;
				activity_signup_profile_btn_female.setBackgroundColor(Color.parseColor("#508b999b"));
			} else {
				isFemaleCheck = true;
				activity_signup_profile_btn_female.setTextColor(Color.WHITE);
				activity_signup_profile_btn_female.setBackgroundColor(Color.BLACK);
				isMaleCheck = false;
				activity_signup_profile_btn_male.setBackgroundColor(Color.parseColor("#508b999b"));
			}

		});


	}

	private void setLayoutInit() {
		activity_signup_btn_next.setOnClickListener(view -> {

			String firstName = activity_signup_profile_firstName.getText().toString();
			String lastName = activity_signup_profile_lastName.getText().toString();
			String fullName = firstName + lastName;
//			if (isValid(firstName, lastName)) {
//				presenter
//						.signUp(email, passwd, fullName)
//						.flatMap(new Func1<User , Observable<String>>() {
//							@Override
//							public Observable<String> call(User userInfo) {
//
//								MyInfoDAO.getInstance().setMyUserInfo(userInfo);
//								MyInfoDAO.getInstance().saveAccountInfo(email, passwd,fullName);
//
//								return presenter.uploadProfileImg(imgfile);
//							}
//						})
//						.flatMap(new Func1<String, Observable<Void>>() {
//							@Override
//							public Observable<Void> call(String profileImgUrl) {
//								return presenter.setProfileImg(profileImgUrl);
//							}
//						})
//						.subscribe(new Subscriber<Void>() {
//							@Override
//							public void onCompleted() {
//								hideLoadingBar();
//
//								SharePrefUtil.putSharedPreference("isLoggedIn", true);
//								Intent intent = new Intent(getApplicationContext(),PlanActivity.class);
//								startActivity(intent);
//								finish();
//
//							}
//
//							@Override
//							public void onError(Throwable e) {
//
//							}
//
//							@Override
//							public void onNext(Void aVoid) {
//
//							}
//						});
//
//			}

			if (isValid(firstName, lastName)) {
				presenter
						.signUp(email, passwd, fullName)
						.flatMap(user -> {
							MyInfoDAO.getInstance().setMyUserInfo(user);
							MyInfoDAO.getInstance().saveAccountInfo(email, passwd,fullName);
							return null;
						})
						.subscribe(new Subscriber<Object>() {
							@Override
							public void onCompleted() {
								hideLoadingBar();
								SharePrefUtil.putSharedPreference("isLoggedIn", true);
								Intent intent = new Intent(getApplicationContext(),PlanActivity.class);
								startActivity(intent);
								finish();
							}

							@Override
							public void onError(Throwable e) {

							}

							@Override
							public void onNext(Object o) {

							}
						});
			}

		});

	}

	private boolean isValid(String firstName, String lastName) {

		if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
			Toast.makeText(getApplicationContext(), "please enter user name",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (firstName.matches("[^가-힣A-Za-z ]") || lastName.matches("[^가-힣A-Za-z ]")) {
			Toast.makeText(getApplicationContext(), "User name can't have number including special characters",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();

		EditText txtTime = (EditText) findViewById(R.id.activity_sign_up_profile_edt_birthDate);
		txtTime.setOnFocusChangeListener((view, hasfocus) -> {
			if (hasfocus) {
				SignUpProfileTimeDialog dialog = SignUpProfileTimeDialog.newInstance(view);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				dialog.show(ft, "TimeDialog");

			}
		});
	}

	public void takepicture() {
		final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
						Intent intent = new Intent(Intent.ACTION_PICK);
						intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
						startActivityForResult(intent, RUQUEST_IMAGE_FROM_ALBUM);
					} else {
						Toast.makeText(getApplicationContext(), items[index], Toast.LENGTH_SHORT).show();
					}
				});


		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
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

					circleImageView.setImageBitmap(resizeBitmap(bp, 2048));

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
						circleImageView.setImageBitmap(resizeBitmap(rotatedBitmap, 2048));

					}
				} else {
					finish();
				}
			}
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


}
