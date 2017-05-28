package com.vocketlist.android.network.error.handler;

import com.vocketlist.android.roboguice.log.Ln;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import rx.Observable;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by SeungTaek.Lim on 2016. 11. 22..
 * 참고 : http://bytes.babbel.com/en/articles/2016-03-16-retrofit2-rxjava-error-handling.html
 */
public class ErrorHandlingCallAdapterBuilder {
    private ErrorHandlingCallAdapterFactory mCallAdapterFactory = new ErrorHandlingCallAdapterFactory();

    public static ErrorHandlingCallAdapterBuilder create() {
        return new ErrorHandlingCallAdapterBuilder();
    }

    public ErrorHandlingCallAdapterBuilder addErrorHandler(Class<? extends ErrorHandler> handler) {
        mCallAdapterFactory.addErrorHandler(handler);
        return this;
    }

    public ErrorHandlingCallAdapterBuilder setCallAdapterFactory(CallAdapter.Factory factory) {
        mCallAdapterFactory.setWrapperCallAdapterFactory(factory);
        return this;
    }

    public CallAdapter.Factory build() {
        assertNotNull(mCallAdapterFactory.mWrapper);
        return mCallAdapterFactory;
    }

    private class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
        private ArrayList<Class<? extends ErrorHandler>> mErrorHandler = new ArrayList<>();
        private CallAdapter.Factory mWrapper;

        @Override
        public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            return new CallAdapterWrapper(retrofit, mWrapper.get(returnType, annotations, retrofit), mErrorHandler);
        }

        void addErrorHandler(Class<? extends ErrorHandler> handler) {
            mErrorHandler.add(handler);
        }

        void setWrapperCallAdapterFactory(CallAdapter.Factory factory) {
            mWrapper = factory;
        }
    }

    private static class CallAdapterWrapper implements CallAdapter<Observable<?>> {
        private static final String TAG = CallAdapterWrapper.class.getSimpleName();

        private final ArrayList<Class<? extends ErrorHandler>> mErrorHandler;
        private final CallAdapter<?> mWrapped;
        private final Retrofit mRetrofit;

        CallAdapterWrapper(Retrofit retrofit, CallAdapter<?> wrapped, ArrayList<Class<? extends ErrorHandler>> errorHandler) {
            mRetrofit = retrofit;
            mWrapped = wrapped;
            mErrorHandler = errorHandler;
        }

        @Override
        public Type responseType() {
            return mWrapped.responseType();
        }

        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            HttpUrl url = call.request().url();

            Observable observable =  ((Observable) mWrapped.adapt(call));
            Object newInstance = null;

            for (Class<? extends ErrorHandler> handler : mErrorHandler) {
                try {
                    newInstance = handler.getClassLoader().loadClass(handler.getName()).newInstance();
                    ((ErrorHandler) newInstance).set(mRetrofit, url, mWrapped);
                    observable = observable.onErrorResumeNext((ErrorHandler) newInstance);

                } catch (ClassNotFoundException e) {
                    Ln.e(e.toString());
                } catch (InstantiationException e) {
                    Ln.e(e.toString());
                } catch (IllegalAccessException e) {
                    Ln.e(e.toString());
                }
            }

            return observable;
        }
    }
}
