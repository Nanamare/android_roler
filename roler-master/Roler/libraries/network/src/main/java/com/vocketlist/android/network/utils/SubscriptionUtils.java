package com.vocketlist.android.network.utils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

public class SubscriptionUtils {
    /**
     * Automatically adds to / removes from specified {@link CompositeSubscription} when subscribe / unsubscribe is called.
     * Useful when subscribing observable repeatedly, as you do not need to manipulate CompositeSubscription manually.
     */
    public static <T> Observable.Operator<T, T> composite(final CompositeSubscription compositeSubscription) {
        return new Observable.Operator<T, T>() {
            @Override
            public Subscriber<? super T> call(final Subscriber<? super T> subscriber) {
                compositeSubscription.add(subscriber);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        compositeSubscription.remove(subscriber);
                    }
                }));
                return subscriber;
            }
        };
    }
}