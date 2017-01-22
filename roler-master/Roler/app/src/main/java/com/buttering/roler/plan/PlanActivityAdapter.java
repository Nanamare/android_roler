package com.buttering.roler.plan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.buttering.roler.R;
import com.buttering.roler.VO.Role;
import com.buttering.roler.role.RoleActivityAdapter;
import com.buttering.roler.util.SharePrefUtil;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.wdullaer.materialdatetimepicker.time.CircleView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nanamare on 16. 7. 31..
 */
public class PlanActivityAdapter extends BaseAdapter {

	private Context context;
	private List<Role> roles;
	private int progress;

	public PlanActivityAdapter(Context context, List<Role> roles) {
		this.context = context;
		this.roles = roles;
	}

	public PlanActivityAdapter(Context context, List<Role> roles, int progress) {
		this.context = context;
		this.roles = roles;
		this.progress = progress;
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
			convertView = inflater.inflate(R.layout.activity_plan_item, null);

			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.rl_roleItemTop.setBackgroundResource(bgColor[roles.get(position).getRolePrimary()]);
		viewHolder.tv_rolePrimaryPlan.setText(String.valueOf(roles.get(position).getRolePrimary()));
		viewHolder.tv_roleContentPlan.setText(roles.get(position).getRoleContent());
		viewHolder.tv_roleNamePlan.setText(roles.get(position).getRoleName());
		viewHolder.cp_planPercent.setProgress(progress);

		return convertView;
	}


	public void setRoleList(List<Role> roles) {
		this.roles.clear();
		this.roles = roles;
	}

	final int[] bgColor = {R.color.holo_green_dark, R.color.primary, R.color.colorAccent, R.color.body_background_green,
			R.color.suggestion_highlight_text, R.color.primary};


	public static class ViewHolder {
		@BindView(R.id.rl_roleItemTop)
		RelativeLayout rl_roleItemTop;
		@BindView(R.id.tv_rolePrimaryPlan)
		TextView tv_rolePrimaryPlan;
		@BindView(R.id.tv_roleNamePlan)
		TextView tv_roleNamePlan;
		@BindView(R.id.tv_roleContentPlan)
		TextView tv_roleContentPlan;
		@BindView(R.id.cp_planPercent)
		CircleProgress cp_planPercent;

		public ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}

	}


}
