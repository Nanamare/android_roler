package com.vocketlist.android.network.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by SeungTaek.Lim
 */

public class NoConverterFactory extends Converter.Factory {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain");

    private NoConverterFactory() {

    }

    public static NoConverterFactory create() {
        return new NoConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, Void>() {
            @Override
            public Void convert(ResponseBody value) throws IOException {
                return null;
            }
        };
    }
}
