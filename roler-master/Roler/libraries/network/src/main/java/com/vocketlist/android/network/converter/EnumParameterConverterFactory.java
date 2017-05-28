package com.vocketlist.android.network.converter;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * 다음과 같은 우선순위로 Enum으로부터 String을 반환한다
 *      1. @SerializedName 어노테이션이 존재하는 경우
 *      2. toString() Override한 경우
 *      3. Enum.name()
 *
 * Created by SeungTaek.Lim
 */
public class EnumParameterConverterFactory extends Converter.Factory {
    public static EnumParameterConverterFactory create() {
        return new EnumParameterConverterFactory();
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class && ((Class<?>)type).isEnum()) {
            return new EnumStringConverter();
        }

        return null;
    }

    class EnumStringConverter implements Converter<Object, String> {
        EnumStringConverter() {
        }

        @Override
        public String convert(Object value) throws IOException {
            String data = null;
            try {
                data = value.getClass().getField(((Enum) value).name()).getAnnotation(SerializedName.class).value();
            } catch (NoSuchFieldException exception) {

            } catch (Exception e) {

            }

            return (data == null) ? value.toString() : data;
        }
    }
}
