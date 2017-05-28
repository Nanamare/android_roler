package com.vocketlist.android.network.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.vocketlist.android.network.error.ParsingException;
import com.vocketlist.android.roboguice.log.Ln;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.StringReader;

import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Created by SeungTaek.Lim on 2016. 11. 15..
 */
final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private static final String TAG = GsonResponseBodyConverter.class.getSimpleName();
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String originData = IOUtils.toString(value.charStream());
        JsonReader jsonReader = gson.newJsonReader(new StringReader(originData));

        try {
            Ln.d("response : " + originData);
            return adapter.read(jsonReader);
        } catch (Exception e) {
            throw new ParsingException(originData, e);
        } finally {
            value.close();
        }
    }
}
