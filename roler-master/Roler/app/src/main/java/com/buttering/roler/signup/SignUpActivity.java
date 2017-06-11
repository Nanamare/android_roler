package com.buttering.roler.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;
import com.buttering.roler.web.TermServiceActivity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;

/**
 * Created by kinamare on 2016-12-17.
 */

public class SignUpActivity extends AppCompatActivity {


	@BindView(R.id.activity_signup_ll_policy) LinearLayout activity_signup_ll_policy;
	@BindView(R.id.activity_signup_btn) Button activity_signup_btn;
	@BindView(R.id.activity_signup_edt_id) EditText activity_signup_edt_id;
	@BindView(R.id.activity_signup_edt_pwd) EditText activity_signup_edt_pwd;
	@BindView(R.id.activity_signup_tv_show) TextView activity_signup_tv_show;


	private ISignUpPresenter presenter;
	private boolean isShow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		ButterKnife.bind(this);

		setToolbar();

		setServiceText();

		presenter = new SignUpPresenter(SignUpActivity.this);

		presenter.check_blank(activity_signup_edt_id, activity_signup_edt_pwd);

		checkViewHide_tv();


	}

	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		textView.setTextColor(Color.BLACK);
		textView.setText(getString(R.string.activity_sign_up_toolbar_title));
		setSupportActionBar(toolbar);

		imageView.setOnClickListener(view -> {
					this.finish();
					hideKeyboard();
				}
		);
	}

	private void checkViewHide_tv() {

		activity_signup_tv_show.setOnClickListener(view -> {
			if (isShow) {
				// TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD == 0x00000081
				activity_signup_edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
				activity_signup_tv_show.setText(getString(R.string.show_pwd));
				isShow = false;
			} else {
				activity_signup_edt_pwd.setInputType(InputType.TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				activity_signup_tv_show.setText(getString(R.string.hide_pwd));
				isShow = true;
			}

		});
	}

	private void setServiceText() {

		TextView policyTv[] = new TextView[4];

		policyTv[0] = new TextView(this);
		policyTv[0].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		policyTv[0].setTextSize(10);
		policyTv[0].setText(getString(R.string.notice_service_0));
		policyTv[1] = new TextView(this);
		policyTv[1].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		policyTv[1].setText(getString(R.string.notice_service_1));
		policyTv[1].setTextSize(10);
		policyTv[2] = new TextView(this);
		policyTv[2].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		policyTv[2].setText(getString(R.string.notice_service_2));
		policyTv[2].setTextSize(10);
		policyTv[3] = new TextView(this);
		policyTv[3].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		policyTv[3].setText(getString(R.string.notice_service_3));
		policyTv[3].setTextSize(10);

		dynamicView(activity_signup_ll_policy, policyTv, this);

		//Privacy Policy 여기 나중에 수정이 필요하다.
		policyTv[1].setOnClickListener(view -> {
			Intent pdfIntent = new Intent(this, TermServiceActivity.class);
			startActivity(pdfIntent);

		});
		//Terms of Service
		policyTv[3].setOnClickListener(view -> {
			Intent pdfIntent = new Intent(this, TermServiceActivity.class);
			startActivity(pdfIntent);
		});
	}

	@OnClick(R.id.activity_signup_btn)
	public void signUpOnClick(){

		String email = activity_signup_edt_id.getText().toString().trim();
		String passwd = activity_signup_edt_pwd.getText().toString();

		if (isValid(email, passwd)) {
			presenter.checkDuplicateEmail(email)
					.subscribe(new Subscriber<Void>() {
						@Override
						public void onCompleted() {
						}

						@Override
						public void onError(Throwable e) {
							e.printStackTrace();
							Toast.makeText(SignUpActivity.this, getString(R.string.already_to_join_email), Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onNext(Void aVoid) {
							presenter.registerUser(email, passwd);
						}
					});
		}

	}


	private boolean isValid(String email, String passwd) {

		if (email == null || email.isEmpty()) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_empty_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (email.length() > 60) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_too_long_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isEmailValid(email)) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_invalid_email),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!isPwdValid(passwd)) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_invalid_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (passwd == null || passwd.isEmpty()) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_empty_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (passwd.length() < 8) {
			Toast.makeText(getApplicationContext(), getString(R.string.join_too_short_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}


		return true;
	}

	private boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	private boolean isPwdValid(String pwd) {
		boolean isPwd = false;

		String expression = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(pwd);
		if (matcher.matches()) {
			isPwd = true;
		}
		return isPwd;
	}


	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private void dynamicView(LinearLayout ll, View[] views, Context mContext) {

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		ll.removeAllViews();
		int maxWidth = display.getWidth() - 50;
		LinearLayout.LayoutParams params;
		LinearLayout newLL = new LinearLayout(mContext);
		newLL.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		newLL.setGravity(Gravity.LEFT);
		newLL.setOrientation(LinearLayout.HORIZONTAL);

		int widthSoFar = 0;
		for (int i = 0; i < views.length; i++) {
			LinearLayout LL = new LinearLayout(mContext);
			LL.setOrientation(LinearLayout.HORIZONTAL);
			LL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
			LL.setLayoutParams(new ListView.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			views[i].measure(0, 0);
			params = new LinearLayout.LayoutParams(views[i].getMeasuredWidth(),
					ViewGroup.LayoutParams.WRAP_CONTENT);
			LL.addView(views[i], params);
			LL.measure(0, 0);
			widthSoFar += views[i].getMeasuredWidth();// YOU MAY NEED TO ADD THE MARGINS
			if (widthSoFar >= maxWidth) {
				ll.addView(newLL);
				newLL = new LinearLayout(mContext);
				newLL.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				newLL.setOrientation(LinearLayout.HORIZONTAL);
				newLL.setGravity(Gravity.LEFT);
				params = new LinearLayout.LayoutParams(LL
						.getMeasuredWidth(), LL.getMeasuredHeight());
				newLL.addView(LL, params);
				widthSoFar = LL.getMeasuredWidth();
			} else {
				newLL.addView(LL);
			}
		}
		ll.addView(newLL);
	}

	//ListType migration
	private void dynamicView(LinearLayout ll, List<View> views, Context mContext) {

		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		ll.removeAllViews();
		int maxWidth = display.getWidth() - 50;
		LinearLayout.LayoutParams params;
		LinearLayout newLL = new LinearLayout(mContext);
		newLL.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		newLL.setGravity(Gravity.LEFT);
		newLL.setOrientation(LinearLayout.HORIZONTAL);

		int widthSoFar = 0;
		for (int i = 0; i < views.size(); i++) {
			LinearLayout LL = new LinearLayout(mContext);
			LL.setOrientation(LinearLayout.HORIZONTAL);
			LL.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
			LL.setLayoutParams(new ListView.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			views.get(i).measure(0, 0);
			params = new LinearLayout.LayoutParams(views.get(i).getMeasuredWidth(),
					ViewGroup.LayoutParams.WRAP_CONTENT);
			LL.addView(views.get(i), params);
			LL.measure(0, 0);
			widthSoFar += views.get(i).getMeasuredWidth();// YOU MAY NEED TO ADD THE MARGINS
			if (widthSoFar >= maxWidth) {
				ll.addView(newLL);
				newLL = new LinearLayout(mContext);
				newLL.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				newLL.setOrientation(LinearLayout.HORIZONTAL);
				newLL.setGravity(Gravity.LEFT);
				params = new LinearLayout.LayoutParams(LL
						.getMeasuredWidth(), LL.getMeasuredHeight());
				newLL.addView(LL, params);
				widthSoFar = LL.getMeasuredWidth();
			} else {
				newLL.addView(LL);
			}
		}
		ll.addView(newLL);
	}


}
