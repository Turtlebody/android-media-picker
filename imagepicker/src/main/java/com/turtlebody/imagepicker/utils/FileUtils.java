package com.turtlebody.imagepicker.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.turtlebody.imagepicker.models.Folder;
import com.turtlebody.imagepicker.models.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FileUtils {
    
  public final static int FILE_TYPE_IMAGES = 501;
  public final static int FILE_TYPE_VIDEOS = 502;

  public final static Map<Integer, String> titleMap = new HashMap<Integer, String>() {
    {
      put(FILE_TYPE_IMAGES, "All Images");
      //put(FILE_TYPE_VIDEOS, "All Videos");
    }
  };

  public final static Map<Integer, Uri> uriMap = new HashMap<Integer, Uri>() {
    {
      put(FILE_TYPE_IMAGES, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      //put(FILE_TYPE_VIDEOS, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }
  };

  public final static String FILE_TYPE_TO_CHOOSE = "in.arj.fileselectionlib.FILE_TYPE_TO_CHOOSE";
  public static final String SELECTED_FILES = "selected_files";

  public static ArrayList<Folder> fetchLocalFolders(Context context) {
    ArrayList<Folder> Folders = new ArrayList<>();
    Map<String, Integer> FolderFileCountMap = new HashMap<>();
    
    String[] projection = getFolderProjection(FILE_TYPE_IMAGES);
    Uri queryUri = uriMap.get(FILE_TYPE_IMAGES);
    
    // Create the cursor pointing to the SDCard
    Cursor cursor = context.getContentResolver().query(queryUri, projection, // Which columns to return
            null,       // Return all rows
            null, MediaStore.MediaColumns.DATE_ADDED + " DESC");
    int columnIndexFolderId = cursor.getColumnIndexOrThrow(projection[0]);
    int columnIndexFolderName = cursor.getColumnIndexOrThrow(projection[1]);
    int columnIndexFilePath = cursor.getColumnIndexOrThrow(projection[2]);
    while (cursor.moveToNext()) {
      //            Log.i("Folder_DISPLAY_NAME ", cursor.getString(columnIndexFolderName) + " " + cursor.getString(columnIndexFolderId));
      String folderId = cursor.getString(columnIndexFolderId);
      if (FolderFileCountMap.containsKey(folderId)) {
        FolderFileCountMap.put(folderId, FolderFileCountMap.get(folderId) + 1);
      } else {
        Folder Folder = new Folder();
        Folder.setFolderId(folderId);
        Folder.setFolderName(cursor.getString(columnIndexFolderName));
        Folder.setFolderCoverImageFilePath(cursor.getString(columnIndexFilePath));
        Folders.add(Folder);
        FolderFileCountMap.put(folderId, 1);
      }
    }
    for (Folder Folder : Folders) {
      Folder.setFolderContentCount(FolderFileCountMap.get(Folder.getFolderId()));
    }
    cursor.close();
    return Folders;
  }

  private static String[] getFolderProjection(int fileType) {
    if (fileType == FILE_TYPE_IMAGES) {
      return new String[] {
          MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
          MediaStore.Images.Media.DATA
      };
    } else if (fileType == FILE_TYPE_VIDEOS) {
      return new String[] {
          MediaStore.Video.Media.BUCKET_ID, MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
          MediaStore.Video.Media.DATA
      };
    } else {
      return new String[] {
          MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.AudioColumns.ALBUM,
          MediaStore.Audio.Media.DATA
      };
    }
  }

  public static ArrayList<Image> getFilesInFolder(Context context, String FolderId) {
    ArrayList<Image> fileItems = new ArrayList<>();
    String[] projection = getFileProjection(FILE_TYPE_IMAGES);
    Uri queryUri = uriMap.get(FILE_TYPE_IMAGES);
    // Create the cursor pointing to the SDCard
    Cursor cursor =
        context.getContentResolver().query(queryUri, projection, // Which columns to return
            projection[4] + " = '" + FolderId + "'",       // Return all rows
            null, MediaStore.MediaColumns.DATE_ADDED + " DESC");
    int columnIndexFileId = cursor.getColumnIndexOrThrow(projection[0]);
    int columnIndexFileName = cursor.getColumnIndexOrThrow(projection[1]);
    int columnIndexFileSize = cursor.getColumnIndexOrThrow(projection[2]);
    int columnIndexFilePath = cursor.getColumnIndexOrThrow(projection[3]);
    int columnIndexFileThumbPath = cursor.getColumnIndexOrThrow(projection[5]);
    while (cursor.moveToNext()) {
      Image fileItem = new Image();
      fileItem.setImageId(cursor.getString(columnIndexFileId));
      fileItem.setImageName(cursor.getString(columnIndexFileName));
      fileItem.setImagePath( cursor.getString(columnIndexFilePath));
      fileItem.setImageSize(cursor.getString(columnIndexFileSize));
      fileItem.setImageThumbnailPath(cursor.getString(columnIndexFileThumbPath));
      fileItems.add(fileItem);
    }
    cursor.close();
    return fileItems;
  }

  private static String[] getFileProjection(int fileType) {
    switch (fileType) { 
        case FILE_TYPE_IMAGES: 
            return new String[] {
                    MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Thumbnails.DATA
            };
            default: 
                return new String[] {
                        MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Thumbnails.DATA
                }; 
//      case FILE_TYPE_VIDEOS:
//        return new String[] {
//            MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME,
//            MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DATA,
//            MediaStore.Video.Media.Folder_ID, MediaStore.Video.Thumbnails.DATA
//        };
//      case FILE_TYPE_AUDIO:
//        return new String[] {
//            MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME,
//            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.SIZE
//        };
    }
  }
}

