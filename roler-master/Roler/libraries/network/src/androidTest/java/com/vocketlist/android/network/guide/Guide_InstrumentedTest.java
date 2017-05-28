package com.vocketlist.android.network.guide;

import android.support.test.runner.AndroidJUnit4;

import com.vocketlist.android.network.converter.EnumParameterConverterFactory;
import com.vocketlist.android.network.converter.gson.GsonConverterFactory;
import com.vocketlist.android.network.error.handler.ErrorHandlingCallAdapterBuilder;
import com.vocketlist.android.network.error.handler.RxErrorHandler;
import com.vocketlist.android.network.executor.Priority;
import com.vocketlist.android.network.service.ServiceErrorChecker;
import com.vocketlist.android.network.service.ServiceHelper;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;
import com.vocketlist.android.roboguice.log.Ln;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertNotNull;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Guide_InstrumentedTest {
    private Response<List<User>> response;

    @Before
    public void setup() {
        RxJavaPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getComputationScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void API_호출및_응답_처리_방법() {
        String API_BASE_URL = "https://jsonplaceholder.typicode.com/";

        OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(new WebkitCookieJar())
                .connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
                .readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
                .addInterceptor(new DefaultHeaderInterceptor());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(ErrorHandlingCallAdapterBuilder.create()
                        .setCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addErrorHandler(RxErrorHandler.class)
                        .build())
                .addConverterFactory(EnumParameterConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mDefaultHttpClientBuilder.build())
                .build();

        retrofit.create(JsonplaceholderService.class)
                .getUsers()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<List<User>>(new JsonplaceholderErrorChecker()))
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 필요한 경우 cancel에 대한 처리 해야 함...
                        Ln.d("unsubscribe");
                    }
                })
                .subscribe(new Subscriber<Response<List<User>>>() {
                    @Override
                    public void onCompleted() {
                        Ln.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.d(e, "onError");
                        Ln.d("onError : %s", e.toString());
                    }

                    @Override
                    public void onNext(Response<List<User>> listResponse) {
                        response = listResponse;
                    }
                });

        assertNotNull(this.response);
    }

    @Test
    public void API_호출및_응답_처리_방법_with_mock() {
        String API_BASE_URL = "https://jsonplaceholder.typicode.com/";

        OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(new WebkitCookieJar())
                .connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
                .readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
                .addInterceptor(new DefaultHeaderInterceptor())
                .addInterceptor(new MockInterpolator());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addCallAdapterFactory(ErrorHandlingCallAdapterBuilder.create()
                        .setCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addErrorHandler(RxErrorHandler.class)
                        .build())
                .addConverterFactory(EnumParameterConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mDefaultHttpClientBuilder.build())
                .build();

        retrofit.create(JsonplaceholderService.class)
                .getUsers()
                .subscribeOn(ServiceHelper.getPriorityScheduler(Priority.MEDIUM))
                .lift(new ServiceErrorChecker<List<User>>(new JsonplaceholderErrorChecker()))
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        // 필요한 경우 cancel에 대한 처리 해야 함...
                        Ln.d("unsubscribe");
                    }
                })
                .subscribe(new Subscriber<Response<List<User>>>() {
                    @Override
                    public void onCompleted() {
                        Ln.i("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Ln.d(e, "onError");
                        Ln.d("onError : %s", e.toString());
                    }

                    @Override
                    public void onNext(Response<List<User>> listResponse) {
                        response = listResponse;
                    }
                });

        assertNotNull(this.response);
    }
}
