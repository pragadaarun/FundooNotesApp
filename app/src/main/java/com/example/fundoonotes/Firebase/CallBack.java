package com.example.fundoonotes.Firebase;

public interface CallBack<T>{
    void onSuccess(T data);
    void onFailure(Exception exception);
}
