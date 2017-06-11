package com.buttering.roler.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

import com.buttering.roler.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by kinamare on 2017-06-12.
 */

public class TodoDialog extends Dialog {

	@BindView(R.id.dialog_todo_edit) AppCompatEditText todoEditText;
	@BindView(R.id.dialog_todo_cancel_btn) AppCompatTextView cancleBtn;
	@BindView(R.id.dialog_todo_done_btn) AppCompatTextView doneBtn;

	private Context context;
	private TodoListener todoListener;

	public TodoDialog(@NonNull Context context) {
		super(context);

		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	public TodoDialog(@NonNull Context context, @StyleRes int themeResId) {
		super(context, themeResId);
	}

	protected TodoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_todo);

		ButterKnife.bind(this);

		checkBlanck();

	}

	private void checkBlanck() {

		Observable<CharSequence> observable1 = RxTextView.textChanges(todoEditText);
		observable1.map(charSequence -> charSequence.length() > 0).subscribe(new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				if(aBoolean){
					doneBtn.setTextColor(context.getResources().getColor(R.color.point_5FA9D0));
				} else {
					doneBtn.setTextColor(context.getResources().getColor(R.color.dialog_todo_font_color));
				}

			}
		});
	}

	@OnClick(R.id.dialog_todo_done_btn)
	public void doneOnClick() {
		String contents = todoEditText.getText().toString();
		if (TextUtils.isEmpty(contents)) {
			Toast.makeText(context, context.getString(R.string.dialog_todo_content), Toast.LENGTH_SHORT).show();
			return;
		} else {
			todoListener.addTodoList(contents);
			dismiss();
		}

	}

	@OnClick(R.id.dialog_todo_cancel_btn)
	public void cancleOnClick(){
		if (todoListener != null) {
			todoListener.onCancel();
		}
		cancel();
	}

	public void setTodoListener(TodoListener todoListener){
		this.todoListener = todoListener;
	}

	public interface TodoListener {
		void addTodoList(String contents);
		void onCancel();
	}

}
