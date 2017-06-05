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
import com.buttering.roler.VO.Schedule;
import com.buttering.roler.login.ILoginPresenter;
import com.buttering.roler.login.ILoginView;
import com.buttering.roler.login.LogInActivity;
import com.buttering.roler.login.LoginPresenter;
import com.buttering.roler.plan.PlanActivity;
import com.buttering.roler.util.SharePrefUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpProfileActivity extends AppCompatActivity implements ISignUpProfileView, ILoginView {

	private static final int REQUEST_WRITE_STORAGE = 112;
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int RUQUEST_IMAGE_FROM_ALBUM = 5;

	public static final String putExtraEmail = "ROLER_USER_EMAIL";
	public static final String putExtraPwd = "ROLER_USER_PWD";

	@BindView(R.id.activity_signup_circleview) de.hdodenhof.circleimageview.CircleImageView circleImageView;
	@BindView(R.id.activity_signup_btn_next) Button activity_signup_btn_next;
	@BindView(R.id.activity_signup_profile_firstName) EditText activity_signup_profile_firstName;
	@BindView(R.id.activity_signup_profile_lastName) EditText activity_signup_profile_lastName;
	@BindView(R.id.activity_signup_profile_btn_male) Button activity_signup_profile_btn_male;
	@BindView(R.id.activity_signup_profile_btn_female) Button activity_signup_profile_btn_female;

	public String email;
	public String passwd;

	public ISignUpProfilePresenter presenter;
	public ILoginPresenter loginPresenter;

	public boolean isMaleCheck;
	public boolean isFemaleCheck;

	private Bitmap bp;
	private File imgfile;
	private SweetAlertDialog materialDialog;
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
		email = intent.getExtras().getString(putExtraEmail);
		passwd = intent.getExtras().getString(putExtraPwd);

		presenter = new SignUpProfilePresenter(this);
		loginPresenter = new LoginPresenter(this,this);

		setGender();

	}

	@OnClick(R.id.activity_signup_btn_next)
	public void signUpOnClick(){

		String firstName = activity_signup_profile_firstName.getText().toString();
		String lastName = activity_signup_profile_lastName.getText().toString();
		String fullName = firstName + lastName;

		if (isValid(firstName, lastName)) {

			showLoadingBar();

			presenter
					.signUp(email, passwd, fullName)
					.subscribe(new Subscriber<Object>() {
						@Override
						public void onCompleted() {

						}

						@Override
						public void onError(Throwable e) {

						}

						@Override
						public void onNext(Object o) {
							sendToServerUserImage();
							sendToServerLogin();
						}

						private void sendToServerLogin() {

							loginPresenter.signIn(email,passwd)
									.observeOn(AndroidSchedulers.mainThread())
									.subscribe(new Subscriber<Void>() {
										@Override
										public void onCompleted() {
										}

										@Override
										public void onError(Throwable e) {

										}

										@Override
										public void onNext(Void aVoid) {
											hideLoadingBar();
											SharePrefUtil.putSharedPreference("isLoggedIn", true);
											goToPlanActivity();
										}
									});

						}

						private void sendToServerUserImage() {

							if (imgfile != null) {
								presenter.uploadProfileImg(imgfile)
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
					});
		}

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


	private void goToPlanActivity() {
		Toast.makeText(SignUpProfileActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(getApplicationContext(), PlanActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
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
						SaveBitmapToFileCache(bp, imgfile.getAbsolutePath());

					}
				} else {
					finish();
				}
			}
		}

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

		try
		{
			imgfile.createNewFile();
			out = new FileOutputStream(imgfile);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void showLoadingBar() {
		materialDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
		materialDialog.getProgressHelper().setBarColor(this.getResources().getColor(R.color.dialog_color));
		materialDialog.setTitleText(getString(R.string.loading_dialog_title));
		materialDialog.setCancelable(false);
		materialDialog.show();
	}

	@Override
	public void hideLoadingBar() {
		if (materialDialog != null)
			materialDialog.dismiss();
	}


}
