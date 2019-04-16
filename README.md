# MediaPicker Library for Android


A Media library for Android for single/selecting multiple files(image/video/audio).


## Installation
Step 1: Add the dependency

```gradle
    dependencies {
        ...
        implementation 'com.greentoad.turtlebody:media-picker:1.0.1'
    }
```

## Usage
Step 1: Declare and Initialize media in Activity or Fragment.

```
    private void startPick() {
        PickerConfig pickerConfig = new PickerConfig().setAllowMultiImages(false).setShowDialog(true);
        
        MediaPicker.with(this, pickerConfig, Constants.FileTypes.FILE_TYPE_IMAGE)
                .onResult()
                .subscribe(new Observer<ArrayList<Uri>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Uri> uris) {
                        Log.i("tag", "next: " + uris.toString());
                        //uris: list of uri
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
    }
```

## PickerConfig:
It is use to set the configuration.
1. **.setAllowMultiImages(booleanValue)**: tells whether to select single file or multiple file.
2. **.setAllowMultiImages(booleanValue)**: tells whether to show confirmation dialog on selecting the file(only work in single file selection).

eg.
```
//Pick single file with confirmation dialog
PickerConfig pickerConfig = new PickerConfig().setAllowMultiImages(false).setShowDialog(true);
```

## File types:
The type of file user want to select(its a constant integer value).
1. **FILE_TYPE_IMAGE** : for picking image files
2. **FILE_TYPE_VIDEO** : for picking video files
3. **FILE_TYPE_AUDIO** : for picking audio files


## URI:
We will be returning the list of Uri after selecting the files. That's why it is better to know about Uri first.

A Uniform Resource Identifier (URI) is a compact sequence of characters that identifies an abstract or physical resource.

In Android, Content providers manage access to a structured set of data. They encapsulate the data, and provide mechanisms for defining data security. Content providers are the standard interface that connects data in one process with code running in another process.

#### URI usages:
1. Get file from uri:
```
File file = new File(uri.getPath());
```

2. Get mime from uri:
```
String mimeType = getContentResolver().getType(uri);
```





