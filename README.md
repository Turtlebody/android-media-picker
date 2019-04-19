# MediaPicker Library for Android (AndroidX)

[ ![Download](https://api.bintray.com/packages/greentoad/android-media-picker/com.greentoad.turtlebody.mediapicker/images/download.svg?version=1.0.2) ](https://bintray.com/greentoad/android-media-picker/com.greentoad.turtlebody.mediapicker/1.0.2/link)

A Media library for Android for selecting single/multiple files(image/video/audio).


## Setup
Step 1: Add the dependency

```gradle
    dependencies {
        ...
        implementation 'com.greentoad.turtlebody:media-picker:1.0.2'
    }
```

## Usage
Step 1: Declare and Initialize MediaPicker.

#### Java
```java
PickerConfig pickerConfig = new PickerConfig().setAllowMultiImages(false).setShowConfirmationDialog(true);
        
MediaPicker.with(this,Constants.FileTypes.FILE_TYPE_IMAGE)
        .setConfig(pickerConfig)
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
                //uri: list of uri
            }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onComplete() { }
        });
```

#### Kotlin
```kotlin
val pickerConfig = PickerConfig().setAllowMultiImages(allowMultiple).setShowConfirmationDialog(true)
MediaPicker.with(this, fileType)
        .setConfig(pickerConfig)
        .setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener{
            override fun onMissingFileWarning() {
                Toast.makeText(this@ActivityHome,"some file is missing",Toast.LENGTH_LONG).show()
            }
        })
        .onResult()
        .subscribe({
            info { "success: $it" }
        },{
            info { "error: $it" }
        })
```

## Explanation:

#### 1. PickerConfig:
It is use to set the configuration.
1. **.setAllowMultiImages(booleanValue)**: tells whether to select single file or multiple file.
2. **.setAllowMultiImages(booleanValue)**: tells whether to show confirmation dialog on selecting the file(only work in single file selection).

eg.
```
//Pick single file with confirmation dialog
PickerConfig pickerConfig = new PickerConfig().setAllowMultiImages(false).setShowConfirmationDialog(true);
```

#### 2. ExtraListener:
In Android many times the file not exist physically but may contain uri. Such file(uri) may produce error. So in our library we are filtering out invalid uri. So if end-developer wants to know if library filtered out uris, they can set ```.setFileMissingListener()```.

#### Java
```java
.setFileMissingListener(new MediaPicker.MediaPickerImpl.OnMediaListener() {
    @Override
    public void onMissingFileWarning() {
        //trigger when some missing file are filtered out
    }
})
```
#### Kotlin
```kotlin
.setFileMissingListener(object : MediaPicker.MediaPickerImpl.OnMediaListener{
    override fun onMissingFileWarning() {
        //trigger when some missing file are filtered out
    }
})
```
#### 3. Media types:
It's a type of file user want to select.
1. **MEDIA_TYPE_IMAGE** : for picking image files
2. **MEDIA_TYPE_VIDEO** : for picking video files
3. **MEDIA_TYPE_AUDIO** : for picking audio files


### URI:
We will be returning the list of Uri after selecting the files. That's why it is better to know about Uri first.

A Uniform Resource Identifier (URI) is a compact sequence of characters that identifies an abstract or physical resource.

In Android, Content providers manage access to a structured set of data. They encapsulate the data, and provide mechanisms for defining data security. Content providers are the standard interface that connects data in one process with code running in another process.
You can get almost all information from uri.
#### URI usages:
1. Get file from uri:
```
File file = new File(uri.getPath());
```

2. Get mime from uri:
```
String mimeType = getContentResolver().getType(uri);
```

3. Used in Glide:
```java
Glide.with(context)
     .load(uri)
     .into(imageView);
```


---
### Quick Links

*  [ChangeLog](/CHANGELOG.md)
*  [Documentation](https://github.com/Turtlebody/android-media-picker/wiki)

### Demos

*  [Example](/Example.md)
*  [Sample APK file](https://github.com/Turtlebody/android-media-picker/blob/master/app-development-debug.apk)

### Developers

*  [API Documentation](https://github.com/Turtlebody/android-media-picker/wiki/API-Documentation)
*  [Developer Setup & Usage](https://github.com/Turtlebody/android-media-picker/wiki/Developer-Setup)

---





