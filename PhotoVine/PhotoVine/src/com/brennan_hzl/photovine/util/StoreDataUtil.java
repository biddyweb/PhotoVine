package com.brennan_hzl.photovine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.widget.Toast;

public class StoreDataUtil {
	
	public static void saveImgFileToAppInternalStorage(Bitmap bitmap, String filePath) {
		File file = new File(filePath);
		FileOutputStream fos = null;
		try {
	       if (!file.exists()) {
	    	   file.createNewFile();
	       }
	       fos = new FileOutputStream(file);
	       bitmap.compress(CompressFormat.JPEG, 100, fos); 
	       fos.flush();
	       fos.close();
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	public static boolean copyFile(String oldPath, String newPath) {
	       boolean isok = true;
	       try {
	           int byteread = 0;
	           File oldfile = new File(oldPath);
	           if (oldfile.exists()) { 
	               InputStream inStream = new FileInputStream(oldPath); 
	               FileOutputStream fs = new FileOutputStream(newPath);
	               byte[] buffer = new byte[1024];

	               while ( (byteread = inStream.read(buffer)) != -1) {
	                   fs.write(buffer, 0, byteread);
	               }
	               fs.flush();
	               fs.close();
	               inStream.close();
	           }
	           else
	           {
	            isok = false;
	           }
	       }
	       catch (Exception e) {
	          e.printStackTrace();
	          isok = false;
	       }
	       return isok;
	 
	   } 
		
	
	public static File getTempPathOfAppInternalStorage(Context context) {
	    String str = context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "temp";
	    File tmp = new File(str);
	    if (!tmp.exists())
	    	tmp.mkdir();
	    return tmp;
	}
	
	public static File getTempImagePathOfAppInternalStorage(Context context) {
	    String str = context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "temp" + File.separator +"images";
	    File tmp = new File(str);
	    if (!tmp.exists())
	    	tmp.mkdirs();
	    return tmp;
	}
	
	public static File getTempMusicPathOfAppInternalStorage(Context context) {
	    String str = context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "temp" + File.separator +"music";
	    File tmp = new File(str);
	    if (!tmp.exists())
	    	tmp.mkdirs();
	    return tmp;
	}
	
	public static File getPathOfStoreSlideShow(Context context) {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(context, "waibucunchu", Toast.LENGTH_SHORT).show();
			return null;
		}
		String externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		File tmp = new File(externalStoragePath, "photoVine");
		if (!tmp.exists())
	    	tmp.mkdir();
	    return tmp;
	}
	
	public static void clearTempFileOfAppInternalStorage(Context context) {
		String str = context.getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "temp";
	    File tmp = new File(str);
	    if (tmp.exists()) {
	    	deleteFilesInDirectory(tmp);
	    }
	}
	
	public static void deleteFilesInDirectory(File directory) {
		if (directory.exists()) {
	    	File[] files = directory.listFiles();
	    	for (int i=0; i<files.length; i++) {
	    		if (files[i].isDirectory()) {
	    			deleteFilesInDirectory(files[i]);
		    	} 
	    		files[i].delete();
	    	}
	    }
	}
	
}