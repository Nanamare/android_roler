package com.buttering.roler.role;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttering.roler.R;
import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.VO.Role;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by WonYoung on 16. 7. 31..
 */
public class EditRoleActivity extends AppCompatActivity implements IRoleView {

	@BindView(R.id.activity_edit_role_btn)
	Button done_role_btn;
	@BindView(R.id.et_roleName)
	EditText et_roleName;
	@BindView(R.id.et_roleContent)
	EditText et_roleContent;
	@BindView(R.id.picker_ui_view)
	PickerUI picker_ui_view;
	@BindView(R.id.et_priority)
	Button et_priority;

	private List<String> score;
	private int currentPosition = -1;
	private int user_id;
	private int id;
	private int priority;
	private int role_id;
	private IRolePresenter presenter;
	private boolean isAdd;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_role);
		ButterKnife.bind(this);
		setToolbar();
		presenter = new RolePresenter(this, this);

		Intent intent = getIntent();
		Role role = (Role) intent.getSerializableExtra("Role");
		if (role == null) {
			//새로 추가 더하기
			isAdd = true;
			et_roleContent.setText("");
			et_roleName.setText("");
			user_id = Integer.valueOf(MyInfoDAO.getInstance().getUserId());

		} else {
			// 기존의 아이템을 가지고 와서 보여주기
			user_id = role.getUser_id();
			priority = role.getRolePrimary();
			role_id = role.getRole_id();
			id = role.getId();
			et_roleContent.setText(role.getRoleContent());
			et_roleName.setText(role.getRoleName());

		}
		score = new ArrayList<>();
		score.add("1순위");
		score.add("2순위");
		score.add("3순위");
		score.add("4순위");
		score.add("5순위");

		setPriority();
		generatePicker();
		presenter.check_blank(et_priority, et_roleName, et_roleContent);
		putData();


	}

	private void putData() {
		done_role_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkRolePrimary(et_priority.getText().toString()) && checkText()) {
					Role role = new Role();
					role.setId(id);
					//아직 필요 없는 코드
					role.setRole_id(role_id);
					role.setRoleContent(et_roleContent.getText().toString());
					role.setRoleName(et_roleName.getText().toString());
					role.setRolePrimary(Integer.parseInt("" + et_priority.getText().charAt(0)));
					role.setUser_id(user_id);
					Intent intent = new Intent(getApplicationContext(), RoleActivity.class);
					if (isAdd) {
						presenter.addRole(Integer.parseInt("" + et_priority.getText().charAt(0))
								, et_roleName.getText().toString()
								, et_roleContent.getText().toString(), user_id);
						isAdd = false;
					} else {
						presenter.editRole(Integer.parseInt("" + et_priority.getText().charAt(0))
								, et_roleName.getText().toString()
								, et_roleContent.getText().toString(), role_id);
					}
					intent.putExtra("Role", role);
					startActivity(intent);
				}
			}
		});
	}

	private boolean checkText() {
		String content = et_roleContent.getText().toString();
		String roleName = et_roleName.getText().toString();
		if (!content.isEmpty() && !roleName.isEmpty()) {
			return true;
		} else {
			Toast.makeText(this, "텍스트를 입력해 주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private boolean checkRolePrimary(String primary) {
		if (!primary.isEmpty()) {
			return true;
		} else {
			Toast.makeText(this, "우선순위를 선택해주세요.", Toast.LENGTH_SHORT).show();
			return false;
		}

	}


	private void setPriority() {
		picker_ui_view.setItems(this, score);
		picker_ui_view.setColorTextCenter(R.color.primaryColor);
		picker_ui_view.setColorTextNoCenter(R.color.primary);
		picker_ui_view.setBackgroundColorPanel(R.color.mdtp_accent_color);
		picker_ui_view.setLinesColor(R.color.primaryColor);
		picker_ui_view.setItemsClickables(false);
		picker_ui_view.setAutoDismiss(false);

		picker_ui_view.setOnClickItemPickerUIListener(
				new PickerUI.PickerUIItemClickListener() {

					@Override
					public void onItemClickPickerUI(int which, int position, String valueResult) {
						currentPosition = position;
						et_priority.setText(valueResult);

					}
				});
	}

	private void generatePicker() {
		et_priority.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				PickerUISettings pickerUISettings = new PickerUISettings.Builder()
						.withItems(score)
						.withBackgroundColor(R.color.white)
						.withAutoDismiss(true)
						.withItemsClickables(false)
						.withUseBlur(false)
						.build();

				picker_ui_view.setSettings(pickerUISettings);

				if (currentPosition == -1) {
					picker_ui_view.slide();
				} else {
					picker_ui_view.slide(currentPosition);
				}

			}
		});
	}


	private void setToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.custom_toolBar);
		TextView textView = (TextView) findViewById(R.id.toolbar_title);
		ImageView imageView = (ImageView) findViewById(R.id.toolBar_image);
		imageView.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
		textView.setTextColor(Color.BLACK);
		textView.setText("역할 정보 수정");
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

	@Override
	public void setRoleContent(List<Role> roleList) {

	}

	@Override
	public void showLoadingBar() {

	}

	@Override
	public void hideLoadingBar() {

	}

	@Override
	public void addRole() {

	}

	@Override
	public void refreshRoleContent() {

	}
}
