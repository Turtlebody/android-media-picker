package com.greentoad.turtlebody.mediapicker.sample.test;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.greentoad.turtlebody.mediapicker.MediaPicker;
import com.greentoad.turtlebody.mediapicker.core.MediaPickerConfig;
import com.greentoad.turtlebody.mediapicker.sample.R;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class TestActivityPicker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_picker);


        Button picker = (Button) findViewById(R.id.id_picker);

        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPick();
            }
        });
    }

    private void startPick() {
        MediaPickerConfig mediaPickerConfig = new MediaPickerConfig()
                .setAllowMultiSelection(false)
                .setUriPermanentAccess(true)
                .setShowConfirmationDialog(true);

        MediaPicker.with(this, MediaPicker.MediaTypes.IMAGE)
                .setConfig(mediaPickerConfig)
                .setFileMissingListener(new MediaPicker.MediaPickerImpl.OnMediaListener() {
                    @Override
                    public void onMissingFileWarning() {
                        //trigger when some file are missing
                    }
                })
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        Log.i("tag", "next: " + uris.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("tag", "error: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i("tag", "complete");
                    }
                });


        Uri uri = Uri.parse("");
        File file = new File(uri.getPath());

        String mimeType = getContentResolver().getType(uri);
    }
}
