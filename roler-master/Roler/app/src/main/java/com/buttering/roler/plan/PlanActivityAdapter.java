package com.buttering.roler.plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.buttering.roler.R;
import com.buttering.roler.VO.Role;

import java.util.List;

/**
 * Created by WonYoung on 16. 7. 31..
 */
public class PlanActivityAdapter extends BaseAdapter {

	private Context context;
	private List<Role> roles;

	public PlanActivityAdapter(Context context, List<Role> roles) {
		this.context = context;
		this.roles = roles;
	}


	public void setCommentList(List<Role> roles) {
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

		View rowView = convertView;

		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.activity_plan_item, null);

			ViewHolder viewHolder = new ViewHolder();

			viewHolder.tv_rolePrimaryPlan = (TextView) rowView.findViewById(R.id.tv_rolePrimaryPlan);
			viewHolder.tv_roleNamePlan = (TextView) rowView.findViewById(R.id.tv_roleNamePlan);
			viewHolder.tv_roleContentPlan = (TextView) rowView.findViewById(R.id.tv_roleContentPlan);

			rowView.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) rowView.getTag();

		holder.tv_rolePrimaryPlan.setText(String.valueOf(roles.get(position).getRolePrimary()));
		holder.tv_roleContentPlan.setText(roles.get(position).getRoleContent());
		holder.tv_roleNamePlan.setText(roles.get(position).getRoleName());

		return rowView;
	}


	public static class ViewHolder {
		TextView tv_rolePrimaryPlan = null;
		TextView tv_roleNamePlan = null;
		TextView tv_roleContentPlan = null;
	}


}
