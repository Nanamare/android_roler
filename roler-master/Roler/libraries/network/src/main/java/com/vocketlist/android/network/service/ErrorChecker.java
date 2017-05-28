package com.vocketlist.android.network.service;

public interface ErrorChecker<T> {
    void checkError(T data) throws RuntimeException;
}
