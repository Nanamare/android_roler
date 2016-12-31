package com.buttering.roler.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttering.roler.R;
import com.buttering.roler.VO.Todo;

import java.util.List;

/**
 * Created by WonYoung on 16. 7. 31..
 */
public class PlanActivityTodoAdapter extends RecyclerView.Adapter<PlanActivityTodoAdapter.ViewHolder> {

    private static Context context;
    private List<Todo> todos;
    private int itemLayout;

    public PlanActivityTodoAdapter(PlanActivity context, List<Todo> todos, int itemLayout){
        this.context = context;
        this.todos = todos;
        this.itemLayout = itemLayout;
    }

    @Override
    public PlanActivityTodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PlanActivityTodoAdapter.ViewHolder holder, int position) {

        holder.tv_no.setText(String.valueOf(position));
        holder.tv_list.setText(todos.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_no;
        public TextView tv_list;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
            tv_list = (TextView) itemView.findViewById(R.id.tv_list);
        }
    }
}


