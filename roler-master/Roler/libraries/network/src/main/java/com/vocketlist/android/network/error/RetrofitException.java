package com.vocketlist.android.network.error;

import com.vocketlist.android.network.utils.NetworkState;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.HttpUrl;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static junit.framework.Assert.assertNotNull;

/**
 * @author Seungtaek.lim
 */
public final class RetrofitException extends RuntimeException {
    public static RetrofitException httpError(HttpUrl url, Response response, Throwable cause, Retrofit retrofit) {
        assertNotNull(url);
        assertNotNull(response);
        assertNotNull(cause);
        assertNotNull(retrofit);

        String message = createMessage(url, response, Kind.HTTP, cause);
        return new RetrofitException(message, url, response, Kind.HTTP, cause, retrofit);
    }

    public static RetrofitException networkError(HttpUrl url, IOException exception, Retrofit retrofit) {
        assertNotNull(url);
        assertNotNull(exception);
        assertNotNull(retrofit);

        String message = createMessage(url, null, Kind.NETWORK, exception);
        return new RetrofitException(message, url, null, Kind.NETWORK, exception, retrofit);
    }

    public static RetrofitException unexpectedError(HttpUrl url, Response response, Throwable exception) {
        assertNotNull(url);
        assertNotNull(exception);

        String message = createMessage(url, response, Kind.UNEXPECTED, exception);
        return new RetrofitException(message, url, null, Kind.UNEXPECTED, exception, null);
    }

    /** Identifies the event kind which triggered a {@link RetrofitException}. */
    public enum Kind {
        /** An {@link IOException} occurred while communicating to the server. */
        NETWORK,
        /** A non-200 HTTP status code was received from the server. */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    private final HttpUrl url;
    private final Response response;
    private final Kind kind;
    private final Retrofit retrofit;

    RetrofitException(String message, HttpUrl url, Response response, Kind kind, Throwable exception, Retrofit retrofit) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.kind = kind;
        this.retrofit = retrofit;
    }

    /** The request URL which produced the error. */
    public HttpUrl getUrl() {
        return url;
    }

    /** Response object containing status code, headers, body, etc. */
    public Response getResponse() {
        return response;
    }

    /** The event kind which triggered this error. */
    public Kind getKind() {
        return kind;
    }

    /** The Retrofit this request was executed on */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    /**
     * HTTP response body converted to specified {@code type}. {@code null} if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified {@code type}.
     */
    public <T> T getErrorBodyAs(Class<T> type) throws IOException {
        if (response == null || response.errorBody() == null) {
            return null;
        }
        Converter<ResponseBody, T> converter = retrofit.responseBodyConverter(type, new Annotation[0]);
        return converter.convert(response.errorBody());
    }

    protected static String createMessage(HttpUrl url, Response response, Kind kind, Throwable exception) {
        StringBuilder builder = new StringBuilder();
        builder.append("Request URL : " + url);
        builder.append("\nkind : " + kind.name());

        if (response != null) {
            builder.append("\n<http>");
            builder.append("\ncode : " + response.code());
            builder.append("\nbody : " + response.body());
            builder.append("\nerror body : " + response.errorBody());
            builder.append("\nmessage : " + response.message());
        }

        builder.append("\nerror : " + exception.getMessage());
        builder.append("\ncause : " + exception.getCause());
        builder.append("\nwifi network name : " + NetworkState.getInstance().getConnectedWifiNetowkName());
        builder.append("\nwifi strength level : " + NetworkState.getInstance().getConntectedWifiSignalLevel());
        builder.append("\nproxy Info : " + NetworkState.getInstance().getProxyHost() + ":" + NetworkState.getInstance().getProxyPort());

        return builder.toString();
    }
}
