package com.vocketlist.android.network.executor;

import com.vocketlist.android.roboguice.log.Ln;

import java.util.Comparator;

/**
 * 
 * @author Lim SeungTaek
 *
 */
class PriorityComparator implements Comparator<Runnable> {
	private static final String TAG = PriorityComparator.class.getSimpleName();

	@Override
	public int compare(Runnable lhs, Runnable rhs) {
		if (lhs instanceof PriorityScheduler.ExecutorSchedulerWorker
				&& rhs instanceof PriorityScheduler.ExecutorSchedulerWorker) {

			Priority lhsPriority = ((PriorityScheduler.ExecutorSchedulerWorker) lhs).getPriority();
			Priority rhsPriority = ((PriorityScheduler.ExecutorSchedulerWorker) rhs).getPriority();

			Ln.d(String.format("lhs priority = %d, rhs priority = %d", lhsPriority.getValue(), rhsPriority.getValue()));
			return lhsPriority.getValue() - rhsPriority.getValue();
		}

		return 0;
	}
}
