package com.buttering.roler.composition.baseservice;

import android.content.Context;
import android.text.TextUtils;

import com.buttering.roler.VO.MyInfoDAO;
import com.buttering.roler.composition.baseservice.cookies.AddCookiesInterceptor;
import com.buttering.roler.composition.baseservice.cookies.ReceivedCookiesInterceptor;
import com.buttering.roler.composition.serialization.RolerResponse;
import com.buttering.roler.composition.serialization.RolerResponseDeserializer;
import com.buttering.roler.util.MyApplication;
import com.buttering.roler.util.NetUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
	OkHttpClient.Builder httpClient;
	int counter = 0;

	public BaseService(final Class<T> clazz) {

		GsonBuilder gsonBuilder = new GsonBuilder();

		// Adding custom deserializers
		gsonBuilder.registerTypeAdapter(RolerResponse.class, new RolerResponseDeserializer());
		Gson myGson = gsonBuilder.create();



//		AddCookiesInterceptor in1 = new AddCookiesInterceptor();
//		ReceivedCookiesInterceptor in2 = new ReceivedCookiesInterceptor();
//
//		OkHttpClient httpClient = new OkHttpClient.Builder()
//				.addNetworkInterceptor(in1)
//				.addInterceptor(in2)
//				.build();



		retrofit = new Retrofit.Builder()
//				.client(httpClient)
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


	private void setTokenInterceptor(String authToken) {
		httpClient.addInterceptor(chain -> {

			Request original = chain.request();

			Request.Builder reqBuilder = original.newBuilder()
					.header("Authorization", TokenTYPE.BEARER.type + " " + authToken)
					.method(original.method(), original.body());

			Request req = reqBuilder.build();

			return chain.proceed(req);
		});
	}



	private enum TokenTYPE {
		BEARER("Bearer");
		private String type;

		TokenTYPE(String type) {
			this.type = type;
		}
	}

	public T getAPI() {
		return service;
	}

}
