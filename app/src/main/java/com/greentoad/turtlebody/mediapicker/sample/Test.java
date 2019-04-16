package com.greentoad.turtlebody.mediapicker.sample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.greentoad.turtlebody.mediapicker.core.MediaPicker;
import com.greentoad.turtlebody.mediapicker.core.PickerConfig;

import java.util.ArrayList;

import androidx.fragment.app.FragmentActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by WANGSUN on 16-Apr-19.
 */
public class Test {

    FragmentActivity context;

    //@SuppressLint("CheckResult")
    void fun1(){
        Observable<ArrayList<Uri>> fileList = MediaPicker.with(context,new PickerConfig().setAllowMultiImages(true),34).onResult();


        fileList.subscribe(new Observer<ArrayList<Uri>>() {
            @Override
            public void onSubscribe(Disposable d) { }

            @Override
            public void onNext(ArrayList<Uri> uris) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
