package com.example.fundoonotes.Firebase;

interface CallBack<T>{
    void onSuccess(T data);
    void onFailure(Exception exception);
}
