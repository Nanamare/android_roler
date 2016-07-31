package com.buttering.roler.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.buttering.roler.EditRoleActivity;
import com.buttering.roler.R;
import com.buttering.roler.RoleActivity;
import com.buttering.roler.VO.Role;

import java.util.List;


/**
 * Created by WonYoung on 16. 7. 30..
 */

public class RoleActivityAdapter extends BaseAdapter {

    private Context context;
    private List<Role> roles;

    public RoleActivityAdapter(Context context,  List<Role> roles){
        this.context = context;
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
            rowView = inflater.inflate(R.layout.activity_role_item, null);

            ViewHolder viewHolder = new ViewHolder();

            viewHolder.rolePrimary = (TextView) rowView.findViewById(R.id.tv_rolePrimary);
            viewHolder.roleName = (TextView) rowView.findViewById(R.id.tv_roleName);
            viewHolder.roleContent = (TextView) rowView.findViewById(R.id.tv_roleContent);

            rowView.setTag(viewHolder);

        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        holder.rolePrimary.setText(String.valueOf(roles.get(position).getRolePrimary()));
        holder.roleContent.setText(roles.get(position).getRoleContent());
        holder.roleName.setText(roles.get(position).getRoleName());

        return rowView;
    }


    public static class ViewHolder{
        public TextView rolePrimary;
        public TextView roleName;
        public TextView roleContent;
    }

}
