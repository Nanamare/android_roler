package com.buttering.roler.plan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buttering.roler.R;
import com.buttering.roler.VO.Todo;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SwipeLayout;

import java.util.List;

/**
 * Created by nanamare on 16. 7. 31..
 */
public class PlanActivityTodoAdapter extends RecyclerView.Adapter<PlanActivityTodoAdapter.ViewHolder> {

	private static Context context;
	private List<Todo> todos;
	private int itemLayout;
	private IPlanPresenter presenter;



	public PlanActivityTodoAdapter(PlanActivity context, List<Todo> todos, int itemLayout) {
		this.context = context;
		this.todos = todos;
		this.itemLayout = itemLayout;
	}


	@Override
	public PlanActivityTodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(PlanActivityTodoAdapter.ViewHolder holder, int position) {

		presenter = new PlanPresenter(context);

		holder.tv_no.setText(String.valueOf(position));
		holder.tv_list.setText(todos.get(position).getContent());

		holder.cb_todo.setOnClickListener(v -> {
			if (holder.cb_todo.isChecked()) {
				holder.tv_list.setTextColor(Color.LTGRAY);
				holder.tv_no.setTextColor(Color.LTGRAY);
				todos.get(position).setDone(true);

			} else {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setMessage("Todo list를 취소 하시겠습니까?").setCancelable(false)
						.setPositiveButton("확인", (dialog, which) -> {
							holder.tv_list.setTextColor(Color.BLACK);
							holder.tv_no.setTextColor(Color.BLACK);
							todos.get(position).setDone(false);

						})
						.setNegativeButton("취소", (dialog, which) -> {
							holder.cb_todo.setChecked(true);
							holder.tv_list.setTextColor(Color.LTGRAY);
							holder.tv_no.setTextColor(Color.LTGRAY);

						});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();

			}


			presenter.conveyProgress(todos);

		});

		holder.swipe_layout.setShowMode(SwipeLayout.ShowMode.LayDown);
		holder.swipe_layout.addDrag(SwipeLayout.DragEdge.Left, holder.ll_swipe_left);
		holder.swipe_layout.setRightSwipeEnabled(false);
		holder.swipe_layout.addSwipeListener(new SwipeLayout.SwipeListener() {


			@Override
			public void onStartOpen(SwipeLayout layout) {

			}

			@Override
			public void onOpen(SwipeLayout layout) {
				YoYo.with(Techniques.Tada)
						.duration(700)
						.playOn(layout.findViewById(R.id.ll_swipe_left));
			}

			@Override
			public void onStartClose(SwipeLayout layout) {

			}

			@Override
			public void onClose(SwipeLayout layout) {

			}

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

			}

			@Override
			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

			}
		});

		holder.ll_swipe_left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.deleteTodo(todos.get(position).getId());
				todos.remove(position);
				notifyDataSetChanged();
			}
		});


	}

	public void setTodoList(List<Todo> todoList) {
		this.todos.clear();
		this.todos = todoList;
	}


	public void setTodo(Todo todo) {
		this.todos.add(todo);
	}


	@Override
	public int getItemCount() {
		return todos.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public TextView tv_no;
		public TextView tv_list;
		public CheckBox cb_todo;
		public SwipeLayout swipe_layout;
		public LinearLayout ll_swipe_left;

		public ViewHolder(View itemView) {
			super(itemView);
			ll_swipe_left = (LinearLayout) itemView.findViewById(R.id.ll_swipe_left);
			swipe_layout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
			tv_no = (TextView) itemView.findViewById(R.id.tv_no);
			tv_list = (TextView) itemView.findViewById(R.id.tv_list);
			cb_todo = (CheckBox) itemView.findViewById(R.id.cb_todo);

		}
	}
}


