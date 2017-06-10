package com.buttering.roler.plan;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by nanamare on 16. 7. 31..
 */
public class PlanActivityTodoAdapter extends RecyclerView.Adapter<PlanActivityTodoAdapter.ViewHolder> {

	private static Context context;
	private List<Todo> todos;
	private int itemLayout;
	private IPlanPresenter presenter;
	private IPlanView view;
	private boolean isDialogMode;


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
		holder.cb_todo.setChecked(todos.get(position).getDone());
		holder.tv_list.setText(todos.get(position).getContent());
		if (todos.get(position).getDone()) {

			holder.tv_list.setPaintFlags(holder.tv_list.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_no.setPaintFlags(holder.tv_no.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_list.setTextColor(Color.LTGRAY);
			holder.tv_no.setTextColor(Color.LTGRAY);

		} else {

			holder.tv_list.setTextColor(Color.DKGRAY);
			holder.tv_no.setTextColor(Color.DKGRAY);
		}


		holder.cb_todo.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SmoothCheckBox smoothCheckBox, boolean b) {

				if (holder.cb_todo.isChecked()) {

					if ((holder.tv_list.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) != Paint.STRIKE_THRU_TEXT_FLAG)
						holder.tv_list.setPaintFlags(holder.tv_list.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
					if ((holder.tv_no.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) != Paint.STRIKE_THRU_TEXT_FLAG)
						holder.tv_no.setPaintFlags(holder.tv_no.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

					todos.get(position).setDone(true);
					presenter.setDone(todos.get(position).getId(), todos.get(position).getDone());
				} else {

					if(!isDialogMode){

						AlertDialog.Builder alert = new AlertDialog.Builder(context);
						alert.setMessage("Todo list를 취소 하시겠습니까?").setCancelable(false)
								.setPositiveButton("확인", (dialog, which) -> {
									holder.tv_list.setTextColor(Color.DKGRAY);
									holder.tv_no.setTextColor(Color.DKGRAY);
									todos.get(position).setDone(false);
									presenter.setDone(todos.get(position).getId(), todos.get(position).getDone());

								})
								.setNegativeButton("취소", (dialog, which) -> {
									holder.cb_todo.setChecked(true);
									holder.tv_list.setTextColor(Color.LTGRAY);
									holder.tv_no.setTextColor(Color.LTGRAY);
								});

						AlertDialog alertDialog = alert.create();
						alertDialog.show();

						isDialogMode = false;

					}

				}
			}
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
				//animation
//				YoYo.with(Techniques.Tada)
//						.duration(700)
//						.playOn(layout.findViewById(R.id.ll_swipe_left));
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
				isDialogMode = true;
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
		public SmoothCheckBox cb_todo;
		public SwipeLayout swipe_layout;
		public LinearLayout ll_swipe_left;

		public ViewHolder(View itemView) {
			super(itemView);
			ll_swipe_left = (LinearLayout) itemView.findViewById(R.id.ll_swipe_left);
			swipe_layout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
			tv_no = (TextView) itemView.findViewById(R.id.tv_no);
			tv_list = (TextView) itemView.findViewById(R.id.tv_list);
			cb_todo = (SmoothCheckBox) itemView.findViewById(R.id.cb_todo);

		}
	}
}


