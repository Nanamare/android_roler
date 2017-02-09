package com.buttering.roler.role;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buttering.roler.R;
import com.buttering.roler.VO.Role;
import com.github.lzyzsd.circleprogress.CircleProgress;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by WonYoung on 16. 7. 30..
 */

public class RoleActivityAdapter extends BaseAdapter {

	private Context context;
	private List<Role> roles;
	private int primary = 0;

//	public RoleActivityAdapter(Context context, List<Role> roles) {
//		this.context = context;
//		this.roles = roles;
//	}

	public RoleActivityAdapter(Context context, List<Role> roles) {
		this.context = context;
		this.roles = new ArrayList<>();
		this.roles = roles;
	}

	@Override
	public int getCount() {
		return roles.size();
	}

	@Override
	public Object getItem(int position) {
		return roles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.activity_role_item, null);

			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}


		viewHolder.role_title.setText("우선순위 " + String.valueOf(roles.get(position).getRolePrimary()));
		viewHolder.rl_roleItemTop.setBackgroundResource(bgColor[roles.get(position).getRolePrimary()]);
		viewHolder.role_subTitle.setText("역할을 하나하나 실천해 나가세요!");
		viewHolder.rolePrimary.setText(String.valueOf(roles.get(position).getRole_id()));
		viewHolder.roleContent.setText(roles.get(position).getRoleContent());
		viewHolder.roleName.setText(roles.get(position).getRoleName());

		return convertView;
	}

	public void setCommentList(List<Role> roles) {
		this.roles.clear();
		this.roles = roles;
	}

	//색 추가로 더 넣어줘야 함
	final int[] bgColor = {R.color.holo_green_dark, R.color.primary, R.color.colorAccent, R.color.body_background_green,
			R.color.suggestion_highlight_text, R.color.primary};

	public static class ViewHolder {
		@BindView(R.id.tv_rolePrimary)
		TextView rolePrimary;
		@BindView(R.id.tv_roleName)
		TextView roleName;
		@BindView(R.id.tv_roleContent)
		TextView roleContent;
		@BindView(R.id.rl_roleItemTop)
		RelativeLayout rl_roleItemTop;
		@BindView(R.id.cp_rolePercent)
		CircleProgress cp_rolePercent;
		@BindView(R.id.activity_role_title)
		TextView role_title;
		@BindView(R.id.activity_role_subTitle)
		TextView role_subTitle;
		@BindView(R.id.role_card)
		RelativeLayout role_card;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}

}
