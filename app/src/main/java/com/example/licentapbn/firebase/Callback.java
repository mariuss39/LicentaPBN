package com.example.licentapbn.firebase;

public interface Callback<T> {

    void runResultOnUiThread(T result);
}
