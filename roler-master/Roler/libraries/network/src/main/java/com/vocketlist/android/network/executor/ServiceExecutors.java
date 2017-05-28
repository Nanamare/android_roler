package com.vocketlist.android.network.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ServiceExecutors {
	private static final Object MUTEX = new Object();
	private static final int DEFAULT_QUEUE_SIZE = 100;

	private static final int MINIMUM_THREAD_POOL_SIZE = 2;
	private static final int MAXIMUM_THREAD_POOL_SIZE = 4;

	private static Executor mAPIExecutor;

	public static int getThreadPoolSize() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int threadCount = availableProcessors;
		
		if (threadCount < MINIMUM_THREAD_POOL_SIZE) {
			threadCount = MINIMUM_THREAD_POOL_SIZE;
			
		} else if (threadCount > MAXIMUM_THREAD_POOL_SIZE) {
			threadCount = MAXIMUM_THREAD_POOL_SIZE;
		}
		
		return threadCount;
	}

	public static Executor getAPIExecutor() {
		if (mAPIExecutor != null) {
			return mAPIExecutor;
		}

		synchronized (MUTEX) {
			if (mAPIExecutor != null) {
				return mAPIExecutor;
			}

			int threadPoolSize = getThreadPoolSize();
			mAPIExecutor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize,
					0L, TimeUnit.MILLISECONDS,
					new PriorityBlockingQueue<Runnable>(DEFAULT_QUEUE_SIZE, new PriorityComparator()),
					Executors.defaultThreadFactory());
		}

		return mAPIExecutor;
	}
}
