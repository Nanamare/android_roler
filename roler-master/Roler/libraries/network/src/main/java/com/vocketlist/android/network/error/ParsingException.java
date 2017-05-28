package com.vocketlist.android.network.error;

/**
 * Created by SeungTaek.Lim on 2016. 11. 21..
 */
public final class ParsingException extends RuntimeException {
    private final String mOriginalData;

    public ParsingException(String originalData, Throwable cause) {
        super(cause);
        mOriginalData = originalData;
    }

    public String getOriginalData() {
        return mOriginalData;
    }

    @Override
    public String getMessage() {
        return "original Data : " + mOriginalData
                + "\nthrow : " + getCause().getMessage();
    }
}
