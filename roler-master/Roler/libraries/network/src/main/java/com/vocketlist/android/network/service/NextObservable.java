package com.vocketlist.android.network.service;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.exceptions.Exceptions;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Func1;
import rx.internal.producers.ProducerArbiter;
import rx.internal.util.RxJavaPluginUtils;
import rx.subscriptions.SerialSubscription;

/**
 * 본 코드는 OperatorOnErrorResumeNextViaFunction 클래스와 OperatorMap 클래스를 참고하여
 * 개발한 것이다.
 *
 * Created by SeungTaek.Lim on 2016. 12. 28..
 */

public final class NextObservable<Input, Output> implements Observable.Operator<Output, Input> {
    final Func1<? super Input, ? extends Observable<? extends Output>> mTransformer;

    public NextObservable(Func1<? super Input, ? extends Observable<? extends Output>> transformer) {
        mTransformer = transformer;
    }

    @Override
    public Subscriber<? super Input> call(final Subscriber<? super Output> parent) {
        ProducerArbiter producer = new ProducerArbiter();
        // 다른 외부 쓰레드에서 unsubscriber를 호출하는 경우를 대비하기 위하여 SerialSubscription를 사용.
        SerialSubscription serialSubscription = new SerialSubscription();

        Subscriber<Input> nextSubscriber = new NextOperatorSubscriber(parent, mTransformer, producer, serialSubscription);
        serialSubscription.set(nextSubscriber);

        parent.add(serialSubscription);     // unsubscribe가 발생하는 경우를 대비하기 위해.
        parent.setProducer(producer);

        return nextSubscriber;
    }

    static final class NextOperatorSubscriber<Input, Output> extends Subscriber<Input> {
        private final Subscriber mParent;
        private final Func1<? super Input, ? extends Observable<? extends Output>> mNextOperator;
        private final ProducerArbiter mProducer;
        private final SerialSubscription mSubscription;

        private boolean done;

        NextOperatorSubscriber(Subscriber<? super Output> parent, Func1<? super Input, ? extends Observable<? extends Output>> transformer, ProducerArbiter producer, SerialSubscription subscription) {
            mParent = parent;
            mNextOperator = transformer;
            mProducer = producer;
            mSubscription = subscription;
        }

        @Override
        public void onCompleted() {
            if (done) {
                return;
            }
            done = true;
            mParent.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            if (done) {
                RxJavaPluginUtils.handleException(e);
                return;
            }

            done = true;
            mParent.onError(e);
        }

        @Override
        public void onNext(Input input) {
            if (done) {
                return;
            }

            try {
                Subscriber<Output> next = new Subscriber<Output>() {
                    @Override
                    public void onNext(Output t) {
                        mParent.onNext(t);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mParent.onError(e);
                    }
                    @Override
                    public void onCompleted() {
                        mParent.onCompleted();
                    }

                    @Override
                    public void setProducer(Producer producer) {
                        mProducer.setProducer(producer);
                    }
                };

                // 기존에 동작중인 NextOperatorSubscriber 중지 시켜야 unsafeSubscibe할때 정상적으로 동작된다.
                mSubscription.set(next);

                Observable<? extends Output> resume = mNextOperator.call(input);
                resume.unsafeSubscribe(next);

            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);

                unsubscribe();
                onError(OnErrorThrowable.addValueAsLastCause(ex, input));
            }
        }

        @Override
        public void setProducer(final Producer producer) {
            mProducer.setProducer(producer);
        }
    }
}
