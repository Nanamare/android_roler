package com.vocketlist.android.network.service;

import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.executor.PriorityScheduler;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;

/**
 * Created by SeungTaek.Lim on 2016. 12. 21..
 */

public class ServiceHelper {
    public static Scheduler getPriorityScheduler(Priority priority) {
        RxJavaSchedulersHook hook = RxJavaPlugins.getInstance().getSchedulersHook();
        Scheduler hookScheduler = hook.getComputationScheduler();

        if (hookScheduler != null) {
            return hookScheduler;
        }

        return new PriorityScheduler(priority);
    }
}
