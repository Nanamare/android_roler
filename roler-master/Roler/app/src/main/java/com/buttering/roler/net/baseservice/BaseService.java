package com.buttering.roler.net.baseservice;

import com.buttering.roler.net.baseservice.basetoken.DefaultHeaderInterceptor;
import com.buttering.roler.net.serialization.RolerResponse;
import com.buttering.roler.net.serialization.RolerResponseDeserializer;
import com.buttering.roler.util.MyApplication;
import com.buttering.roler.util.NetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vocketlist.android.network.service.HttpResponseErrorInterceptor;
import com.vocketlist.android.network.service.LoggingInterceptor;
import com.vocketlist.android.network.service.WebkitCookieJar;
import com.vocketlist.android.network.utils.Timeout;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kinamare on 2016-12-17.
 */

public class BaseService<T> {


	public static final String BASE_URL = "http://52.78.65.255:3000";


	private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {
		Response originalResponse = chain.proceed(chain.request());
		if (NetUtil.isNetworkAvailable(MyApplication.getInstance().getContext())) {
			int maxAge = 5; // read from cache for 5 sec
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, max-age=" + maxAge)
					.build();
		} else {
			int maxStale = 60 * 60 * 24 * 3; // tolerate 3-day stale
			return originalResponse.newBuilder()
					.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
					.build();

		}

	};
	Retrofit retrofit;
	T service;

	public BaseService(final Class<T> clazz) {

		GsonBuilder gsonBuilder = new GsonBuilder();

		// Adding custom deserializers
		gsonBuilder.registerTypeAdapter(RolerResponse.class, new RolerResponseDeserializer());
		Gson myGson = gsonBuilder.create();



		retrofit = new Retrofit.Builder()
				.client(mDefaultHttpClientBuilder.build())
				.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
				.addConverterFactory(GsonConverterFactory.create(myGson))
				.baseUrl(BASE_URL)
				.build();

		service = retrofit.create(clazz);
	}

	protected static String getStatusResult(String json) {
		try {

			JsonObject ja = new JsonParser().parse(json).getAsJsonObject();
			String result = ja.get("result").getAsString();

			return result;
		} catch (Exception e) {
			System.out.print(e);
		}
		return "false";
	}


	public final static OkHttpClient.Builder mDefaultHttpClientBuilder = new OkHttpClient.Builder()
			.cookieJar(new WebkitCookieJar())
			.connectTimeout(Timeout.getConnectionTimeout(), Timeout.UNIT)
			.readTimeout(Timeout.getReadTimeout(), Timeout.UNIT)
			.addInterceptor(new DefaultHeaderInterceptor())
			.addInterceptor(new HttpResponseErrorInterceptor())
			.addNetworkInterceptor(new LoggingInterceptor(MyApplication.getInstance().getContext()));




	public T getAPI() {
		return service;
	}

}
